package edu.temple.m.smarthomedroid.Handlers;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by M on 4/9/2017.
 */

public class TaskHandler {
    String sessionToken = "";
    String username = "", userPassword = "";
    String houseName = "", housePassword = "";
    String tag = "";

    public void createHouse(String context, String name, String password, String session){
        tag = context;

        //TODO: Check House name availability
        houseName = name;
        housePassword = password;
        sessionToken = session;

        new CreateHouse().execute();
    }

    /**
     * Asynchronous Tasks -- HTTP GET Calls
     */


    /*
    ** Asynchronous Tasks -- HTTP POST Calls
     *
     */
    private class CreateHouse extends AsyncTask<Void, Void, Void> {
        JSONObject json = new JSONObject();
        String response;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            if(houseName != null && sessionToken != null) {
                try {
                    json.put("houseName", houseName);
                    json.put("housePassword", housePassword);
                    json.put("sessionToken", sessionToken);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            response = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/dev/house/createhouse", json);

            if(response != null){
                Log.d(tag, "Create House Response: " + response);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            houseName = "";
            housePassword = "";
        }
    }
}
