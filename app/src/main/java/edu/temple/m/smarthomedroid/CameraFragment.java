package edu.temple.m.smarthomedroid;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Adapters.CameraAdapter;
import edu.temple.m.smarthomedroid.Handlers.TaskHandler;
import edu.temple.m.smarthomedroid.Objects.Camera;

import static java.lang.Thread.sleep;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends ListFragment{
    Bundle bundle;

    private final String TAG = "CameraFragment";
    private String userID;
    private String sessionID;
    private String houseName;
    private ArrayList<Camera> cameraList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //Create new view from relay fragment layout
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        bundle = new Bundle();
        //Receive argument bundle from Home Activity
        userID = getArguments().getString("Username");
        sessionID = getArguments().getString("SessionToken");

        return view;
    }

    public void onBackPressed()
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        houseName = getArguments().getString("HouseName");
        //Construct data source
        cameraList = new ArrayList<Camera>();

        Log.d(TAG, "For JSONArray: " + houseName + " " + sessionID);
        JSONArray jsonArray =  new TaskHandler().getCurrentCameraByHouse(getContext(), sessionID, houseName);

        //Create and set custom adapter for relay list
        CameraAdapter cAdapter = new CameraAdapter(getContext(), cameraList, sessionID);

        int k=0;
        if(jsonArray != null) {
            int o =0;
            for(;o<jsonArray.length();o++){
                String name="";
                int val=0;
                try {
                    JSONObject check= jsonArray.getJSONObject(o);
                    name = check.getString("PeripheralName");
                    //Log.d(TAG, name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                cAdapter.add(new Camera(sessionID, houseName, name));
            }
        }else{

        }
        ListView lv = (ListView)getView().findViewById(R.id.fragment_camera_listview);
        lv.setAdapter(cAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = parent.getItemAtPosition(position);
                Camera camera = (Camera)object;

                String name = camera.getName();
                Log.d(TAG, "Clicked Camera: " + name);

                Bundle bundle = new Bundle();
                bundle.putString("CameraName", name);
                bundle.putString("HouseName", houseName);
                bundle.putString("SessionToken", sessionID);
                bundle.putString("Username", userID);

                Fragment fragment = new CameraFeedFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.flContent, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
