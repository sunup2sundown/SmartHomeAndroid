package edu.temple.m.smarthomedroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by M on 3/16/2017.
 */

public class UserSettingsFragment extends Fragment{
    OnFragment4AttachedListener activity;
    String userid;
    public UserSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fourthlayout,container,false);
        TextView username = (TextView) v.findViewById(R.id.text_username);
        userid = getArguments().getString("userid");
        username.setText(userid);
        ((Button)v.findViewById(R.id.button_changepw)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordDialogFragment f = ChangePasswordDialogFragment.newInstance();
                f.show(getFragmentManager(), null);
            }
        });
        ((Button)v.findViewById(R.id.button_newhouse)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewHouseDialogFragment f = NewHouseDialogFragment.newInstance();
                f.show(getFragmentManager(), null);
            }
        });
        ((Button)v.findViewById(R.id.button_switchhouse)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchHouseDialogFragment f = SwitchHouseDialogFragment.newInstance();
                f.show(getFragmentManager(), null);
            }
        });
        return v;
    }

    public void onAttach(Context actv){
        super.onAttach(actv);
        activity = (OnFragment4AttachedListener)actv;
        userid = activity.getUsername();
    }

    public interface OnFragment4AttachedListener {
        String getUsername();
    }

}
