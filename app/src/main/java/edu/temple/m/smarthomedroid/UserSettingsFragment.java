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
import android.widget.TextView;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.temple.m.smarthomedroid.Dialogs.ChangeHouseNameDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeHousePasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangePasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeUsernameDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.JoinHouseDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.LeaveHouseDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.NewHouseDialogFragment;
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
        ((Button)v.findViewById(R.id.button_leavehouse)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeaveHouseDialogFragment f = new LeaveHouseDialogFragment();
                f.setArguments(bundle);
                f.show(fm, null);
            }
        });

        return v;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
    }
}

