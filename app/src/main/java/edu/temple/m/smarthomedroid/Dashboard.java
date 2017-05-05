package edu.temple.m.smarthomedroid;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.temple.m.smarthomedroid.Adapters.GridAdapter;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Objects.Relay2;
import edu.temple.m.smarthomedroid.Objects.Sensor;


import static java.lang.Thread.sleep;

/**
 * Created by quido on 3/25/17.
 */

public class Dashboard extends Fragment {
    private String housename;
    private ArrayList<Sensor> sensorList;
    private ArrayList<Relay2> relayList;
    private Spinner listhouse;
    private JSONArray houses,rel, sens;
    private GridAdapter sAdapter, rAdapter;
    private String TAG = "Dashboard";
    private String usern = "Tom Brady";
    private String sessionId;
    DataPassListener mCallback;
    private int done=0;

    public interface DataPassListener{
        public void passData(String data);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (DataPassListener) activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_dashboard,container,false);
        sessionId = getArguments().getString("SessionToken");
        listhouse = (Spinner)v.findViewById(R.id.listhouse);
        return v;
    }
    private synchronized void setdone(int n){
        this.done =n;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        additem();
        listhouse.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        sensorList = new ArrayList<>();
                        sAdapter = new GridAdapter(getActivity().getApplicationContext(), sensorList);
                        relayList = new ArrayList<Relay2>();
                        rAdapter = new GridAdapter(getActivity().getApplicationContext(), relayList);
                        Object item = parent.getItemAtPosition(pos);
                        housename = item.toString();
                        Log.d(TAG,housename);
                        mCallback.passData(housename);
                        new RetrieveSensors().execute();
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (sens!=null){
                            int o=0;
                            for(;o<sens.length();o++){
                                Log.d(TAG,"get in: "+sens);
                                try {
                                    JSONObject check = sens.getJSONObject(o);
                                    String name1 = check.getString("PeripheralName");
                                    int val=check.getInt("PeripheralValue");
                                    sAdapter.add(new Sensor(sessionId, housename, name1, val));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            ListView lv = (ListView)getView().findViewById(R.id.sensors_view);
                            lv.setAdapter(sAdapter);
                        }
                        new getRe().execute();
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (rel!=null){
                            int o=0;
                            for(;o<rel.length();o++){
                                Log.d(TAG,"get in: "+rel);
                                try {
                                    JSONObject check = rel.getJSONObject(o);
                                    String name1 = check.getString("PeripheralName");
                                    int val=check.getInt("PeripheralValue");
                                    rAdapter.add(new Relay2(sessionId, housename, name1, val));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            ListView lv = (ListView)getView().findViewById(R.id.relays_view);
                            lv.setAdapter(rAdapter);
                        }
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
    }
    public void additem(){

        List<String> list1 = new ArrayList<String>();
        new getlist().execute();
        try {
            sleep(1800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (houses!=null) {
            int o=0;
            for(;o<houses.length();o++){
                try {
                    list1.add(houses.getJSONObject(o).getString("HouseName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ArrayAdapter<String> dataAdapter;
            dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.item_spinner,list1);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listhouse.setAdapter(dataAdapter);
        }else{
            Log.d(TAG,"null");
        }
    }
    private class getlist extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String session = sessionId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("sessionToken", session);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            JSONArray ii=null;
            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/house/listhouses", jsonObject);

            if(resp != null){
                Log.d(TAG, "Join House: " + resp);
                try {
                    ii = new JSONArray(resp);
                    houses=ii.getJSONArray(0);
                    Log.d(TAG,"Resp: "+houses);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }
    private class RetrieveSensors extends AsyncTask<Void, Void, Void> {
        JSONObject json = new JSONObject();
        String house = housename;
        String session = sessionId;
        String resp;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            if(house != null && session != null) {
                try {
                    json.put("houseName", house);
                    json.put("sessionToken", session);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            JSONArray arr = new JSONArray();

            //Make a request to url and get response
            resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/sensor/getsensorvaluesbyhouse", json);

            if(resp != null){
                Log.d(TAG, "Retrieve Sensor Response: " + resp);
                try {
                    arr= new JSONArray(resp);
                    arr=arr.getJSONArray(0);
                    sens=arr;
                    Log.d(TAG,"Array : "+ sens);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
        }
    }

    private class getRe extends AsyncTask<Void, Void, Void> {
        JSONObject json = new JSONObject();
        String house = housename;
        String session = sessionId;
        String resp1;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            if(house != null && session != null) {
                try {
                    json.put("HouseName", house);
                    json.put("SessionToken", session);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            JSONArray arr = new JSONArray();

            //Make a request to url and get response
            resp1 = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/dev/relay/getrelayvaluesbyhouseid", json);

            if (resp1!=null) {
                Log.d(TAG, "Retrieve Relay Response string: " + resp1);
                try {
                    arr= new JSONArray(resp1);
                    arr=arr.getJSONArray(0);
                    rel=arr;
                    Log.d(TAG,"Array : "+ rel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
        }
    }
    public void onBackPressed()
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }
}
