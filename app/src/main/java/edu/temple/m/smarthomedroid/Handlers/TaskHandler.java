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
    String response;

    public void createHouse(String context, String name, String password, String session){
        tag = context;

        //TODO: Check House name availability
        houseName = name;
        housePassword = password;
        sessionToken = session;

        new CreateHouse().execute();
    }

    public void addPeripheral(String context, String houseName, String boardName,
                              String peripheralName, String peripheralModel,
                              String pinConnection,  String session){
        tag = context;
        sessionToken = session;
        new AddPeripheral().execute(houseName, boardName, peripheralName, peripheralModel, pinConnection);
    }

    public void removePeripheral(String context, String houseName, String peripheralName, String session){
        tag = context;
        sessionToken = session;
        new RemovePeripheral().execute(houseName, peripheralName);
    }

    public void renamePeripheral(String context, String houseName, String oldPeripheralName,
                                 String newPeripheralName, String session) {
        tag = context;
        sessionToken = session;
        new RenameBoard().execute(houseName, oldPeripheralName, newPeripheralName);
    }

    public void addBoard(String context, String houseName, String boardName,
                              String boardSerialNo, String session){
        tag = context;
        sessionToken = session;
        new AddBoard().execute(houseName, boardName, boardSerialNo);
    }

    public void removeBoard(String context, String houseName, String boardName, String session) {
        tag = context;
        sessionToken = session;
        new RemoveBoard().execute(houseName, boardName);
    }

    public void renameBoard(String context, String houseName, String oldBoardName, String newBoardName,
                            String session) {
        tag = context;
        sessionToken = session;
        new RenameBoard().execute(houseName, oldBoardName, newBoardName);
    }

    public String retrieveBoards(String context, String houseName, String session){
        tag = context;
        sessionToken=session;
        new GetBoardsByHouse().execute(houseName);
        return response;
    }

    public String retrieveHouses(String context, String session){
        tag = context;
        sessionToken = session;
        new ListAllHouses().execute();
        return response;
    }

    /**
     * Asynchronous Tasks -- HTTP GET Calls
     */


    /*
    ** Asynchronous Tasks -- HTTP POST Calls
     *
     */

    // for API call 8. Create House
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
            response = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/house/createhouse", json);

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

    // for API call 14. List All Houses
    private class ListAllHouses extends AsyncTask<Void, Void, Void>{
        JSONObject json = new JSONObject();
        String response;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... args) {
            HttpHandler sh = new HttpHandler();
            if(houseName != null && sessionToken != null) {
                try {
                    json.put("sessionToken", sessionToken);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Make a request to url and get response
            response = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/house/listallhouses", json);
            TaskHandler.this.response = response;

            if(response != null){
                Log.d(tag, "List All Houses Response: " + response);
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

    // for API call 18. Remove Peripheral
    private class RemovePeripheral extends AsyncTask<String, Void, Void> {
        JSONObject json = new JSONObject();
        String response;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... args) {
            HttpHandler sh = new HttpHandler();
            if(houseName != null && sessionToken != null) {
                try {
                    json.put("sessionToken", sessionToken);
                    json.put("houseName", args[0]);
                    json.put("peripheralName", args[1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Make a request to url and get response
            response = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/peripheral/removeperipheral", json);
            if(response != null){
                Log.d(tag, "Remove Peripheral Response: " + response);
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

    // for API call 20. Create Peripheral
    private class AddPeripheral extends AsyncTask<String, Void, Void> {
        JSONObject json = new JSONObject();
        String response;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... args) {
            HttpHandler sh = new HttpHandler();
            if(houseName != null && sessionToken != null) {
                try {
                    json.put("sessionToken", sessionToken);
                    json.put("houseName", args[0]);
                    json.put("boardName", args[1]);
                    json.put("peripheralName", args[2]);
                    json.put("peripheralModelName", args[3]);
                    json.put("pinConnection", args[4]);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Make a request to url and get response
            response = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/peripheral/createperipheral", json);

            if(response != null){
                Log.d(tag, "Create Peripheral Response: " + response);
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

    // for API call 22. Change Peripheral Name
    private class RenamePeripheral extends AsyncTask<String, Void, Void> {
        JSONObject json = new JSONObject();
        String response;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... args) {
            HttpHandler sh = new HttpHandler();
            if(houseName != null && sessionToken != null) {
                try {
                    json.put("sessionToken", sessionToken);
                    json.put("houseName", args[0]);
                    json.put("oldPeripheralName", args[1]);
                    json.put("newPeripheralName", args[2]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Make a request to url and get response
            response = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/peripheral/changeperipheralname", json);

            if(response != null){
                Log.d(tag, "Rename Peripheral Response: " + response);
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

    // for API call 23. Get Boards by House
    private class GetBoardsByHouse extends AsyncTask<String, Void, Void> {
        JSONObject json = new JSONObject();
        String response;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... args) {
            HttpHandler sh = new HttpHandler();
            if(houseName != null && sessionToken != null) {
                try {
                    json.put("SessionToken", sessionToken);
                    json.put("HouseName", args[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Make a request to url and get response
            response = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/house/getboardsbyhouse", json);
            TaskHandler.this.response = response;

            if(response != null){
                Log.d(tag, "Get Boards by House Response: " + response);
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

    // for API call 27. Create Board
    private class AddBoard extends AsyncTask<String, Void, Void> {
        JSONObject json = new JSONObject();
        String response;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... args) {
            HttpHandler sh = new HttpHandler();
            if(houseName != null && sessionToken != null) {
                try {
                    json.put("SessionToken", sessionToken);
                    json.put("HouseName", args[0]);
                    json.put("BoardName", args[1]);
                    json.put("BoardSerialNumber", args[2]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Make a request to url and get response
            response = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/board/createboard", json);

            if(response != null){
                Log.d(tag, "Create Board Response: " + response);
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

    // for API call 28. Remove Board
    private class RemoveBoard extends AsyncTask<String, Void, Void> {
        JSONObject json = new JSONObject();
        String response;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... args) {
            HttpHandler sh = new HttpHandler();
            if(houseName != null && sessionToken != null) {
                try {
                    json.put("SessionToken", sessionToken);
                    json.put("HouseName", args[0]);
                    json.put("BoardName", args[1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Make a request to url and get response
            response = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/board/removeboard", json);

            if(response != null){
                Log.d(tag, "Remove Board Response: " + response);
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

    // for API call 29. Change Board Name
    private class RenameBoard extends AsyncTask<String, Void, Void> {
        JSONObject json = new JSONObject();
        String response;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... args) {
            HttpHandler sh = new HttpHandler();
            if(houseName != null && sessionToken != null) {
                try {
                    json.put("SessionToken", sessionToken);
                    json.put("HouseName", args[0]);
                    json.put("OldBoardName", args[1]);
                    json.put("NewBoardName", args[2]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Make a request to url and get response
            response = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/board/changeboardname", json);

            if(response != null){
                Log.d(tag, "Rename Board Response: " + response);
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
