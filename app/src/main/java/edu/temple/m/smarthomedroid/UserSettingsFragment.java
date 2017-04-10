package edu.temple.m.smarthomedroid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import edu.temple.m.smarthomedroid.Adapters.HouseAdapter;
import edu.temple.m.smarthomedroid.Dialogs.ChangePasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeUserPasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeUsernameDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.NewHouseDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.SwitchHouseDialogFragment;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler;

/**
 * Created by M on 3/16/2017.
 */

public class UserSettingsFragment extends Fragment implements ChangeUsernameDialogFragment.ChangeUsernameDialogListener, ChangeUserPasswordDialogFragment.ChangeUserPasswordDialogListener{

    FragmentManager fm;

    private final String TAG = "SettingsFragment";

    private String usern, userPassword, newUserPassword;
    private String houseName, newHouseName, housePassword, newHousePassword;
    private String  sessionToken;

    FragmentManager dialogManager;

    //String userid;
    public UserSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_settings,container,false);
        TextView username = (TextView) v.findViewById(R.id.tv_username);
        //userid = getArguments().getString("userid");
        //username.setText(userid);
        usern = "Tom Brady";
        sessionToken = "51FAA52D-CD90-461A-8735-D866DB3BDFF3";

        dialogManager = getActivity().getSupportFragmentManager();

        ((Button)v.findViewById(R.id.btn_changepw)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeUserPasswordDialogFragment f = new ChangeUserPasswordDialogFragment();
                showChangeUserPasswordDialog(dialogManager);
            }
        });
        ((Button)v.findViewById(R.id.btn_newhouse)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewHouseDialogFragment f = new NewHouseDialogFragment();
                f.show(fm, null);
            }
        });
        ((Button)v.findViewById(R.id.btn_switchhouse)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchHouseDialogFragment f = SwitchHouseDialogFragment.newInstance();
                f.show(fm, null);
            }
        });

        return v;
    }

    /* Dialog Fragment Listeners Implementations
    *
     */
    //Change Username Dialog Listener Implementation
    public void showChangeUsernameDialog(FragmentManager fm){
        //create instance of dialog fragment and show it
        DialogFragment changeUsernameDialog = new ChangeUsernameDialogFragment();
        changeUsernameDialog.show(fm, "ChangeUsernameDialogFragment");
    }

    @Override
    public void onChangeUsernameDialogPositiveClick(DialogFragment dialog){

    }

    @Override
    public void onChangeUsernameDialogNegativeClick(DialogFragment dialog){
        dialog.getDialog().cancel();
    }

    //Change Username Dialog Listener Implementation
    public void showChangeUserPasswordDialog(FragmentManager fm){
        //create instance of dialog fragment and show it
        DialogFragment changeUserPasswordDialog = new ChangeUserPasswordDialogFragment();
        changeUserPasswordDialog.show(fm, "ChangeUserPasswordDialogFragment");
    }

    @Override
    public void onChangeUserPasswordDialogPositiveClick(DialogFragment dialog){

    }

    @Override
    public void onChangeUserPasswordDialogNegativeClick(DialogFragment dialog){
        //dialog.getDialog().cancel();
    }

    /**
     * HTTP Calls --Async Task
     */
    private class ChangeUserName extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String user = usern;
        String session = sessionToken;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("password", user);
                jsonObject.put("sessionToken", sessionToken);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/changeusername", jsonObject);

            if(resp != null){
                Log.d(TAG, "Change Username: " + resp);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    private class ChangeUserPassword extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String pw = userPassword;
        String session = sessionToken;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("password", pw);
                jsonObject.put("sessionToken", sessionToken);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/changepassword", jsonObject);

            if(resp != null){
                Log.d(TAG, "Change Password: " + resp);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    private class ChangeHouseName extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String name = houseName;
        String password = housePassword;
        String newName = newHouseName;
        String session = sessionToken;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("oldHouseName", name);
                jsonObject.put("housePassword", password);
                jsonObject.put("newHouseName", newName);
                jsonObject.put("sessionToken", sessionToken);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/house/changehousename", jsonObject);

            if(resp != null){
                Log.d(TAG, "Change House Name: " + resp);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    private class ChangeHousePassword extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String name = houseName;
        String password = housePassword;
        String newPassword = newHousePassword;
        String session = sessionToken;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("houseName", name);
                jsonObject.put("oldHousePassword", password);
                jsonObject.put("newHousePassword", newPassword);
                jsonObject.put("sessionToken", sessionToken);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/house/changehousepassword", jsonObject);

            if(resp != null){
                Log.d(TAG, "Change House Password: " + resp);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    private class JoinHouse extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String name = houseName;
        String password = housePassword;
        String session = sessionToken;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("houseName", name);
                jsonObject.put("housePassword", password);
                jsonObject.put("sessionToken", sessionToken);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/house/joinhouse", jsonObject);

            if(resp != null){
                Log.d(TAG, "Join House: " + resp);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }
}

