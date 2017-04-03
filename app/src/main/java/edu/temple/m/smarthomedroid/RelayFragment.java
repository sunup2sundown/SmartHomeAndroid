package edu.temple.m.smarthomedroid;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class RelayFragment extends Fragment {
    /* private Switch ss1,ss2,ss3,ss4,ss5,ss6,ss7; */
    public RelayFragment() {
        // Required empty public constructor
    }

    private String TAG = RelayFragment.class.getSimpleName();
    private ListAdapter mAdapter;
    ArrayList<Relay> relays = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.secondlayout,container,false);

        Bundle bundle2=getArguments();
        String jsonString=bundle2.getString("data2");
        JSONObject obj2=null;
        try {
            obj2=new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, obj2.toString());

        // populate relays array
        Iterator<String> keys = obj2.keys();
        while (keys.hasNext()){
            String id = keys.next();
            Relay r = null;
            try {
                r = new Relay(id, (int)obj2.get(id) == 1 ? true : false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            relays.add(r);
        }
        mAdapter = new ArrayAdapter<Relay>(getActivity(), R.layout.item_relay, relays){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                if (convertView == null){
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_relay, parent, false);
                }
                TextView name = (TextView) convertView.findViewById(R.id.name);
                Switch status = (Switch) convertView.findViewById(R.id.status);
                final Relay rl = relays.get(position);

                name.setText(rl.id);
                status.setChecked(rl.status);
                status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            // switch relay on...
                            rl.switchOn();
                            Log.d(rl.id, "" + rl.status);
                        } else {
                            // switch relay off...
                            rl.switchOff();
                            Log.d(rl.id, "" + rl.status);
                        }
                    }
                });
                convertView.setBackgroundColor(Color.WHITE);
                return convertView;
            }
        };

        ListView lv = (ListView) v.findViewById(R.id.listview_relays);
        lv.setAdapter(mAdapter);
        /*
        ss1 = (Switch) v.findViewById(R.id.switch1);
        ss2 = (Switch) v.findViewById(R.id.switch2);
        ss3 = (Switch) v.findViewById(R.id.switch3);
        ss4 = (Switch) v.findViewById(R.id.switch4);
        ss5 = (Switch) v.findViewById(R.id.switch5);
        ss6 = (Switch) v.findViewById(R.id.switch6);
        ss7 = (Switch) v.findViewById(R.id.switch7);
        try{
            if(obj2.getInt("ss1")==1){
                ss1.setChecked(true);
            }else{
                ss1.setChecked(false);
            }

            if(obj2.getInt("ss2")==1){
                ss2.setChecked(true);
            }else{
                ss2.setChecked(false);
            }

            if(obj2.getInt("ss3")==1){
                ss3.setChecked(true);
            }else{
                ss3.setChecked(false);
            }

            if(obj2.getInt("ss4")==1){
                ss4.setChecked(true);
            }else{
                ss4.setChecked(false);
            }

            if(obj2.getInt("ss5")==1){
                ss5.setChecked(true);
            }else{
                ss5.setChecked(false);
            }

            if(obj2.getInt("ss6")==1){
                ss6.setChecked(true);
            }else{
                ss6.setChecked(false);
            }

            if(obj2.getInt("ss7")==1){
                ss7.setChecked(true);
            }else{
                ss7.setChecked(false);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        */
        return v;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
}
