package edu.temple.m.smarthomedroid;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.temple.m.smarthomedroid.Adapters.RelayAdapter;
import edu.temple.m.smarthomedroid.Objects.Relay;

/**
 * Created by M on 4/4/2017.
 */

public class RelayFragment extends ListFragment implements AdapterView.OnItemClickListener{

    ArrayList<Relay> relayList;
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
        relayList = new ArrayList<Relay>();
        //Populate relaysList from API call
        //populateList();

        //Create and set custom adapter for relay list
        RelayAdapter rAdapter = new RelayAdapter(getContext(), relayList);

        rAdapter.add(new Relay("0", "Test Relay", 0));
        rAdapter.add(new Relay("1", "Test Relay1", 1));
        rAdapter.add(new Relay("2", "Test Relay2", 0));
        rAdapter.add(new Relay("2", "Test Relay3", 0));
        rAdapter.add(new Relay("2", "Test Relay4", 1));
        rAdapter.add(new Relay("2", "Test Relay5", 1));
        rAdapter.add(new Relay("2", "Test Relay6", 0));

        ListView lv = (ListView)getView().findViewById(R.id.fragment_relay_listview);
        lv.setAdapter(rAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Toast.makeText(getActivity(), "Clicked: " + position, Toast.LENGTH_SHORT).show();
    }

    private void populateList(){
        Relay newRelay = new Relay("0", "Test Relay", 0);

        relayList.add(0, newRelay);
    }
}

