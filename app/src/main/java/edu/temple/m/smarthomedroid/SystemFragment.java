package edu.temple.m.smarthomedroid;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Adapters.PeripheralAdapter;
import edu.temple.m.smarthomedroid.Adapters.RelayAdapter;
import edu.temple.m.smarthomedroid.Objects.Peripheral;
import edu.temple.m.smarthomedroid.Objects.Relay;

/**
 * A simple {@link Fragment} subclass.
 */
public class SystemFragment extends Fragment {


    public SystemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_system,container,false);
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
        adapter.add(new Relay("2", "Test Relay 2", 0));
        adapter.add(new Relay("3", "Test Relay 3", 0));
        adapter.add(new Relay("4", "Test Relay 4", 0));

        GridView gv = (GridView)getView().findViewById(R.id.gridview_peripherals);
        gv.setAdapter(adapter);
    }

}
