package edu.temple.m.smarthomedroid.Objects;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import edu.temple.m.smarthomedroid.Handlers.HttpHandler;

import static java.lang.Thread.sleep;

/**
 * Created by Jhang Myong Ja on 3/25/2017.
 *
 * Edited: 3/27/2017 10:45am by Matt White
 */

public class Relay2 {
    String id;
    String name;
    String houseName;
    String session;
    int currentValue;
    final String TAG = "Relay2 Object";

    //JSONObject on,off;
    public Relay2() {
    }

    public Relay2(String sess, String house, String name, int status) {
        this.houseName = house;
        this.currentValue = status;
        this.name = name;
        this.session = sess;
    }

    public boolean getStatus() {
        if (currentValue == 1) {
            return true;
        } else {
            return false;
        }
    }

    // Peripheral interface functions
    public void setName(String name) {
        this.name = name;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getName() {
        return this.name;
    }

    public String getHouseName() {
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
}