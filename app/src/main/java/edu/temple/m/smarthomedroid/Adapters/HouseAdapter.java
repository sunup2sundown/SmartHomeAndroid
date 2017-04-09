package edu.temple.m.smarthomedroid.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Dialogs.HouseOptionsDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.SignupDialogFragment;
import edu.temple.m.smarthomedroid.Objects.House;
import edu.temple.m.smarthomedroid.R;

/**
 * Created by M on 4/3/2017.
 */

public class HouseAdapter extends ArrayAdapter<House> {
    private OnHouseAdapterItemClickListener activity;
    private boolean useList = true;

    /**
     * Constructor Method
     * @param context
     * @param houses
     */
    public HouseAdapter(Context context, ArrayList<House> houses){
        super(context, android.R.layout.simple_list_item_1, houses);
        try{
            //Instantiate listener so events can be sent to host
            this.activity = (OnHouseAdapterItemClickListener) context;
        } catch(ClassCastException e){
            //Activity doesn't implement
            throw new ClassCastException("activity must implement OnHouseAdapterItemClickListener");
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final House house = (House)getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_house, parent, false);
        }

        TextView tvName = (TextView)convertView.findViewById(R.id.item_house_name);

        tvName.setText(house.getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.OnHouseAdapterItemClick(house.getName());
            }
        });

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    public interface OnHouseAdapterItemClickListener{
        void OnHouseAdapterItemClick(String houseName);
    }
}
