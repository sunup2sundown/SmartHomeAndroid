package edu.temple.m.smarthomedroid;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.temple.m.smarthomedroid.Adapters.RelayAdapter;
import edu.temple.m.smarthomedroid.Adapters.SensorAdapter;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Objects.Relay;
import edu.temple.m.smarthomedroid.Objects.Sensor;

import static java.lang.Thread.sleep;

/**
 * Created by M on 4/4/2017.
 */

public class SensorFragment extends ListFragment{

    ArrayList<Sensor> sensorList;

    final String TAG = "SensorFragment";
    private String sessionToken = "";
    private String houseName = "";
    private JSONArray jArray;

    private Bundle bundle;
    private String userID;
    private String sessionID;

    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //Create new view from Sensor fragment layout
        View view = inflater.inflate(R.layout.fragment_sensors, container, false);
        bundle = new Bundle();
        //Receive argument bundle from Home Activity
        userID = getArguments().getString("Username");
        sessionID = getArguments().getString("SessionToken");

        bundle.putString("SessionToken", sessionID);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        jArray = new JSONArray();
        houseName = getArguments().getString("HouseName");
        sessionToken = getArguments().getString("SessionToken");//"018C98BB-C886-44B1-8667-DA304872B452";//"3CEB721D-BDE8-4CBC-950F-E70568D2A2DE";
        //Construct data source
        sensorList = new ArrayList<Sensor>();
        new RetrieveSensors().execute();

        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Create and set custom adapter for sensor  list
        SensorAdapter sAdapter = new SensorAdapter(getContext(), sensorList, sessionToken);


        int k=0;
        if(jArray!=null) {
            int o =0;
            for(;o<jArray.length();o++){
                String name="";
                int val=0;
                try {
                    JSONObject check= jArray.getJSONObject(o);
                    name = check.getString("PeripheralName");
                    val = check.getInt("PeripheralValue");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sAdapter.add(new Sensor(sessionToken, houseName, name, val));
            }
        }else{

        }
        ListView lv = (ListView)getView().findViewById(R.id.fragment_sensors_listview);
        lv.setAdapter(sAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = parent.getItemAtPosition(position);
                Sensor sensor = (Sensor)object;
                String name = sensor.getName();

                Log.d(TAG, "Clicked Sensor: " + name);

                Bundle bundle = new Bundle();
                bundle.putString("SessionToken", sessionID);
                bundle.putString("HouseName", houseName);
                bundle.putString("PeripheralName", name);


                Fragment fragment = new SensorGraphFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.flContent, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }


    /*
    ** Asynchronous Tasks -- HTTP Calls
     *
     */

    private class RetrieveSensors extends AsyncTask<Void, Void, Void> {
        JSONObject json = new JSONObject();
        String house = houseName;
        String session = sessionToken;
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

            //Make a request to url and get response
            resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/sensor/getsensorvaluesbyhouse", json);

            if(resp != null){
                Log.d(TAG, "Retrieve Sensor Response: " + resp);
                try {
                    jArray=new JSONArray(resp);
                    jArray=jArray.getJSONArray(0);
                    Log.d(TAG, "Retrieve Sensor Array: " + jArray);
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
}
