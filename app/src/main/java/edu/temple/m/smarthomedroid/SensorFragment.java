package edu.temple.m.smarthomedroid;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import edu.temple.m.smarthomedroid.Adapters.SensorAdapter;
import edu.temple.m.smarthomedroid.Objects.Relay;
import edu.temple.m.smarthomedroid.Objects.Sensor;

/**
 * A simple {@link Fragment} subclass.
 */
public class SensorFragment extends ListFragment implements AdapterView.OnItemClickListener {

    ArrayList<Sensor> sensorList;
    Adapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //Create new view from relay fragment layout
        View view = inflater.inflate(R.layout.fragment_relay, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        //Construct data source
        sensorList = new ArrayList<Sensor>();
        //Populate sensorList from API call
        //populateList();

        //Create and set custom adapter for relay list
        SensorAdapter adapter = new SensorAdapter(getContext(), sensorList);

        adapter.add(new Sensor("0", "Test Relay", 0));

        ListView lv = (ListView)getView().findViewById(R.id.fragment_sensor_listview);
        lv.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Toast.makeText(getActivity(), "Clicked: " + position, Toast.LENGTH_SHORT).show();
    }

    private void populateList(){
        Sensor newSensor = new Sensor("0", "Test Sensor", 0);

        sensorList.add(0, newSensor);
    }
}
