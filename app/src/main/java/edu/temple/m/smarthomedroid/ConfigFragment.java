package edu.temple.m.smarthomedroid;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Adapters.BoardAdapter;
import edu.temple.m.smarthomedroid.Dialogs.AddBoardDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.AddPeripheralDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeUserPasswordDialogFragment;
import edu.temple.m.smarthomedroid.Handlers.TaskHandler;
import edu.temple.m.smarthomedroid.Objects.Board;
import edu.temple.m.smarthomedroid.Objects.House;

import static java.lang.Thread.sleep;

/**
 * Created by M on 3/16/2017.
 */

public class ConfigFragment extends Fragment{
    FragmentManager fm;

    private String sessionToken = "";
    private String houseName = "";
    private JSONArray jArray;
    private static final String TAG = "ConfigFragment";

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
        sessionToken = getArguments().getString("SessionToken");

        fm = getActivity().getSupportFragmentManager();
        //Log.d(TAG, "Session Token: " + sessionID);

        bundle.putString("SessionToken", sessionID);
        ((Button)v.findViewById(R.id.button_add_board)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBoardDialogFragment f = AddBoardDialogFragment.newInstance();
                f.setArguments(bundle);
                f.show(fm, null);
            }
        });
        ((Button)v.findViewById(R.id.button_add_peripheral)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPeripheralDialogFragment f = AddPeripheralDialogFragment.newInstance();
                f.setArguments(bundle);
                f.show(fm, null);

            }
        });
        return v;
    }
    //TODO: Expandable List view of Houses->Boards->Peripheral types->Peripheral Names
    public void onViewCreated(View view, Bundle savedInstanceState) {
        houseName = "Hardwick";
        sessionToken = "3CEB721D-BDE8-4CBC-950F-E70568D2A2DE";
        //Construct data source
        houseList = getHouseList(sessionToken);
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ExpandableListView elv = (ExpandableListView)getView().findViewById(R.id.elv_peripherals);
        BoardAdapter ba = new BoardAdapter(getContext(), houseList, sessionToken);
    }

    private ArrayList<House> getHouseList(String sessionToken){
        ArrayList<House> list = new ArrayList<>();
        String name;

        try {
            String response = new TaskHandler().retrieveHouses(TAG, sessionToken);
            JSONObject respObject = new JSONObject(response);
            JSONArray respArray = respObject.getJSONArray("");

            for(int i = 0; i < respArray.length(); i++){
                JSONObject curr = respArray.getJSONObject(i);
                name = curr.getString("HouseName");
                Log.d(TAG, "Name: "+ name);
                list.add(i, new House(name));
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
        return list;
    }

    private ArrayList<Board> getBoardList(String houseName, String sessionToken){
        ArrayList<Board> list = new ArrayList<>();
        String name;

        try {
            String response = new TaskHandler().retrieveBoards(TAG, houseName, sessionToken);
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
}
