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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.temple.m.smarthomedroid.Adapters.HouseAdapter;
import edu.temple.m.smarthomedroid.Dialogs.ChangePasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeUsernameDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.JoinHouseDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.NewHouseDialogFragment;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Objects.House;

import static java.lang.Thread.sleep;

/**
 * Created by M on 3/16/2017.
 */

public class UserSettingsFragment extends Fragment {

    FragmentManager fm;

    private final String TAG = "SettingsFragment";
    private String userID;
    private Bundle bundle;

    private JSONArray jArray;
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
        ((Button)v.findViewById(R.id.button_joinhouse)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinHouseDialogFragment f = new JoinHouseDialogFragment();
                f.setArguments(bundle);
                f.show(fm, null);
            }
        });

        return v;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        sessionToken = getArguments().getString("SessionToken");
        //Construct data source
        houseList = new ArrayList<>();
        new RetrieveHouses().execute();

        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Create and set custom adapter for relay list
        HouseAdapter rAdapter = new HouseAdapter(getContext(), houseList, sessionToken);


        int k=0;
        if(jArray!=null) {
            int o =0;
            for(;o<jArray.length();o++){
                String name="";
                try {
                    JSONObject check= jArray.getJSONObject(o);
                    name = check.getString("HouseName");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rAdapter.add(new House(name));
            }
        }else{

        }
        ListView lv = (ListView)getView().findViewById(R.id.listview_houses);
        lv.setAdapter(rAdapter);
    }

    /*
    ** Asynchronous Tasks -- HTTP Calls
     *
     */

    private class RetrieveHouses extends AsyncTask<Void, Void, Void> {
        JSONObject json = new JSONObject();
        String session = sessionToken;
        String resp;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            if(session != null) {
                try {
                    json.put("sessionToken", session);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String resp;
            //Make a request to url and get response
            resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/house/listhouses", json);
            if (resp!=null){
                Log.d(TAG, "Retrieve Houses Response: " + resp);
                try {
                    jArray=new JSONArray(resp);
                    jArray=jArray.getJSONArray(0);
                    Log.d(TAG, "Retrieve Houses Array: " + jArray);
                } catch (JSONException e) {
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
}

