package edu.temple.m.smarthomedroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Objects.Peripheral;
import edu.temple.m.smarthomedroid.Objects.Relay;
import edu.temple.m.smarthomedroid.R;

/**
 * Created by Jhang Myong Ja on 4/7/2017.
 */

public class PeripheralAdapter extends ArrayAdapter<Peripheral> {

    private Context context;
    private boolean useList = true;

    /**
     * Constructor Method
     * @param context
     * @param peripherals
     */
    public PeripheralAdapter(Context context, ArrayList<Peripheral> peripherals){
        super(context, android.R.layout.simple_list_item_1, peripherals);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Peripheral per = (Peripheral)getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_peripheral, parent, false);
        }

        TextView tvName = (TextView)convertView.findViewById(R.id.item_peripheral_name);

        tvName.setText(per.getName());

        return convertView;
    }
}
