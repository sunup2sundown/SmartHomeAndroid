package edu.temple.m.smarthomedroid;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.temple.m.smarthomedroid.Adapters.RelayAdapter;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler2;
import edu.temple.m.smarthomedroid.Objects.Relay;

import static java.lang.Thread.sleep;

/**
 * Created by M on 4/4/2017.
 */

public class RelayFragment extends ListFragment implements AdapterView.OnItemClickListener{

    private ArrayList<Relay> relayList;
    final String TAG = "RelayFragment";
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
        //Create new view from relay fragment layout
        View view = inflater.inflate(R.layout.fragment_relay, container, false);
        bundle = new Bundle();
        //Receive argument bundle from Home Activity
        userID = getArguments().getString("Username");
        sessionID = getArguments().getString("SessionToken");

        bundle.putString("SessionToken", sessionID);

        return view;
    }
    public void onBackPressed()
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        houseName = getArguments().getString("HouseName");
        sessionToken = getArguments().getString("SessionToken");//"018C98BB-C886-44B1-8667-DA304872B452";//"3CEB721D-BDE8-4CBC-950F-E70568D2A2DE";
        //Construct data source
        relayList = new ArrayList<Relay>();
        //Populate relaysList from API call
        //populateList();

        new RetrieveRelays().execute();
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Create and set custom adapter for relay list
        RelayAdapter rAdapter = new RelayAdapter(getContext(), relayList, sessionToken);

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
                rAdapter.add(new Relay(sessionToken, houseName, name, val));
            }
        }else{

        }
        ListView lv = (ListView)getView().findViewById(R.id.fragment_relay_listview);
        lv.setAdapter(rAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Toast.makeText(getActivity(), "Clicked: " + position, Toast.LENGTH_SHORT).show();
    }

    private void populateList(){
        Relay newRelay = new Relay(sessionToken,houseName, "Test Relay", 0);

        relayList.add(0, newRelay);
    }

    /*
    ** Asynchronous Tasks -- HTTP Calls
     *
     */

    private class RetrieveRelays extends AsyncTask<Void, Void, Void> {
        JSONObject json = new JSONObject();
        String house = houseName;
        String session = sessionToken;
        String resp;

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
            String resp;
            //Make a request to url and get response
            resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/dev/relay/getrelayvaluesbyhouseid", json);
            if (resp!=null){
                Log.d(TAG, "Retrieve Relay Response: " + resp);
                try {
                    jArray=new JSONArray(resp);
                    jArray=jArray.getJSONArray(0);
                    Log.d(TAG, "Retrieve Relay Array: " + jArray);
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

    private void populateList(String response){
        String name;
        String value;

        try {
            JSONObject respObject = new JSONObject(response);
            JSONArray respArray = respObject.getJSONArray("");

            for(int i = 0; i < respArray.length(); i++){
                JSONObject curr = respArray.getJSONObject(i);
                name = curr.getString("PeripheralName");
                value = curr.getString("PeripheralValue");
                Log.d(TAG, "Name: "+ name);
                Log.d(TAG, "Value:" + value);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
    }
}

