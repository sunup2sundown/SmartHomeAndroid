package edu.temple.m.smarthomedroid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import edu.temple.m.smarthomedroid.Adapters.HouseAdapter;
import edu.temple.m.smarthomedroid.Dialogs.ChangeHouseNameDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeHousePasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangePasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeUserPasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeUsernameDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.NewHouseDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.SwitchHouseDialogFragment;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Objects.House;

/**
 * Created by M on 3/16/2017.
 */

public class UserSettingsFragment extends Fragment {

    FragmentManager fm;

    private final String TAG = "SettingsFragment";

    private String usern, userPassword, newUserPassword;
    private String houseName, newHouseName, housePassword, newHousePassword;
    private String  sessionToken;
    private ArrayList<House> houseList;
    private String response;

    //String userid;
    public UserSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_settings,container,false);
        TextView username = (TextView) v.findViewById(R.id.text_username);
        usern = "Tom Brady";
        sessionToken = "51FAA52D-CD90-461A-8735-D866DB3BDFF3";
        fm = getActivity().getSupportFragmentManager();

        username.setText(usern);

        ((Button)v.findViewById(R.id.button_changepw)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeUserPasswordDialogFragment f = new ChangeUserPasswordDialogFragment();
                f.show(fm, null);
            }
        });
        ((Button)v.findViewById(R.id.button_newhouse)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewHouseDialogFragment f = NewHouseDialogFragment.newInstance();
                f.show(fm, null);
            }
        });
        ((Button)v.findViewById(R.id.button_switchhouse)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchHouseDialogFragment f = SwitchHouseDialogFragment.newInstance();
                f.show(fm, null);
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        //Construct data source
        houseList = new ArrayList<House>();
        // new RetrieveHouses().execute();
        // Populate list from API call
        populateList(response);

        //Create and set custom adapter for relay list
        HouseAdapter adapter = new HouseAdapter(getActivity(), houseList);

        ExpandableListView lv = (ExpandableListView)getView().findViewById(R.id.listview_houses);
        lv.setAdapter(adapter);
    }

    private class RetrieveHouses extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("sessionToken", sessionToken);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/house/listhouses", jsonObject);

            if(resp != null){
                Log.d(TAG, "Retrieve Houses: " + resp);
            }
            response = resp;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    private void populateList(String response){
        /*
        JSONObject houseJson = null;
        try {
            houseJson = new JSONObject(response);
        } catch (JSONException e){
            Log.e(TAG, "JSONException: " + e.getMessage());
        }
        Iterator<String> keys = houseJson.keys();
        while (keys.hasNext()){
            String key = keys.next();
            // get house data from json into houseList
        }
        */
    }
}

