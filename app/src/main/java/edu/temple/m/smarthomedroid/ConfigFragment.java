package edu.temple.m.smarthomedroid;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Dialogs.AddBoardDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.AddPeripheralDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeUserPasswordDialogFragment;
import edu.temple.m.smarthomedroid.Objects.Board;
import edu.temple.m.smarthomedroid.Objects.House;

/**
 * Created by M on 3/16/2017.
 */

public class ConfigFragment extends Fragment{
    FragmentManager fm;

    private static final String TAG = "ConfigFragment";

    private Bundle bundle;

    private String userID;
    private String sessionID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.config_layout,container,false);

        bundle = new Bundle();
        //Receive argument bundle from Home Activity
        userID = getArguments().getString("Username");
        sessionID = getArguments().getString("SessionToken");

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

        ExpandableListView elv = (ExpandableListView)getView().findViewById(R.id.elv_peripherals);
    }
    public interface ConfigFragmentListener{
         ArrayList<Board> retrieveBoards(House house);

    }
}
