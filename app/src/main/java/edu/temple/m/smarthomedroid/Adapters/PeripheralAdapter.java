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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Objects.Board;
import edu.temple.m.smarthomedroid.Objects.House;
import edu.temple.m.smarthomedroid.R;

/**
 * Created by Jhang Myong Ja on 4/14/2017.
 */

public class PeripheralAdapter extends BaseExpandableListAdapter {
    private OnPeripheralAdapterItemClickListener activity;
    private ArrayList<House> houseList;


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
    public PeripheralAdapter(Context context, ArrayList<House> houses){
        this.houseList = houses;
        try{
            //Instantiate listener so events can be sent to host
            this.activity = (OnPeripheralAdapterItemClickListener) context;
        } catch(ClassCastException e){
            //Activity doesn't implement
            throw new ClassCastException("activity must implement OnPeripheralAdapterItemClickListener");
        }
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
        ArrayList<Board> boardList = activity.getBoards(house);

        if(convertView == null){
            convertView = LayoutInflater.from((Activity)activity).inflate(R.layout.item_board, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.item_board_id);
        tv.setText(boardList.get(childPosition).getId());
        return convertView;
    }

    public interface OnPeripheralAdapterItemClickListener{
        ArrayList<Board> getBoards(House house);
        void onBoardItemClick(String houseName);
        void onPeripheralItemClick(String houseName);
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public int getGroupCount(){
        return houseList.size();
    }
}