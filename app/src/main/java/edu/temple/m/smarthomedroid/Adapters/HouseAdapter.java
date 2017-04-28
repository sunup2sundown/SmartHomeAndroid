package edu.temple.m.smarthomedroid.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Objects.House;
import edu.temple.m.smarthomedroid.R;

/**
 * Created by M on 4/3/2017.
 */

public class HouseAdapter extends ArrayAdapter<House> {
    private String sessionToken;
    private ArrayList<House> houseList;
    private OnHouseItemClickListener activity;

    /*
     * Constructor Method
     * @param context
     * @param houses
     */
    public HouseAdapter(Context context, ArrayList<House> houses, String sessionToken) {
        super(context, R.layout.item_house, houses);
        this.sessionToken = sessionToken;
        try {
            this.activity = (OnHouseItemClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException("activity must implement OnHouseItemClickListener");
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
                activity.onHouseItemClick(house.getName(), sessionToken);
            }
        });
        return convertView;
    }

    public interface OnHouseItemClickListener{
        void onHouseItemClick(String houseName, String sessionToken);
    }
}
