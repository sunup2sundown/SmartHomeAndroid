package edu.temple.m.smarthomedroid;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Adapters.PeripheralAdapter;
import edu.temple.m.smarthomedroid.Objects.Peripheral;
import edu.temple.m.smarthomedroid.Objects.Relay;
import edu.temple.m.smarthomedroid.Objects.Sensor;

/**
 * Created by M on 3/16/2017.
 */

public class ConfigFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_config,container,false);
        return v;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        //Construct data source
        ArrayList<Peripheral> peripheralList = new ArrayList<Peripheral>();
        //Populate peripheralList from API call

        //Create and set custom adapter for relay list
        PeripheralAdapter adapter = new PeripheralAdapter(getContext(), peripheralList);

        adapter.add(new Relay("1", "Test Relay 1", 0));
        adapter.add(new Sensor("1", "Test Sensor 1", 0));

        GridView gv = (GridView)getView().findViewById(R.id.gridview_peripherals);
        gv.setAdapter(adapter);
    }
}
