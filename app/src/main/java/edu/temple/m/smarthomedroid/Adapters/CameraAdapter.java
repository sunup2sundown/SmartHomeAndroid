package edu.temple.m.smarthomedroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Handlers.TaskHandler;
import edu.temple.m.smarthomedroid.Objects.Camera;
import edu.temple.m.smarthomedroid.R;

/**
 * Created by M on 4/23/2017.
 */

public class CameraAdapter extends ArrayAdapter<Camera> {
    private Context context;
    private boolean useList = true;
    String sessionToken;

    /**
     * Constructor Method
     * @param context
     * @param cameras
     */
    public CameraAdapter(Context context, ArrayList<Camera> cameras, String sessionToken){
        super(context, android.R.layout.simple_list_item_1, cameras);
        this.sessionToken = sessionToken;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Camera camera = (Camera)getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_camera, parent, false);
        }

        TextView tvName = (TextView)convertView.findViewById(R.id.item_camera_name);

        tvName.setText(camera.getName());

        return convertView;
    }

}