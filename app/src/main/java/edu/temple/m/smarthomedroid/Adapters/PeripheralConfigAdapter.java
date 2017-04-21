package edu.temple.m.smarthomedroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Objects.Peripheral;
import edu.temple.m.smarthomedroid.R;

/**
 * Created by quido on 4/19/17.
 */

public class PeripheralConfigAdapter extends BaseExpandableListAdapter{
    private Context context;
    private String session;
    ArrayList<Peripheral> header;
    public PeripheralConfigAdapter(Context context, ArrayList<Peripheral> header, String session){
        this.context = context;
        this.header=header;
        this.session=session;
    }
    @Override
    public int getGroupCount() {
        return header.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 2;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return header.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Peripheral peri = (Peripheral)getGroup(groupPosition);
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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_peripherals, null);
        }
        Button edit = (Button)convertView.findViewById(R.id.edit_peripheral);
        Button remove = (Button)convertView.findViewById(R.id.remove_peripheral);

        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
