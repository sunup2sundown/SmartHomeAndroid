package edu.temple.m.smarthomedroid.Objects;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler2;

import static java.lang.Thread.sleep;

/**
 * Created by Jhang Myong Ja on 3/25/2017.
 *
 * Edited: 3/27/2017 10:45am by Matt White
 */

public class Relay {
    String id;
    String name;
    String houseName;
    String session;
    int currentValue;
    final String TAG = "Relay Object";
    //JSONObject on,off;
    public Relay(){}

    public Relay(String sess,String house, String name, int status){
        this.houseName = house;
        this.currentValue = status;
        this.name = name;
        this.session= sess;
    }

    public void switchOn() throws JSONException {
        new son().execute();
        try {
            sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentValue = 1;
    }
    public void switchOff() throws JSONException {
        new soff().execute();
        try {
            sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentValue = 0;
    }
    public boolean getStatus(){
        if(currentValue == 1){
            return true;
        }
        else{
            return false;
        }
    }

    // Peripheral interface functions
    public void setName(String name){
        this.name = name;
    }

    public void setHouseName(String houseName){
        this.houseName = houseName;
    }

    public String getName(){
        return this.name;
    }

    public String getHouseName(){
        return this.houseName;
    }

    /*/public JSONObject getJson(String val) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("SessionToken",this.session)
                .put("PeripheralName",this.name)
                .put("HouseName",this.houseName)
                .put("PeripheralValue",val);
        Log.d(TAG,obj.toString());
        return obj;
    }*/
    private class son extends AsyncTask<Void, Void, Void> {
        JSONObject on = new JSONObject();
        String res;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            try {
                on.put("SessionToken",session)
                        .put("PeripheralName",name)
                        .put("HouseName",houseName)
                        .put("PeripheralValue","1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG,on.toString());
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            Log.d(TAG, "Json: " + on.toString());
            res= sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/dev/relay/setrelaystatus",on);
            Log.d(TAG, "Retrieve Relay Status Response: " + res);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
        }
    }

    private class soff extends AsyncTask<Void, Void, Void> {
        JSONObject off = new JSONObject();
        String res;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            try {
                off.put("SessionToken",session)
                        .put("PeripheralName",name)
                        .put("HouseName",houseName)
                        .put("PeripheralValue","0");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG,off.toString());
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            Log.d(TAG, "Json: " + off.toString());
            res= sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/dev/relay/setrelaystatus",off);
            Log.d(TAG, "Retrieve Relay Status Response: " + res);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
        }
    }

}
