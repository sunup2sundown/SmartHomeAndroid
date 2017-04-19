package edu.temple.m.smarthomedroid.Handlers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import edu.temple.m.smarthomedroid.HomeActivity;

/**
 * Created by M on 4/9/2017.
 */

public class TaskHandler {
    final String TAG = "TaskHandler";
    String sessionToken;
    ProgressDialog pDialog;
    boolean nameIsGood;
    Context mContext;
    String response;
    String houseName;
    String housePassword;
    HashingHandler hashingHandler = new HashingHandler();

    public void login(Context context, String username, String password){
        mContext = context;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("username", username);
            jsonObject.put("password", hashingHandler.hash_pass(password));
        } catch(JSONException e){
            e.printStackTrace();
        }

        new Login().execute(jsonObject);

    }

    public void register(Context context, String username, String password){
        mContext = context;
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("username", username);
            jsonObject.put("password", hashingHandler.hash_pass(password));
        } catch(JSONException e){
            e.printStackTrace();
        }

        //new register.execute();
    }

    public void createHouse(Context context, String name, String password, String session){
        mContext = context;
        JSONObject temp = new JSONObject();

        try {
            temp.put("houseName", houseName);
            temp.put("sessionToken", session);
        } catch(JSONException e){
            e.printStackTrace();
        }

        new CheckHouseNameAvailability().execute(temp);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("houseName", houseName);
            jsonObject.put("housePassword", hashingHandler.hash_pass(password));
            jsonObject.put("sessionToken", session);
        } catch(JSONException e){
            e.printStackTrace();
        }

        if(nameIsGood) {
            new CreateHouse().execute(jsonObject);
        } else{
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("The House Name you want is already taken. Try again")
                    .setTitle("Change House Name Failed...")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            dialog.cancel();
                        }
                    });
        }
    }

    public void joinHouse(Context context, String houseName, String housePassword, String sessionToken){
        mContext = context;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("houseName", houseName);
            jsonObject.put("housePassword", housePassword);
            jsonObject.put("sessionToken", sessionToken);
        } catch(JSONException e){
            e.printStackTrace();
        }

        //new login.execute();
    }

    public void leaveHouse(Context context, String sessionToken, String houseName){
        mContext = context;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("sessionToken", sessionToken);
            jsonObject.put("houseName", houseName);
        } catch(JSONException e){
            e.printStackTrace();
        }

        //new login.execute();
    }

    public void changeHouseName(Context context, String oldHouseName, String housePassword,
                                String newHouseName, String sessionToken){
        mContext = context;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("oldHouseName", oldHouseName);
            jsonObject.put("housePassword", housePassword);
            jsonObject.put("newHouseName", newHouseName);
            jsonObject.put("sessionToken", sessionToken);
        } catch(JSONException e){
            e.printStackTrace();
        }

        //new login.execute();
    }

    public void changeHousePassword(Context context, String houseName, String oldHousePassword,
                                    String newHousePassword, String sessionToken){
        mContext = context;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("houseName", houseName);
            jsonObject.put("oldHousePassword", oldHousePassword);
            jsonObject.put("newHousePassword", newHousePassword);
            jsonObject.put("sessionToken", sessionToken);
        } catch(JSONException e){
            e.printStackTrace();
        }

        //new login.execute();
    }

    public void changeUsername(Context context, String username, String sessionToken){
        mContext = context;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("username", username);
            jsonObject.put("sessionToken", sessionToken);
        } catch(JSONException e){
            e.printStackTrace();
        }
            new ChangeUsername().execute(jsonObject);
    }

    public void changeUserPassword(Context context, String password, String sessionToken){
        mContext = context;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("password", hashingHandler.hash_pass(password));
            jsonObject.put("sessionToken", sessionToken);
        } catch(JSONException e){
            e.printStackTrace();
        }

        new ChangePassword().execute(jsonObject);
    }

    public void checkHouseAvailability(Context context, String houseName, String sessionToken){
        mContext = context;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("houseName", houseName);
            jsonObject.put("sessionToken", sessionToken);
        } catch(JSONException e){
            e.printStackTrace();
        }

        new CheckHouseNameAvailability().execute(jsonObject);
    }

    public void removeHouse(Context context, String sessionToken, String houseName, String housePassword){
        mContext = context;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("sessionToken", sessionToken);
            jsonObject.put("houseName", houseName);
            jsonObject.put("housePassword", housePassword);
        } catch(JSONException e){
            e.printStackTrace();
        }

        //new login.execute();
    }

    public void addPeripheral(Context context, String houseName, String boardName,
                              String peripheralName, String peripheralModel,
                              String pinConnection,  String session){
        mContext = context;
        sessionToken = session;
        new AddPeripheral().execute(houseName, boardName, peripheralName, peripheralModel, pinConnection);
    }

    public void removePeripheral(Context context, String houseName, String peripheralName, String session){
        mContext = context;
        sessionToken = session;
        new RemovePeripheral().execute(houseName, peripheralName);
    }

    public void renamePeripheral(Context context, String houseName, String oldPeripheralName,
                                 String newPeripheralName, String session) {
        mContext = context;
        sessionToken = session;
        new RenameBoard().execute(houseName, oldPeripheralName, newPeripheralName);
    }

    public void getPeripheralTypes(String sessionToken){

    }

    public void peripheralModelByType(Context context, String peripheralTypeName, String sessionToken){
        mContext = context;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("PeripheralTypeName", peripheralTypeName);
            jsonObject.put("SessionToken", sessionToken);
        } catch(JSONException e){
            e.printStackTrace();
        }

        //new login.execute();
    }

    public void checkPeripheralAvailability(Context context, String sessionToken,
                                            String houseName, String peripheralName){
        mContext = context;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("sessionToken", sessionToken);
            jsonObject.put("houseName", houseName);
            jsonObject.put("peripheralName", peripheralName);
        } catch(JSONException e){
            e.printStackTrace();
        }

        //new login.execute();
    }

    public void addBoard(Context context, String houseName, String boardName,
                              String boardSerialNo, String session){
        mContext = context;
        sessionToken = session;
        new AddBoard().execute(houseName, boardName, boardSerialNo);
    }

    public void removeBoard(Context context, String houseName, String boardName, String session) {
        mContext = context;
        sessionToken = session;
        new RemoveBoard().execute(houseName, boardName);
    }

    public void renameBoard(Context context, String houseName, String oldBoardName, String newBoardName,
                            String session) {
        mContext = context;
        sessionToken = session;
        new RenameBoard().execute(houseName, oldBoardName, newBoardName);
    }

    public String retrieveBoards(Context context, String houseName, String session){
        mContext = context;
        sessionToken=session;
        new GetBoardsByHouse().execute(houseName);
        return response;
    }

    public void checkBoardNameAvailability(Context context, String houseName, String sessionToken, String boardName){
        mContext = context;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("houseName", houseName);
            jsonObject.put("SessionToken", sessionToken);
            jsonObject.put("BoardName", boardName);
        } catch(JSONException e){
            e.printStackTrace();
        }

        //new login.execute();
    }

    public String retrieveHouses(Context context, String session){
        mContext = context;
        sessionToken = session;
        new ListAllHouses().execute();
        return response;
    }

    public void availablePins(Context context,String houseName, String sessionToken,
                              String boardName, String peripheralTypeName){
        mContext = context;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("HouseName", houseName);
            jsonObject.put("SessionToken", sessionToken);
            jsonObject.put("BoardName", boardName);
            jsonObject.put("PeripheralTypeName", peripheralTypeName);
        } catch(JSONException e){
            e.printStackTrace();
        }

        //new login.execute();
    }

    /*
    ** Asynchronous Tasks -- HTTP POST Calls
     *
     */
    private class Login extends AsyncTask<JSONObject, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String response;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(JSONObject...args){

            jsonObject = args[0];

            JSONObject json = new HttpHandler2().makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/login", args[0]);
            if(json!=null) {
                Log.d("", json.toString());
                try {
                    response = json.getString("SessionToken");
                } catch(JSONException e){
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

    private class Register extends AsyncTask<JSONObject, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String response;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(JSONObject...args){

            JSONObject json = new HttpHandler2().makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/register", args[0]);


            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
        }
    }



    private class ChangeUsername extends AsyncTask<JSONObject, Void, Void>{
        String response;

        @Override
        protected void onPreExecute(){
            //Show progress dialog
            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(JSONObject...args){
            HttpHandler sh = new HttpHandler();
            String username = "";
            try {
                username = args[0].get("username").toString();
            } catch(JSONException e){
                e.printStackTrace();
            }


            String resp = new HttpHandler().makeGetCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/checkusername/" + username, "GET");

            if(resp.equals("1")){
                response = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/changeusername", args[0]);
            } else {

            }

            Log.d("TaskHandler", "Change Username Response: " + response);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            //Dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    private class ChangePassword extends AsyncTask<JSONObject, Void, Void>{
        @Override
        protected void onPreExecute(){
            //Show progress dialog
            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(JSONObject...arg0){

            //Make a request to url and get response
            response = new HttpHandler().makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/changepassword", arg0[0]);

            Log.d("TaskHandler", "Change Password Response: " + response);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            //Dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    private class ChangeHouseName extends AsyncTask<JSONObject, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String response;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(JSONObject...args){

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
        }
    }

    private class ChangeHousePassword extends AsyncTask<JSONObject, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String response;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(JSONObject...args){

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
        }
    }

    private class JoinHouse extends AsyncTask<JSONObject, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String response;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(JSONObject...args){

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
        }
    }

    private class RemoveHouse extends AsyncTask<JSONObject, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String response;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(JSONObject...args){

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
        }
    }

    private class CheckHouseNameAvailability extends AsyncTask<JSONObject, Void, Void> {
        JSONObject jsonObject = new JSONObject();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(JSONObject...args){

            response = new HttpHandler().makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/house/check-house-availability", args[0]);

            Log.d(TAG, "Check House Name: " + response);

            if(response.equals("1")){
                nameIsGood = true;
            } else {
                nameIsGood = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
        }
    }

    // for API call 8. Create House
    private class CreateHouse extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //Show progress dialog
            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(JSONObject... args) {

            //Make a request to url and get response
            response = new HttpHandler().makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/house/createhouse", args[0]);

            Log.d("TaskHandler", "Create house response: " + response);

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            //Dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
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
            Log.d(TAG, "List All Houses Response: " + response);
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
            Log.d(TAG, "Remove Peripheral Response: " + response);
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

            Log.d(TAG, "Create Peripheral Response: " + response);

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

                Log.d(TAG, "Rename Peripheral Response: " + response);

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

                Log.d(TAG, "Get Boards by House Response: " + response);

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

                Log.d(TAG, "Create Board Response: " + response);
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

                Log.d(TAG, "Remove Board Response: " + response);

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

                Log.d(TAG, "Rename Board Response: " + response);

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
