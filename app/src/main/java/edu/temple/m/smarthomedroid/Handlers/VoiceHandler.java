package edu.temple.m.smarthomedroid.Handlers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by M on 4/22/2017.
 */

/**
 * VoiceHandler class receives a List of Strings and returns a command-string based on the matches
 */
public class VoiceHandler {
    final String TAG = "VoiceHandler";
    JSONArray houses;
    JSONArray relays;
    //List<String> list1 = new ArrayList<String>();
    ArrayList<String> matches;
    Context mContext;
    String sessionToken;
    String result;
    ArrayList<String> houseList;
    ArrayList<String> relayList;
    ArrayList<String> commandList;

    public VoiceHandler(Context context, String sessionToken, ArrayList<String> matches){
        mContext = context;
        this.matches = matches;
        this.sessionToken = sessionToken;
        houseList = new ArrayList<String>();
        getHouseList();
        commandList = new ArrayList<String>();
        commandList.add("off");
        commandList.add("on");
    }

    private ArrayList<String> parseCommand(ArrayList<String> commandString){
        String command = commandString.get(0);
        ArrayList<String> strings = new ArrayList<String>(Arrays.asList(command.split(" ")));

        return strings;
    }


    public String getResult(){
        String command = "";
        ArrayList<String> temp = parseCommand(matches);
        int pos;
        StringBuilder relay = new StringBuilder();

        if(temp.contains("go") && (temp.contains("relays") || temp.contains("relay"))){
            command = "RelayFragment";
        } else if(temp.contains("go") && (temp.contains("settings") || temp.contains("setting"))){
            command = "SettingsFragment";
        } else if(temp.contains("go") && (temp.contains("dashboard") || temp.contains("dashboards"))){
            command = "DashboardFragment";
        } else if(temp.contains("go") && (temp.contains("sensor") || temp.contains("sensors"))){
            command = "SensorFragment";
        } else if(temp.contains("go") && (temp.contains("configuration") || temp.contains("configurations"))){
            command = "ConfigFragment";
        } else if(temp.contains("logout") || (temp.contains("log") && temp.contains("out"))){
            command = "Logout";
        } else{
            houseList.retainAll(temp);
            commandList.retainAll(temp);
            if(houseList.size() > 0){
                getRelayList(houseList.get(0));
                Log.d(TAG, houseList.get(0));
                if(commandList.size() > 0){
                    Log.d(TAG, commandList.get(0));
                    pos = temp.indexOf(commandList.get(0)) + 1;
                    if(pos != temp.size()) {
                        for (; pos < temp.size(); pos++) {
                            relay.append(temp.get(pos));
                            Log.d(TAG, temp.get(pos));
                            Log.d(TAG, relay.toString());
                        }
                        Log.d(TAG, relayList.get(0));
                        if(relayList.size() > 0){
                            if(relayList.contains(relay)){
                                if(commandList.get(0).contentEquals("off")){
                                    Log.d(TAG, "Turning off " + houseList.get(0) + "'s " + relay.toString());
                                    new TaskHandler().setRelayStatus(mContext, sessionToken, relay.toString(), houseList.get(0), "0");
                                } else {
                                    Log.d(TAG, "Turning on " + houseList.get(0) + "'s " + relay.toString());
                                    new TaskHandler().setRelayStatus(mContext, sessionToken, relay.toString(), houseList.get(0), "1");
                                }
                            }
                        }
                    } else {
                        //Not the right command
                    }
                } else {

                }
            }
        }

        return command;
    }

    private void getHouseList(){
        new GetHouses().execute();

        while(houses == null){
            //Nothing
        }

        if (houses!=null) {
            int i = 0;
            for (; i < houses.length(); i++) {
                try {
                    houseList.add(houses.getJSONObject(i).getString("HouseName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getRelayList(String houseName){
        new GetRelayByHouse().execute(houseName);

        while(relays == null){
            //Nothing
        }

        if (relays!=null) {
            int i = 0;
            for (; i < relays.length(); i++) {
                try {
                    relayList.add(relays.getJSONObject(i).getString("PeripheralName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class GetHouses extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String session = sessionToken;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("sessionToken", session);
            } catch(JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            JSONArray ii=null;
            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/house/listhouses", jsonObject);

            if(resp != null){
                try {
                    ii = new JSONArray(resp);
                    houses=ii.getJSONArray(0);
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
    private class GetRelayByHouse extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String...args){
            JSONArray responseArray = null;
            JSONObject json = new JSONObject();
            try {
                json.put("HouseName", args[0]);
                json.put("SessionToken", sessionToken);
            } catch(JSONException e) {
                e.printStackTrace();
            }

            String resp = new HttpHandler().makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/dev/relay/getrelayvaluesbyhouseid", json);

            if(resp != null){
                try {
                    responseArray = new JSONArray(resp);
                    relays=responseArray.getJSONArray(0);
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
