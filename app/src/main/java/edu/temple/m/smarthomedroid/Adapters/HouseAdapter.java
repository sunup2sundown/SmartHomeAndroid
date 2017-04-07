package edu.temple.m.smarthomedroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Objects.House;
import edu.temple.m.smarthomedroid.R;

/**
 * Created by M on 4/3/2017.
 */

public class HouseAdapter extends ArrayAdapter<House> {
    private Context context;
    private boolean useList = true;

    /**
     * Constructor Method
     * @param context
     * @param houses
     */
    public HouseAdapter(Context context, ArrayList<House> houses){
        super(context, android.R.layout.simple_list_item_1, houses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        House house = (House)getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_house, parent, false);
        }

        TextView tvName = (TextView)convertView.findViewById(R.id.item_house_name);

        tvName.setText(house.getName());

        return convertView;
    }
}
