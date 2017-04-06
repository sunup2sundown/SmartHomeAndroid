package edu.temple.m.smarthomedroid;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.temple.m.smarthomedroid.Dialogs.ChangePasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.NewHouseDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.SwitchHouseDialogFragment;

/**
 * Created by M on 3/16/2017.
 */

public class UserSettingsFragment extends Fragment{

    FragmentManager fm;
    OnFragment4AttachedListener activity;
    //String userid;
    public UserSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_settings,container,false);
        TextView username = (TextView) v.findViewById(R.id.tv_username);
        //userid = getArguments().getString("userid");
        //username.setText(userid);

        fm = getFragmentManager();

        ((Button)v.findViewById(R.id.btn_changepw)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordDialogFragment f = new ChangePasswordDialogFragment();
                f.show(fm, null);
            }
        });
        ((Button)v.findViewById(R.id.btn_newhouse)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewHouseDialogFragment f = new NewHouseDialogFragment();
                f.show(fm, null);
            }
        });
        ((Button)v.findViewById(R.id.btn_switchhouse)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchHouseDialogFragment f = SwitchHouseDialogFragment.newInstance();
                f.show(fm, null);
            }
        });
        return v;
    }

    public void onAttach(Context actv){
        super.onAttach(actv);
        //activity = (OnFragment4AttachedListener)actv;
        //userid = activity.getUsername();
    }

    public interface OnFragment4AttachedListener {
        String getUsername();
    }

}

