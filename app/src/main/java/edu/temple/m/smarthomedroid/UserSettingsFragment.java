package edu.temple.m.smarthomedroid;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Adapters.HouseAdapter;
import edu.temple.m.smarthomedroid.Dialogs.ChangeHouseNameDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeHousePasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangePasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeUsernameDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.JoinHouseDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.NewHouseDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.SwitchHouseDialogFragment;
import edu.temple.m.smarthomedroid.Objects.House;

/**
 * Created by M on 3/16/2017.
 */

public class UserSettingsFragment extends Fragment {

    FragmentManager fm;

    private final String TAG = "SettingsFragment";
    private String sessionID, userID;
    private Bundle bundle;

    private String usern, userPassword, newUserPassword;
    private String houseName, newHouseName, housePassword, newHousePassword;
    private String  sessionToken;
    private ArrayList<House> houseList;

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
        sessionID = getArguments().getString("SessionToken");

        bundle.putString("SessionToken", sessionID);

        TextView username = (TextView) v.findViewById(R.id.text_username);
        usern = "Tom Brady";
        sessionToken = "51FAA52D-CD90-461A-8735-D866DB3BDFF3";
        fm = getActivity().getSupportFragmentManager();

        username.setText(usern);

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

    private void populateList(){
        houseList.add(0, new House("0", "John's House"));
        houseList.add(0, new House("1", "Mary's House"));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        //Construct data source
        houseList = new ArrayList<House>();
        //Populate list from API call
        populateList();


        //Create and set custom adapter for relay list
        HouseAdapter adapter = new HouseAdapter(getActivity(), houseList);

        ExpandableListView lv = (ExpandableListView)getView().findViewById(R.id.listview_houses);
        lv.setAdapter(adapter);
    }

}

