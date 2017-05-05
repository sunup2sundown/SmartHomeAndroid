package edu.temple.m.smarthomedroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Handlers.TaskHandler;
import edu.temple.m.smarthomedroid.Objects.Relay;
import edu.temple.m.smarthomedroid.Objects.Sensor;
import edu.temple.m.smarthomedroid.R;

/**
 * Created by M on 4/3/2017.
 */

public class SensorAdapter extends ArrayAdapter<Sensor>{
    String sessionToken;

    /**
     * Constructor Method
     * @param context
     * @param sensors
     */
    public SensorAdapter(Context context, ArrayList<Sensor> sensors, String sessionToken){
        super(context, android.R.layout.simple_list_item_1, sensors);
        this.sessionToken = sessionToken;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Sensor sensor = (Sensor)getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_sensor, parent, false);
        }
        TextView tvStatus = (TextView) convertView.findViewById(R.id.status);

        TextView tvName = (TextView)convertView.findViewById(R.id.name);

        tvName.setText(sensor.getName());
        tvName.setTextSize(16);
        tvStatus.setText("" + sensor.getValue());
        tvStatus.setTextSize(16);
        return convertView;
    }

}
