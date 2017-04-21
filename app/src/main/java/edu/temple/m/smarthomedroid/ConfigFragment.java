package edu.temple.m.smarthomedroid;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.temple.m.smarthomedroid.Dialogs.AddBoardDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.AddPeripheralDialogFragment;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Handlers.TaskHandler;
import edu.temple.m.smarthomedroid.Objects.Board;
import edu.temple.m.smarthomedroid.Objects.House;

import static java.lang.Thread.sleep;

/**
 * Created by M on 3/16/2017.
 */

public class ConfigFragment extends Fragment{
    FragmentManager fm;
    private JSONArray houses;

    private String sessionToken = "";
    private String houseName = "";
    private JSONArray jArray;
    private static final String TAG = "ConfigFragment";
    private Spinner spinner;
    private Bundle bundle;
    private String userID;
    private String sessionID;
    ArrayList<House> houseList;
    ArrayList<Board> boardList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.config_layout,container,false);

        bundle = new Bundle();
        //Receive argument bundle from Home Activity
        userID = getArguments().getString("Username");
        sessionID = getArguments().getString("SessionToken");
        fm = getActivity().getSupportFragmentManager();
        spinner = (Spinner)v.findViewById(R.id.spinner2);
        //Log.d(TAG, "Session Token: " + sessionID);

        /*bundle.putString("SessionToken", sessionID);
        ((Button)v.findViewById(R.id.button_add_board)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBoardDialogFragment f = AddBoardDialogFragment.newInstance();
                f.setArguments(bundle);
                f.show(fm, null);
            }
        });
        ((Button)v.findViewById(R.id.button_edit_peripheral)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPeripheralDialogFragment f = AddPeripheralDialogFragment.newInstance();
                f.setArguments(bundle);
                f.show(fm, null);

            }
        });*/
        return v;
    }
    //TODO: Expandable List view of Houses->Boards->Peripheral types->Peripheral Names
    public void onViewCreated(View view, Bundle savedInstanceState) {
        additem();
    }

    public void additem(){
        List<String> list1 = new ArrayList<String>();
        new getlist1().execute();
        try {
            sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (houses!=null) {
            int o=0;
            for(;o<houses.length();o++){
                try {
                    list1.add(houses.getJSONObject(o).getString("HouseName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ArrayAdapter<String> dataAdapter;
            dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item,list1);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
        }else{
            Log.d(TAG,"null");
        }
    }

    private ArrayList<Board> getBoardList(String houseName, String sessionToken){
        ArrayList<Board> list = new ArrayList<>();
        String name;

        try {
            String response = new TaskHandler().retrieveBoards(getContext(), houseName, sessionToken);
            JSONObject respObject = new JSONObject(response);
            JSONArray respArray = respObject.getJSONArray("");

            for(int i = 0; i < respArray.length(); i++){
                JSONObject curr = respArray.getJSONObject(i);
                name = curr.getString("BoardName");
                Log.d(TAG, "Name: "+ name);
                list.add(i, new Board(name, houseName));
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
        return list;
    }
    private class getlist1 extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String session = sessionID;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("sessionToken", session);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            JSONArray ii=null;
            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/house/listhouses", jsonObject);

            if(resp != null){
                Log.d(TAG, "Join House: " + resp);
                try {
                    ii = new JSONArray(resp);
                    houses=ii.getJSONArray(0);
                    Log.d(TAG,"Resp: "+houses);

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
}
