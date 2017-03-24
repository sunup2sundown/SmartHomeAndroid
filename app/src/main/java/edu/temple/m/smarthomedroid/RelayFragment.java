package edu.temple.m.smarthomedroid;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class RelayFragment extends Fragment {
    private Switch ss1,ss2,ss3,ss4,ss5,ss6,ss7;
    public RelayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.secondlayout,container,false);
        ss1 = (Switch) v.findViewById(R.id.switch1);
        ss2 = (Switch) v.findViewById(R.id.switch2);
        ss3 = (Switch) v.findViewById(R.id.switch3);
        ss4 = (Switch) v.findViewById(R.id.switch4);
        ss5 = (Switch) v.findViewById(R.id.switch5);
        ss6 = (Switch) v.findViewById(R.id.switch6);
        ss7 = (Switch) v.findViewById(R.id.switch7);
        Bundle bundle2=getArguments();
        String jsonString=bundle2.getString("data2");
        JSONObject obj2=null;
        try {
            obj2=new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

        return v;
    }

}
