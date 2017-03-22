package edu.temple.m.smarthomedroid;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {
    private TextView ss1,ss2,ss3,ss4,ss5,ss6;
    public Fragment1() {
        // Required empty public constructor
    }


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

        return v;
    }

}
