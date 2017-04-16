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

import edu.temple.m.smarthomedroid.Objects.Peripheral;
import edu.temple.m.smarthomedroid.Objects.Relay;
import edu.temple.m.smarthomedroid.R;

/**
 * Created by Jhang Myong Ja on 4/15/2017.
 */

public class PeripheralAdapter extends ArrayAdapter<Peripheral> {
    public OnPeripheralAdapterItemClickListener activity;
    public PeripheralAdapter(Context context, ArrayList<Peripheral> peripherals){
        super(context, android.R.layout.simple_list_item_1, peripherals);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Peripheral p = (Peripheral)getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_peripheral, parent, false);
        }
        ((TextView)convertView.findViewById(R.id.peripheral_name)).setText(p.getName());
        ((TextView)convertView.findViewById(R.id.peripheral_type)).setText(p.getType());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onPeripheralAdapterItemClick(p.getName());
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                activity.removePeripheral(p);
                PeripheralAdapter.this.remove(p);
                return true;
            }
        });
        return convertView;
    }
    public interface OnPeripheralAdapterItemClickListener{
        void onPeripheralAdapterItemClick(String name);
        void removePeripheral(Peripheral p);
    }
}
