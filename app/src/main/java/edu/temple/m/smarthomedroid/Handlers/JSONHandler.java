package edu.temple.m.smarthomedroid.Handlers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by M on 4/11/2017.
 */

public class JSONHandler {
    public Object obj = null;
    public boolean isJsonArray = false;

    JSONHandler(Object obj , boolean isJsonArray){
        this.obj = obj;
        this.isJsonArray = isJsonArray;
    }

    public static JSONHandler fromStringToJSON(String jsonString){
        boolean isJsonArray = false;
        Object obj = null;

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            Log.d("JSON", jsonArray.toString());
            obj = jsonArray;
            isJsonArray = true;
        }
        catch (Throwable t) {
            Log.e("JSON", "Malformed JSON: \"" + jsonString + "\"");
        }

        if (obj == null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                Log.d("JSON", jsonObject.toString());
                obj = jsonObject;
                isJsonArray = false;
            } catch (Throwable t) {
                Log.e("JSON", "Malformed JSON: \"" + jsonString + "\"");
            }
        }

        return new JSONHandler(obj, isJsonArray);
    }
}

