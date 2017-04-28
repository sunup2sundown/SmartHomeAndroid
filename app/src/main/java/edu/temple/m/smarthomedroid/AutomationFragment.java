package edu.temple.m.smarthomedroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by DongBinChoi on 4/28/2017.
 */

public class AutomationFragment extends Fragment {
    Bundle bundle;

    private final String TAG = "AutomationFragment";
    private String userID;
    private String sessionID;
    private String houseName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //Create new view from relay fragment layout
        View view = inflater.inflate(R.layout.fragment_automation, container, false);
        bundle = new Bundle();
        //Receive argument bundle from Home Activity
        userID = getArguments().getString("Username");
        sessionID = getArguments().getString("SessionToken");

        bundle.putString("SessionToken", sessionID);

        return view;
    }

    public void onBackPressed()
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }
}
