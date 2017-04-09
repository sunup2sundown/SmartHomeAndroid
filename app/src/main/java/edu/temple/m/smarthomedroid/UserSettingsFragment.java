package edu.temple.m.smarthomedroid;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import edu.temple.m.smarthomedroid.Adapters.HouseAdapter;
import edu.temple.m.smarthomedroid.Dialogs.ChangePasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.NewHouseDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.SwitchHouseDialogFragment;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler;

/**
 * Created by M on 3/16/2017.
 */

public class UserSettingsFragment extends Fragment{

    FragmentManager fm;
    OnFragment4AttachedListener activity;

    private final String TAG = "SettingsFragment";

    private String usern, sessionToken;

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

        fm = getFragmentManager();

        ((Button)v.findViewById(R.id.btn_changepw)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordDialogFragment f = new ChangePasswordDialogFragment();
                f.show(fm, null);
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

    public void onAttach(Context actv){
        super.onAttach(actv);
        //activity = (OnFragment4AttachedListener)actv;
        //userid = activity.getUsername();
    }

    public interface OnFragment4AttachedListener {
        String getUsername();
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
                jsonObject.put("username", user);
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
        //String pw = password;
        String session = sessionToken;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
            //    jsonObject.put("username", pw);
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

    private class ChangeHouseName extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String user = usern;
        String session = sessionToken;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("username", user);
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

    private class ChangeHousePassword extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String user = usern;
        String session = sessionToken;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("username", user);
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

    private class JoinHouse extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String user = usern;
        String session = sessionToken;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("username", user);
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
}

