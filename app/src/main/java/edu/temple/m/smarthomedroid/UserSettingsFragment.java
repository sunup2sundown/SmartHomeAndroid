package edu.temple.m.smarthomedroid;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.temple.m.smarthomedroid.Adapters.HouseAdapter;
import edu.temple.m.smarthomedroid.Dialogs.ChangeHouseNameDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeHousePasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangePasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeUsernameDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.JoinHouseDialogFragment;
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
    private String userID;
    private Bundle bundle;

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

        bundle = new Bundle();
        //Receive argument bundle from Home Activity
        userID = getArguments().getString("Username");
        sessionToken = getArguments().getString("SessionToken");

        bundle.putString("SessionToken", sessionToken);

        TextView username = (TextView) v.findViewById(R.id.text_username);
        fm = getActivity().getSupportFragmentManager();

        username.setText(userID);

        ((Button)v.findViewById(R.id.button_changeusername)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ChangeUsernameDialogFragment f = new ChangeUsernameDialogFragment();
                f.setArguments(bundle);
                f.show(fm, null);
            }
        });

        ((Button)v.findViewById(R.id.button_changepw)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordDialogFragment f = new ChangePasswordDialogFragment();
                f.setArguments(bundle);
                f.show(fm, null);
            }
        });
        ((Button)v.findViewById(R.id.button_newhouse)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewHouseDialogFragment f =  new NewHouseDialogFragment();
                f.setArguments(bundle);
                f.show(fm, null);
            }
        });
        ((Button)v.findViewById(R.id.button_switchhouse)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchHouseDialogFragment f = new SwitchHouseDialogFragment();
                f.setArguments(bundle);
                f.show(fm, null);
            }
        });
        ((Button)v.findViewById(R.id.button_joinhouse)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinHouseDialogFragment f = new JoinHouseDialogFragment();
                f.setArguments(bundle);
                f.show(fm, null);
            }
        });
        ((Button)v.findViewById(R.id.button_changehousename)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeHouseNameDialogFragment f = new ChangeHouseNameDialogFragment();
                f.setArguments(bundle);
                f.show(fm, null);
            }
        });
        ((Button)v.findViewById(R.id.button_changehousepassword)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeHousePasswordDialogFragment f = new ChangeHousePasswordDialogFragment();
                f.setArguments(bundle);
                f.show(fm, null);
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        //Construct data source
        houseList = new ArrayList<House>();
        // Populate list from API call
        new RetrieveHouses().execute();

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
            Log.d(TAG, "Retrieve Houses: " + resp);
            response = resp;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            populateList(response, houseList);
        }
    }

    private void populateList(String response, ArrayList<House> houseList){
        houseList.clear();
        JSONObject houseJson = null;
        JSONArray jArray = null;
        try {
            houseJson = new JSONObject(response);
            jArray = new JSONArray(new JSONObject(response));
        } catch (JSONException e){
            Log.e(TAG, "JSONException: " + e.getMessage());
        }
        if (jArray!=null) {
            for (int i = 0; i < jArray.length(); i++) {
                try {
                    JSONObject obj = jArray.getJSONObject(i);
                    houseList.add(i, new House(obj.getString("HouseName")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

