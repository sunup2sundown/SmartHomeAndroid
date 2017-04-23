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
import edu.temple.m.smarthomedroid.R;


/**
 * Created by M on 4/3/2017.
 */

public class RelayAdapter extends ArrayAdapter<Relay> {
    private Context context;
    private boolean useList = true;
    String sessionToken;

    /**
     * Constructor Method
     * @param context
     * @param relays
     */
    public RelayAdapter(Context context, ArrayList<Relay> relays, String sessionToken){
        super(context, android.R.layout.simple_list_item_1, relays);
        this.sessionToken = sessionToken;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Relay relay = (Relay)getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_relay, parent, false);
        }
        Switch stt = (Switch)convertView.findViewById(R.id.switch1);
        if(relay.getStatus()){
            stt.setChecked(true);
        }else{
            stt.setChecked(false);
        }

        stt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        try {
                            relay.switchOn();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new TaskHandler().setRelayStatus(getContext(), sessionToken, relay.getName(), relay.getHouseName(), "1");
                    } else {
                        try {
                            relay.switchOff();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new TaskHandler().setRelayStatus(getContext(), sessionToken, relay.getName(), relay.getHouseName(), "0");
                    }
                }
            });
        TextView tvName = (TextView)convertView.findViewById(R.id.item_relay_name);

        tvName.setText(relay.getName());

        return convertView;
    }

}
