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
import edu.temple.m.smarthomedroid.Objects.House;
import edu.temple.m.smarthomedroid.Objects.Peripheral;
import edu.temple.m.smarthomedroid.Objects.Relay;
import edu.temple.m.smarthomedroid.R;

/**
 * Created by Jhang Myong Ja on 4/15/2017.
 */

public class PeripheralAdapter extends ArrayAdapter<Peripheral> {
    private OnPeripheralAdapterItemClickListener activity;
    private House house;
    private String sessionToken;
    private final String TAG = "PeripheralAdapter";
    public PeripheralAdapter(Context context, House h, ArrayList<Peripheral> peripherals, String sessionToken){
        super(context, android.R.layout.simple_list_item_1, peripherals);
        this.house = h;
        this.sessionToken = sessionToken;
        this.activity = (OnPeripheralAdapterItemClickListener) context;
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
                removePeripheral(house, p, sessionToken);
                PeripheralAdapter.this.remove(p);
                return true;
            }
        });
        return convertView;
    }
    private void removePeripheral(House house, Peripheral peripheral, String sessionToken){
        new TaskHandler().removePeripheral(TAG, house.getName(), peripheral.getName(), sessionToken);
    }

    public interface OnPeripheralAdapterItemClickListener {
        void onPeripheralAdapterItemClick(String peripheralName);
    }
}
