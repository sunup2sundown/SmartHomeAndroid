package edu.temple.m.smarthomedroid;


import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Objects.Relay;


/**
 * A simple {@link Fragment} subclass.
 */
public class RelayFragment extends Fragment {
    /* private Switch ss1,ss2,ss3,ss4,ss5,ss6,ss7; */
    public RelayFragment() {
        // Required empty public constructor
    }

    private String TAG = RelayFragment.class.getSimpleName();
    private ListAdapter mAdapter;
    ArrayList<Relay> relaysList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_relay_layout,container,false);



        return v;
    }

    private class PopulateRelays extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0){



            return null;
        }

        @Override
        protected void onPostExecute(Void result){

        }
    }

    private class SignalRelay extends AsyncTask<Void, Void, Void>{
        JSONObject json;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            try {
                json.put("SessionToken", "018C98BB-C886-44B1-8667-DA304872B452");
                json.put("PeripheralName", "HardwickKitchenRelayOne");
                json.put("HouseName", "Hardwick");

                if(relaysList.get(0).getStatus()){
                    json.put("PeripheralValue", 1);
                }
                else {
                    json.put("PeripheralValue", 0);
                }
            } catch(JSONException e){
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(Void... arg0){
            HttpHandler hh = new HttpHandler();

            String resp = hh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/dev/relay/setrelaystatus", json);

            return null;
        }

        @Override
        protected void onPostExecute(Void result){

        }
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

/*
    public static ArrayList<Relay> fromJson(JSONArray jsonObjects) {
        ArrayList<Relay> relays = new ArrayList<Relay>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                relays.add(new Relay(jsonObjects.getJSONObject(i).getString("PeripheralName"),
                        jsonObjects.getJSONObject(i).getString("HouseName"),));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return relays;
    }
*/

    public Relay createRelay(){
        Relay relay = new Relay();
        relay.setHouseName("Hardwick");
        relay.setName("HardwickKitchenRelayOne");
        relay.switchOff();


        return relay;
    }
}
