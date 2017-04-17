package edu.temple.m.smarthomedroid.Adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Handlers.TaskHandler;
import edu.temple.m.smarthomedroid.Objects.Board;
import edu.temple.m.smarthomedroid.Objects.House;
import edu.temple.m.smarthomedroid.Objects.Peripheral;
import edu.temple.m.smarthomedroid.Objects.Relay;
import edu.temple.m.smarthomedroid.Objects.Sensor;
import edu.temple.m.smarthomedroid.R;

/**
 * Created by Jhang Myong Ja on 4/14/2017.
 */

public class BoardAdapter extends BaseExpandableListAdapter {
    private ArrayList<House> houseList;
    private final String TAG = "BoardAdapter";
    private Context activity;
    private String sessionToken;
    public boolean hasStableIds(){
        return false;
    }

    public Object getChild(int groupPosition, int childPosition){
        return null;
    }

    public long getChildId(int groupPosition, int childPosition){
        return 0;
    }

    public long getGroupId(int groupPosition){
        return 0;
    }

    public int getChildrenCount(int groupPosition){
        return 1;
    }

    public Object getGroup(int groupId){
        return houseList.get(groupId);
    }
    /**
     * Constructor Method
     * @param context
     * @param houses
     */
    public BoardAdapter(Context context, ArrayList<House> houses, String sessionToken){
        this.activity = context;
        this.houseList = houses;
        this.sessionToken = sessionToken;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
        final House house = (House)getGroup(groupPosition);

        if(convertView == null){
            convertView = LayoutInflater.from((Activity)activity).inflate(R.layout.item_house, parent, false);
        }

        TextView tvName = (TextView)convertView.findViewById(R.id.item_house_name);

        tvName.setText(house.getName());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent){
        final House house = (House)getGroup(groupPosition);
        // retrieve boards by calling getBoards in listener
        ArrayList<Board> boardList = getBoards(house.getName());
        if(convertView == null){
            convertView = LayoutInflater.from((Activity)activity).inflate(R.layout.item_board, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.item_board_id);
        final Board board = (Board) boardList.get(childPosition);
        tv.setText(board.getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // remove board from list on long click
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                removeBoard(house, board);
                return true;
            }
        });
        return convertView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public int getGroupCount(){
        return houseList.size();
    }
    private ArrayList<Board> getBoards(String houseName){

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

    private void removeBoard(House h, Board b){
        new TaskHandler().removeBoard(TAG, h.getName(), b.getName(), sessionToken);
    }
}