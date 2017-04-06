package edu.temple.m.smarthomedroid;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import edu.temple.m.smarthomedroid.Objects.Sensor;

/**
 * A simple {@link Fragment} subclass.
 */
public class SensorFragment extends Fragment {
    private TextView ss1,ss2,ss3,ss4,ss5,ss6;
    public SensorFragment() {
        // Required empty public constructor
    }
    ArrayList<Sensor> sensors;
    private String TAG = SensorFragment.class.getSimpleName();
    private ListAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.firstlayout,container,false);
        Bundle bundle=getArguments();
        String jsonString=bundle.getString("data");
        JSONObject obj=null;
        try {
            obj=new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, obj.toString());

        sensors = new ArrayList<>();

        // populate sensors array
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()){
            String id = keys.next();
            Sensor s = null;
            try {
                s = new Sensor(id, (int)obj.get(id));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sensors.add(s);
        }
        mAdapter = new ArrayAdapter<Sensor>(getActivity(), R.layout.item_sensor, sensors){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                if (convertView == null){
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_sensor, parent, false);
                }
                TextView name = (TextView) convertView.findViewById(R.id.name);
                View status = convertView.findViewById(R.id.status);
                final Sensor s = sensors.get(position);

                name.setText("");
                convertView.setBackgroundColor(Color.WHITE);
                return convertView;
            }
        };

        ListView lv = (ListView) v.findViewById(R.id.listview_sensors);
        lv.setAdapter(mAdapter);

        /*
        ss1 = (TextView) v.findViewById(R.id.stt1);
        ss2 = (TextView) v.findViewById(R.id.stt2);
        ss3 = (TextView) v.findViewById(R.id.stt3);
        ss4 = (TextView) v.findViewById(R.id.stt4);
        ss5 = (TextView) v.findViewById(R.id.stt5);
        ss6 = (TextView) v.findViewById(R.id.stt6);

        try{
            if(obj.getInt("ss1")==1000000){
                ss1.setText("OFF");
            }else if(obj.getInt("ss1")==2000000){
                ss1.setText("ON");
            }else{
                ss1.setText(obj.get("ss1").toString()+" degree");
            }

            if(obj.getInt("ss2")==1000000){
                ss2.setText("OFF");
            }else if(obj.getInt("ss2")==2000000){
                ss2.setText("ON");
            }else{
                ss2.setText(obj.get("ss2").toString()+" degree");
            }

            if(obj.getInt("ss3")==1000000){
                ss3.setText("OFF");
            }else if(obj.getInt("ss3")==2000000){
                ss3.setText("ON");
            }else{
                ss3.setText(obj.get("ss3").toString()+" degree");
            }

            if(obj.getInt("ss4")==1000000){
                ss4.setText("OFF");
            }else if(obj.getInt("ss4")==2000000){
                ss4.setText("ON");
            }else{
                ss4.setText(obj.get("ss4").toString()+" degree");
            }

            if(obj.getInt("ss5")==1000000){
                ss5.setText("OFF");
            }else if(obj.getInt("ss5")==2000000){
                ss5.setText("ON");
            }else{
                ss5.setText(obj.get("ss5").toString()+" degree");
            }

            if(obj.getInt("ss6")==1000000){
                ss6.setText("OFF");
            }else if(obj.getInt("ss6")==2000000){
                ss6.setText("ON");
            }else{
                ss6.setText(obj.get("ss6").toString()+" degree");
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
