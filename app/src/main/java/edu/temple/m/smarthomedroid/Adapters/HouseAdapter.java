package edu.temple.m.smarthomedroid.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Objects.House;
import edu.temple.m.smarthomedroid.R;

/**
 * Created by M on 4/3/2017.
 */

public class HouseAdapter extends BaseExpandableListAdapter  {
    private OnHouseAdapterItemClickListener activity;
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
    public HouseAdapter(Context context, ArrayList<House> houses){
        this.houseList = houses;
        try{
            //Instantiate listener so events can be sent to host
            this.activity = (OnHouseAdapterItemClickListener) context;
        } catch(ClassCastException e){
            //Activity doesn't implement
            throw new ClassCastException("activity must implement OnHouseAdapterItemClickListener");
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

        if(convertView == null){
            convertView = LayoutInflater.from((Activity)activity).inflate(R.layout.item_house_details, parent, false);
        }

        ((TextView)convertView.findViewById(R.id.detail_rename)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onHouseAdapterItemRenameClick(house.getName());
            }
        });
        ((TextView)convertView.findViewById(R.id.detail_change_pw)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onHouseAdapterItemChangePasswordClick(house.getName());
            }
        });


        return convertView;
    }

    public interface OnHouseAdapterItemClickListener{
        void onHouseAdapterItemRenameClick(String houseName);
        void onHouseAdapterItemChangePasswordClick(String houseName);
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
    public int getGroupCount(){
        return houseList.size();
    }

}
