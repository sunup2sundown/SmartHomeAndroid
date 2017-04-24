package edu.temple.m.smarthomedroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.temple.m.smarthomedroid.Objects.Board;
import edu.temple.m.smarthomedroid.Objects.Peripheral;
import edu.temple.m.smarthomedroid.R;

/**
 * Created by quido on 4/19/17.
 */

public class PeripheralConfigAdapter extends BaseExpandableListAdapter{
    private Context context;
    private String session;
    ArrayList<Board> header;
    private HashMap<Board, List<Peripheral>> children;
    public PeripheralConfigAdapter(Context context, ArrayList<Board> header,HashMap<Board, List<Peripheral>>children, String session){
        this.context = context;
        this.header=header;
        this.children=children;
        this.session=session;
    }
    @Override
    public int getGroupCount() {
        return header.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (children!=null){
            return this.children.get(this.header.get(groupPosition))
                    .size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return header.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.children.get((this.header.get(groupPosition))).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Board boa = (Board)getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.board_config, null);
        }
        TextView boardname = (TextView)convertView.findViewById(R.id.boa_name);
        TextView boardquantity = (TextView)convertView.findViewById(R.id.quantity);
        boardname.setText(boa.getName());
        if(getChildrenCount(groupPosition)!=0){
            boardquantity.setText((Integer.toString(getChildrenCount(groupPosition))));
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Peripheral peri = this.children.get(this.header.get(groupPosition)).get(childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.elv_peripherals, null);
        }
        TextView periname = (TextView)convertView.findViewById(R.id.peripheral_name_config);
        periname.setText(peri.getName());
        TextView peritype = (TextView)convertView.findViewById(R.id.peripheral_type_config);
        peritype.setText(peri.getType());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
