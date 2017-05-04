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
    JSONArray peripherals;
    //List<String> list1 = new ArrayList<String>();
    ArrayList<String> matches;
    Context mContext;
    String sessionToken;
    String result;
    String currentHouse;
    ArrayList<String> houseList;
    ArrayList<String> peripheralList;
    ArrayList<String> commandList;
    ArrayList<String> cameraList;
    ArrayList<String> sensorList;

    public VoiceHandler(Context context, String currentHouse, String sessionToken, ArrayList<String> matches){
        mContext = context;
        this.matches = matches;
        this.sessionToken = sessionToken;
        houseList = new ArrayList<String>();
        getHouseList();
        commandList = new ArrayList<String>();
        commandList.add("off");
        commandList.add("on");
        peripheralList = new ArrayList<String>();
        this.currentHouse = currentHouse;
    }

    private ArrayList<String> parseCommand(ArrayList<String> commandString){
        String command = commandString.get(0);
        ArrayList<String> strings = new ArrayList<String>(Arrays.asList(command.split(" ")));

        return strings;
    }


    public String getResult() {
        ArrayList<String> peripheralName = new ArrayList<String>();
        String command = "";
        ArrayList<String> temp = parseCommand(matches);
        int pos;
        StringBuilder peripheral = new StringBuilder();

        if (temp.contains("go")) {
            if((temp.contains("relays") || temp.contains("relay"))){
                command = "RelayFragment";
            }else if ((temp.contains("account") || temp.contains("accounts"))) {
                command = "SettingsFragment";
            }else if ((temp.contains("dashboard") || temp.contains("dashboards"))) {
                command = "DashboardFragment";
            }else if ((temp.contains("sensor") || temp.contains("sensors"))) {
                command = "SensorFragment";
            }else if ((temp.contains("system") || temp.contains("systems"))) {
                command = "SystemSettingsFragment";
            } else if ((temp.contains("camera") || temp.contains("cameras"))) {
                command = "CameraFragment";
            }else if (temp.contains("logout") || (temp.contains("log") && temp.contains("out"))) {
                command = "Logout";
            }
        }else if (temp.contains("show")) {
            if(temp.contains("camera") || temp.contains("cameras")){
                getCameraList(currentHouse);
                pos = 3;
                if(pos != temp.size()) {
                    for (; pos < temp.size(); pos++) {
                        peripheral.append(temp.get(pos));
                        Log.d(TAG, temp.get(pos));
                        Log.d(TAG, peripheral.toString());
                    }
                    Log.d(TAG, peripheralList.get(0));
                    peripheralName.add(peripheral.toString());
                    peripheralList.retainAll(peripheralName);
                    if(peripheralList.size() > 0){
                        Log.d(TAG, "Camera Command Executed");
                    }
                } else {
                    //Not the right command
                }
            } else if(temp.contains("sensor") || temp.contains("sensors")){
                getSensorList(currentHouse);
                pos = 3;
                if(pos != temp.size()) {
                    for (; pos < temp.size(); pos++) {
                        peripheral.append(temp.get(pos));
                        Log.d(TAG, temp.get(pos));
                        Log.d(TAG, peripheral.toString());
                    }
                    Log.d(TAG, peripheralList.get(0));
                    peripheralName.add(peripheral.toString());
                    peripheralList.retainAll(peripheralName);
                    if(peripheralList.size() > 0){
                        Log.d(TAG, "Sensor Command Executed");
                    }
                } else {
                    //Not the right command
                }
            }
        } else{
            houseList.retainAll(temp);
            commandList.retainAll(temp);
            if(houseList.size() > 0){
                Log.d(TAG, houseList.get(0));
                getRelayList(houseList.get(0));
                if(commandList.size() > 0){
                    Log.d(TAG, commandList.get(0));
                    pos = temp.indexOf(commandList.get(0)) + 1;
                    if(pos != temp.size()) {
                        for (; pos < temp.size(); pos++) {
                            peripheral.append(temp.get(pos));
                            Log.d(TAG, temp.get(pos));
                            Log.d(TAG, peripheral.toString());
                        }
                        Log.d(TAG, peripheralList.get(0));
                        peripheralName.add(peripheral.toString());
                        peripheralList.retainAll(peripheralName);
                        if(peripheralList.size() > 0){
                            if(commandList.get(0).contentEquals("off")){
                                Log.d(TAG, "Turning off " + houseList.get(0) + "'s " + peripheralList.get(0));
                                new TaskHandler().setRelayStatus(mContext, sessionToken, peripheralList.get(0), houseList.get(0), "0");
                            } else {
                                Log.d(TAG, "Turning on " + houseList.get(0) + "'s " + peripheralList.get(0));
                                new TaskHandler().setRelayStatus(mContext, sessionToken, peripheralList.get(0), houseList.get(0), "1");
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

    private void getCameraList(String houseName){
        JSONArray responseArray = new TaskHandler().getCurrentCameraByHouse(mContext, sessionToken, houseName);

        while(responseArray == null){

        }
        try {
            peripherals = responseArray.getJSONArray(0);
        } catch(JSONException e){
            e.printStackTrace();
        }

        if (peripherals!=null) {
            int i = 0;
            for (; i < peripherals.length(); i++) {
                try {
                    peripheralList.add(peripherals.getJSONObject(i).getString("PeripheralName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getSensorList(String houseName){
        new GetSensorsByHouse().execute();

        while(peripherals == null){
            //Nothing
        }

        if (peripherals!=null) {
            int i = 0;
            for (; i < peripherals.length(); i++) {
                try {
                    peripheralList.add(peripherals.getJSONObject(i).getString("PeripheralName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getRelayList(String houseName){
        new GetRelayByHouse().execute(houseName);

        while(peripherals == null){
            //Nothing
        }

        if (peripherals!=null) {
            int i = 0;
            for (; i < peripherals.length(); i++) {
                try {
                    peripheralList.add(peripherals.getJSONObject(i).getString("PeripheralName"));
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
                    peripherals=responseArray.getJSONArray(0);
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

    private class GetSensorsByHouse extends AsyncTask<Void, Void, Void> {
        JSONObject json = new JSONObject();
        String house = currentHouse;
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
            JSONArray responseArray = new JSONArray();
            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/sensor/getsensorvaluesbyhouse", json);

            if(resp != null){
                try {
                    responseArray = new JSONArray(resp);
                    peripherals=responseArray.getJSONArray(0);
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
