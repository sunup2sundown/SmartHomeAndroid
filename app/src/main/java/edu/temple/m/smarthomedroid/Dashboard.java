package edu.temple.m.smarthomedroid;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Adapters.HouseAdapter;
import edu.temple.m.smarthomedroid.Adapters.RelayAdapter;
import edu.temple.m.smarthomedroid.Adapters.SensorAdapter;
import edu.temple.m.smarthomedroid.Objects.House;
import edu.temple.m.smarthomedroid.Objects.Relay;
import edu.temple.m.smarthomedroid.Objects.Sensor;

/**
 * Created by quido on 3/25/17.
 */

public class Dashboard extends Fragment {
    ArrayList<House> houseList;
    ArrayList<Sensor> sensorList;
    ArrayList<Relay> relayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.dashboard, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //Construct data source
        houseList = new ArrayList<House>();
        //Populate sensorList from API call
        House newHouse = new House("0", "Test House 0");

        houseList.add(0, newHouse);


        //Create and set custom adapter for relay list
        HouseAdapter hAdapter = new HouseAdapter(getContext(), houseList);

        hAdapter.add(new House("1", "Test House 1"));

        Spinner sp = (Spinner)getView().findViewById(R.id.spinner_houses);
        sp.setAdapter(hAdapter);

        //Construct data source
        sensorList = new ArrayList<Sensor>();
        //Populate sensorList from API call

        //Create and set custom adapter for relay list
        SensorAdapter sAdapter = new SensorAdapter(getContext(), sensorList);

        sAdapter.add(new Sensor("1", "Test Sensor", 0));

        ListView lvSensors = (ListView)getView().findViewById(R.id.listview_sensors);
        lvSensors.setAdapter(sAdapter);

        //Construct data source
        relayList = new ArrayList<Relay>();
        //Populate relaysList from API call

        //Create and set custom adapter for relay list
        RelayAdapter rAdapter = new RelayAdapter(getContext(), relayList);

        rAdapter.add(new Relay("1", "Test Relay 1", 0));

        ListView lvRelays = (ListView)getView().findViewById(R.id.listview_relays);
        lvRelays.setAdapter(rAdapter);

    }

}
