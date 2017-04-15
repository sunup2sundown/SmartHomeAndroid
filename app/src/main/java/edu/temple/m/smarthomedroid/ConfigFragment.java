package edu.temple.m.smarthomedroid;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListView;

/**
 * Created by M on 3/16/2017.
 */

public class ConfigFragment extends Fragment{

    private static final String TAG = "ConfigFragment";

    private Bundle bundle;

    private String userID;
    private String sessionID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.config_layout,container,false);

        bundle = new Bundle();
        //Receive argument bundle from Home Activity
        userID = getArguments().getString("Username");
        sessionID = getArguments().getString("SessionToken");

        //Log.d(TAG, "Session Token: " + sessionID);

        bundle.putString("SessionToken", sessionID);

        return v;
    }

    //TODO: Expandable List view of Houses->Boards->Peripheral types->Peripheral Names
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ExpandableListView elv = (ExpandableListView)getView().findViewById(R.id.elv_peripherals);
    }
}
