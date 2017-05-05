package edu.temple.m.smarthomedroid.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Handlers.refresh;
import edu.temple.m.smarthomedroid.R;

import static edu.temple.m.smarthomedroid.SystemSettingsFragment.mpass2;
import static edu.temple.m.smarthomedroid.UserSettingsFragment.mpass;
import static java.lang.Thread.sleep;

/**
 * Created by Qui Do on 4/15/2017.
 */

public class AddPeripheralDialogFragment extends DialogFragment {
    private JSONArray listtype,listmodel,listpin;
    private String sessionID,housename,boardname;
    private String TAG = "AddPeriFrag";
    private Spinner model,pin,type;
    private String fname,ftype,fpin,fmodel;
    private int fcheck=0;
    private refresh update;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        update = (refresh) activity;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        sessionID = getArguments().getString("SessionToken");
        housename = getArguments().getString("HouseName");
        boardname = getArguments().getString("OldBoard");


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View myView = inflater.inflate(R.layout.dialog_add_peripheral, null);
        String title = "Add Peripheral";
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(myView);
        builder.setTitle(title);
        type=(Spinner)myView.findViewById(R.id.type);
        model = (Spinner)myView.findViewById(R.id.model);
        pin = (Spinner)myView.findViewById(R.id.pin_connection);
        addtype();
        type.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        ftype=item.toString();
                        additem(ftype);
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        model.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        fmodel=item.toString();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        pin.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        fpin=item.toString();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        fname = ((EditText)myView.findViewById(R.id.peri_name)).getText().toString();
                        new addperi(fname,fpin,fmodel).execute();
                        try {
                            sleep(1800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(fcheck==1){
                            Log.d(TAG,"oh yeah");
                        }else{
                            Log.d(TAG,"oh NO");
                        }
                        AddPeripheralDialogFragment.this.getDialog().cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddPeripheralDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }


    ///Set up type spinner*****************************
    ///Set up type spinner*****************************
    public void addtype(){
        List<String> list1 = new ArrayList<String>();
        new gettype().execute();
        try {
            sleep(2100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(listtype!=null){
            Log.d("Array: ",listtype.toString());
            int o=0;
            for(;o<listtype.length();o++){
                try {
                    list1.add(listtype.getJSONObject(o).getString("PeripheralTypeName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ArrayAdapter<String> dataAdapter;
            dataAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.item_spinner,list1);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            type.setAdapter(dataAdapter);
        }else{
            Log.d(TAG,"null");
        }
    }
    private class gettype extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String session = sessionID;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "sesssss: " + session);
            try{
                jsonObject.put("SessionToken", session);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }
        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            Log.e(TAG, "sesssss: " + jsonObject.toString());
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/peripheral/getperipheraltypes", jsonObject);
            if (resp != null) {
                Log.d(TAG, "add type : " + resp);
                try {
                    JSONArray ii = new JSONArray(resp);
                    listtype= ii.getJSONArray(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    ///SET UP MODEL AND PIN SPINNERS************
    ///SET UP MODEL AND PIN SPINNERS************
    ///SET UP MODEL AND PIN SPINNERS************

    public void additem(String type){
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        new populate(type).execute();
        try {
            sleep(3500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (listmodel!=null) {
            int o=0;
            for(;o<listmodel.length();o++){
                try {
                    list1.add(listmodel.getJSONObject(o).getString("PeripheralModelName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ArrayAdapter<String> dataAdapter;
            dataAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.item_spinner,list1);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            model.setAdapter(dataAdapter);
        }else{
            Log.d(TAG,"null");
        }
        if (listpin!=null) {
            int o=0;
            Log.d(TAG,listpin.toString());
            for(;o<listpin.length();o++){
                try {
                    list2.add(listpin.getJSONObject(o).getString("PinConnectionName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ArrayAdapter<String> dataAdapter2;
            dataAdapter2 = new ArrayAdapter<String>(this.getContext(), R.layout.item_spinner,list2);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            pin.setAdapter(dataAdapter2);
        }else{
            Log.d(TAG,"null");
        }
    }
    private class populate extends AsyncTask<Void, Void, Void> {
        String type;
        public populate(String type){
            this.type=type;
        }
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        String session = sessionID;
        String housena = housename;
        String boardna = boardname;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
                jsonObject.put("SessionToken", session)
                        .put("PeripheralTypeName",type);
                jsonObject2.put("HouseName",housena)
                        .put("SessionToken",session)
                        .put("BoardName",boardna)
                        .put("PeripheralTypeName",type);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            //Make a request to url and get response
            Log.d(TAG, "object : " + jsonObject.toString());
            Log.d(TAG, "object2 : " + jsonObject2.toString());
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/peripheral/getperipheralmodelsbyperipheraltype", jsonObject);
            String resp2 = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/board/getavailablepinconnections", jsonObject2);
                if (resp != null) {
                    Log.d(TAG, "add model : " + resp);
                    try {
                        JSONArray ii = new JSONArray(resp);
                        listmodel= ii.getJSONArray(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (resp2!=null){
                    Log.d(TAG, "add pin : " + resp2);
                    try {
                        JSONArray iii = new JSONArray(resp2);
                        listpin= iii.getJSONArray(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    ///CREATE PERIPHERAL API CALL*********
    ///CREATE PERIPHERAL API CALL*********
    ///CREATE PERIPHERAL API CALL*********
    private class addperi extends AsyncTask<Void, Void, Void> {
        String nname,pin,model;
        public addperi(String name,String pin, String model){
            this.nname=name;
            this.pin=pin;
            this.model=model;
        }
        JSONObject jsonObject = new JSONObject();
        JSONObject checkname = new JSONObject();
        String session = sessionID;
        String housena = housename;
        String boardna = boardname;
        String resp;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
                jsonObject.put("sessionToken", session)
                        .put("houseName",housena).put("boardName",boardna).put("peripheralName",nname)
                        .put("peripheralModelName",model).put("pinConnectionName",pin);
                checkname.put("sessionToken", session)
                        .put("houseName",housena).put("peripheralName",nname);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }
        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            //Make a request to url and get response
            Log.d(TAG, "object : " + jsonObject.toString());
            Log.d(TAG, "checkname : " + checkname.toString());
            String checkn = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/peripheral/checkpheriperalnameavailability", checkname);
            if(checkn!=null){
                Log.d(TAG, checkn);
                if(checkn.equalsIgnoreCase("1")){
                    fcheck=1;
                    resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/peripheral/createperipheral", jsonObject);
                    if(resp!=null){
                        Log.d(TAG, resp);
                    }
                }else{
                    fcheck=0;
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(fcheck==0){
                mpass2.msg2("Name is invalid/taken, please try again!");
            }else{
                if(resp!=null){
                    if(resp.equals("[]")){
                        mpass2.msg2("New Peripheral added!");
                        update.config_update();
                    }else{
                        mpass2.msg2("Failed, please try again!");
                    }
                }
            }
        }
    }
}
