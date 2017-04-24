package edu.temple.m.smarthomedroid;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.temple.m.smarthomedroid.Adapters.GridAdapter;
import edu.temple.m.smarthomedroid.Adapters.PeripheralConfigAdapter;
import edu.temple.m.smarthomedroid.Dialogs.AddBoardDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.AddPeripheralDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeBoardNameDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.RemoveBoardDialog;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Handlers.TaskHandler;
import edu.temple.m.smarthomedroid.Objects.Board;
import edu.temple.m.smarthomedroid.Objects.DataHolder;
import edu.temple.m.smarthomedroid.Objects.House;
import edu.temple.m.smarthomedroid.Objects.Peripheral;
import edu.temple.m.smarthomedroid.Objects.Relay2;

import static java.lang.Thread.sleep;

/**
 * Created by M on 3/16/2017.
 */

public class ConfigFragment extends Fragment {
    FragmentManager fm;
    private ExpandableListView lv;
    private JSONArray houses,peri,boards;
    private String sessionToken = "";
    private static final String TAG = "ConfigFragment";
    private Spinner spinner;
    private Bundle bundle;
    private String userID,sessionID,housename;
    public static DataHolder dat = new DataHolder();
    ArrayList<House> houseList;
    ArrayList<Board> boardlist;
    HashMap<Board,List<Peripheral>> perilist;
    private PeripheralConfigAdapter rAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentManager dialogManager;
        final View v = inflater.inflate(R.layout.config_layout,container,false);

        bundle = new Bundle();
        //Receive argument bundle from Home Activity
        userID = getArguments().getString("Username");
        sessionID = getArguments().getString("SessionToken");
        fm = getActivity().getSupportFragmentManager();
        spinner = (Spinner)v.findViewById(R.id.spinner2);
        return v;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        additem();
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        perilist = new HashMap<Board, List<Peripheral>>();
                        boardlist = new ArrayList<Board>();
                        Object item = parent.getItemAtPosition(pos);
                        housename = item.toString();
                        Log.d(TAG,housename);
                        //mCallback.passData(housename);
                        new getboard().execute();
                        try {
                            sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (boards!=null){
                            Log.d(TAG,"get board: "+boards);
                            int o=0;
                            for(;o<boards.length();o++){
                                try {
                                    JSONObject check = boards.getJSONObject(o);
                                    String board = check.getString("BoardName");
                                    boardlist.add(new Board(board,housename,sessionID));
                                    Log.d(TAG,"boardlist : "+boardlist);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            new getperi().execute();
                            try {
                                sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if(peri!=null){
                                int l=0;
                                for(;l<boardlist.size();l++){
                                    List<Peripheral> peri_list= new ArrayList<Peripheral>();
                                    int k=0;
                                    for(;k<peri.length();k++){
                                        try {
                                            JSONObject check = peri.getJSONObject(k);
                                            if(check.getString("BoardName").equals(boardlist.get(l).getName())){
                                                peri_list.add(new Peripheral(check.getString("PeripheralName"),housename,sessionID,check.getString("PeripheralTypeName")));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    Log.d(TAG,"peri: "+peri_list.toString());
                                    perilist.put(boardlist.get(l),peri_list);
                                }
                            }
                            if(!boardlist.isEmpty()){
                                if(perilist==null){
                                    Log.d(TAG,"why nulllllllllllll?????");
                                }else{
                                    Log.d(TAG,"not nulllllllllllll");
                                }
                                rAdapter = new PeripheralConfigAdapter(getActivity().getApplicationContext(), boardlist,perilist,sessionID);
                                lv = (ExpandableListView)getView().findViewById(R.id.elv_peripherals);
                                lv.setAdapter(rAdapter);
                                lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                                    @Override
                                    public boolean onItemLongClick( AdapterView<?> parent, View view, int position, long id) {

                                        long packedPosition = lv.getExpandableListPosition(position);

                                        int itemType = ExpandableListView.getPackedPositionType(packedPosition);
                                        int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                                        int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);
        /*  if group item clicked */
                                        if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                                            String old = boardlist.get(groupPosition).getName();
                                            onBoard(old, groupPosition);
                                        }
        /*  if child item clicked */
                                        else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                                            onPeri();
                                        }
                                        return false;
                                    }
                                });
                            }
                        }
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

    }
    public void onBoard(String old, final int groupPosition){
        CharSequence options[] = new CharSequence[] {"Change Board Name", "Add Peripheral", "Remove Board"};
        final Bundle bundleboard = new Bundle();
        bundleboard.putString("OldBoard",old);
        bundleboard.putString("SessionToken",sessionID);
        bundleboard.putString("HouseName",housename);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Options");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        FragmentManager dialogManager;
                        dialogManager = getActivity().getSupportFragmentManager();
                        ChangeBoardNameDialogFragment f = new ChangeBoardNameDialogFragment();
                        f.setArguments(bundleboard);
                        f.show(dialogManager,null);
                        int i = dat.gettoggle();
                        if(i==1){
                            String n = dat.getDistributor_id();
                            boardlist.get(groupPosition).setname(n);
                        }
                        break;
                    case 1:
                        FragmentManager dialogManager2;
                        dialogManager2 = getActivity().getSupportFragmentManager();
                        AddPeripheralDialogFragment fra = new AddPeripheralDialogFragment();
                        fra.setArguments(bundleboard);
                        fra.show(dialogManager2,null);
                        break;
                    case 2:
                        FragmentManager dialogManager3;
                        dialogManager3 = getActivity().getSupportFragmentManager();
                        RemoveBoardDialog fr = new RemoveBoardDialog();
                        fr.setArguments(bundleboard);
                        fr.show(dialogManager3,null);
                        break;
                    default:
                }
            }
        }).setNegativeButton("Back", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        AlertDialog adialog = builder.show();
    }
    public void onPeri(){
        CharSequence options[] = new CharSequence[] {"Rename Peripheral", "Remove Peripheral"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Options");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
            }
        }).setNegativeButton("Back", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        AlertDialog adialog = builder.show();
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
            dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.item_spinner,list1);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
        }else{
            Log.d(TAG,"null");
        }
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
    //GET BOARD
private class getboard extends AsyncTask<Void, Void, Void> {
    JSONObject jsonObject = new JSONObject();
    String session = sessionID;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try{
            jsonObject.put("SessionToken", session)
                    .put("HouseName",housename);
        } catch(JSONException e){
            Log.e(TAG, "JSONException: " + e.getMessage());
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpHandler sh = new HttpHandler();
        JSONArray ii=null;
        //Make a request to url and get response
        String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/house/getboardsbyhouse", jsonObject);
        if(resp != null){
            Log.d(TAG, "Get Board: " + resp);
            try {
                ii = new JSONArray(resp);
                boards=ii.getJSONArray(0);
                Log.d(TAG,"Resp: "+boards);

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

    //GET repipherals
    private class getperi extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String session = sessionID;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
                jsonObject.put("sessionToken", session)
                    .put("houseName",housename);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            JSONArray ii=null;
            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/peripheral/getcurrentperipheralsbyhouse", jsonObject);
            if(resp != null){
                Log.d(TAG, "Get peripherals: " + resp);
                try {
                    ii = new JSONArray(resp);
                    peri=ii.getJSONArray(0);
                    Log.d("peri =", peri.toString());
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
