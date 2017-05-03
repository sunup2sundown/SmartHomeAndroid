package edu.temple.m.smarthomedroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.temple.m.smarthomedroid.Dialogs.ChangeHouseNameDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeHousePasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangePasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeUsernameDialogFragment;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Handlers.TaskHandler;
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
    HouseAdapter rAdapter;

    public ArrayList<House> getHouseList(){
        return houseList;
    }

    public HouseAdapter getHouseAdapter(){
        return rAdapter;
    }

    //String userid;
    public UserSettingsFragment() {
        // Required empty public constructor
    }

    public void refresh(final Bundle args) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                String newUsername = args.getString("Username");
                ((TextView)UserSettingsFragment.this.getView().findViewById(R.id.text_username))
                        .setText(newUsername);
                UserSettingsFragment.this.refreshData();
            }
        });
    }

    public void refreshData(){
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
        rAdapter = new HouseAdapter(getContext(), houseList, sessionToken);


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_settings,container,false);

        bundle = new Bundle();
        //Receive argument bundle from Home Activity
        userID = getArguments().getString("Username");
        sessionToken = getArguments().getString("SessionToken");
        fm = getActivity().getSupportFragmentManager();

        bundle.putString("SessionToken", sessionToken);

        TextView username = (TextView) v.findViewById(R.id.text_username);
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
        rAdapter = new HouseAdapter(getContext(), houseList, sessionToken);


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
    public class HouseAdapter extends ArrayAdapter<House> {
        private String sessionToken;
        private ArrayList<House> houseList;
        private OnHouseItemClickListener activity;

        /*
         * Constructor Method
         * @param context
         * @param houses
         */
        public HouseAdapter(Context context, ArrayList<House> houses, String sessionToken) {
            super(context, R.layout.item_house, houses);
            this.houseList = houses;
            this.sessionToken = sessionToken;
            try {
                this.activity = (OnHouseItemClickListener) context;
            } catch (ClassCastException e){
                throw new ClassCastException("activity must implement OnHouseItemClickListener");
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            final House house = (House)getItem(position);
            final int i = position;
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_house, parent, false);
            }
            TextView tvName = (TextView)convertView.findViewById(R.id.item_house_name);
            tvName.setText(house.getName());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onHouseItemClick(i, house.getName(), sessionToken);
                }
            });
            return convertView;
        }
    }
    public interface OnHouseItemClickListener{
        void onHouseItemClick(int index, String houseName, String sessionToken);
    }

    public static class NewHouseDialogFragment extends DialogFragment {

        private String sessionID;
        private NotifySettingsFragmentListener mListener;

        public void onAttach(Context context){
            super.onAttach(context);
            mListener = (NotifySettingsFragmentListener) context;
        }

        public static NewHouseDialogFragment newInstance() {
            NewHouseDialogFragment frag = new NewHouseDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", "Create New House");
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            sessionID = getArguments().getString("SessionToken");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            String title = getArguments().getString("title");
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_newhouse, null))
                    // Add action buttons
                    .setTitle(title)
                    .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            EditText houseName = (EditText) NewHouseDialogFragment.this.getDialog().findViewById(R.id.dialog_newhouse_name);
                            EditText housePassword = (EditText) NewHouseDialogFragment.this.getDialog().findViewById(R.id.dialog_newhouse_password);
                            createHouse(houseName.getText().toString(), housePassword.getText().toString());
                            // switch to the new house...
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            NewHouseDialogFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }

        private void createHouse(String name, String password) {
            Log.d("UserSettingsFragment", "createHouse");
            if (new TaskHandler().createHouse(getContext(), name, password, sessionID)) {
                Log.d("UserSettingsFragment", "createHouse returned true");
                mListener.updateSettingsFragment();
            } else {
                Log.d("UserSettingsFragment", "createHouse returned false");
            }
        }
    }

    public static class JoinHouseDialogFragment extends DialogFragment {
        private final String TAG = "JoinHouseDialog";
        private String sessionID;

        private NotifySettingsFragmentListener mListener;
        public void onAttach(Context context){
            super.onAttach(context);
            mListener = (NotifySettingsFragmentListener) context;
        }
        public JoinHouseDialogFragment newInstance() {
            JoinHouseDialogFragment frag = new JoinHouseDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", "Create New House");
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            sessionID = getArguments().getString("SessionToken");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            String title = getArguments().getString("title");
            // Inflate and set the layout for the dialog
            View view = inflater.inflate(R.layout.dialog_join_house, null);
            final EditText houseName = ((EditText)view.findViewById(R.id.dialog_join_house_name));
            final EditText password = ((EditText)view.findViewById(R.id.dialog_join_house_password));

            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view)
                    // Add action buttons
                    .setTitle(title)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            JoinHouseDialogFragment.this.getDialog().cancel();
                        }
                    })
                    .setPositiveButton("Join", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            joinHouse(houseName.getText().toString(), password.getText().toString());
                        }
                    });
            return builder.create();
        }

        private void joinHouse(String name, String password){
            if (new TaskHandler().joinHouse(getActivity(), name, password, sessionID)) {
                mListener.updateSettingsFragment();
            }
        }
    }
    public static class HouseOptionsDialogFragment extends DialogFragment{
        private String sessionID;
        private String houseName;
        private int index;
        private NotifySettingsFragmentListener mListener;
        public void onAttach(Context context){
            super.onAttach(context);
            mListener = (NotifySettingsFragmentListener) context;
        }
        public static HouseOptionsDialogFragment newInstance(int index, String houseName, String sessionToken) {
            HouseOptionsDialogFragment frag = new HouseOptionsDialogFragment();
            Bundle args = new Bundle();
            args.putInt("index", index);
            args.putString("title", "Options for House " + houseName);
            args.putString("HouseName", houseName);
            args.putString("SessionToken", sessionToken);
            frag.setArguments(args);
            return frag;
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            index = getArguments().getInt("index");
            houseName = getArguments().getString("HouseName");
            sessionID = getArguments().getString("SessionToken");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            String title = getArguments().getString("title");
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            View view = inflater.inflate(R.layout.dialog_house_options, null);
            ((Button)view.findViewById(R.id.button_change_name)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChangeHouseNameDialogFragment f = ChangeHouseNameDialogFragment.newInstance(index, houseName, sessionID);
                    f.show(getFragmentManager(), null);
                    HouseOptionsDialogFragment.this.getDialog().cancel();
                }
            });
            ((Button)view.findViewById(R.id.button_change_password)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChangeHousePasswordDialogFragment f = ChangeHousePasswordDialogFragment.newInstance(houseName, sessionID);
                    f.show(getFragmentManager(), null);
                    HouseOptionsDialogFragment.this.getDialog().cancel();
                }
            });
            ((Button)view.findViewById(R.id.button_leave_house)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LeaveHouse(houseName);
                    HouseOptionsDialogFragment.this.getDialog().cancel();
                }
            });
            builder.setView(view)
                    // Add action buttons
                    .setTitle(title)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            HouseOptionsDialogFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
        private void LeaveHouse(String name){
            if(new TaskHandler().leaveHouse(getActivity(), sessionID, name)){
                mListener.updateSettingsFragment();
            }
        }
    }
}

