package edu.temple.m.smarthomedroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Objects.Sensor;

import edu.temple.m.smarthomedroid.R;
/**
 * Created by M on 4/3/2017.
 */

public class SensorAdapter extends ArrayAdapter<Sensor> {

    private Context context;
    private boolean useList = true;

    /**
     * Constructor Method
     * @param context
     * @param sensors
     */
    public SensorAdapter(Context context, ArrayList<Sensor> sensors){
        super(context, android.R.layout.simple_list_item_1, sensors);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Sensor sensor = (Sensor) getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_sensor, parent, false);
        }

        TextView tvName = (TextView)convertView.findViewById(R.id.item_sensor_name);

        tvName.setText(sensor.getName());
        
        return convertView;
    }



}
