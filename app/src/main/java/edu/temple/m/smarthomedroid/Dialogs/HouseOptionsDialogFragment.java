package edu.temple.m.smarthomedroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.temple.m.smarthomedroid.Handlers.TaskHandler;
import edu.temple.m.smarthomedroid.R;

/**
 * Created by DongBinChoi on 4/28/2017.
 */

public class HouseOptionsDialogFragment extends DialogFragment{
    private String sessionID;
    private String houseName;

    public static HouseOptionsDialogFragment newInstance(String houseName, String sessionToken) {
        HouseOptionsDialogFragment frag = new HouseOptionsDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Options for House " + houseName);
        args.putString("HouseName", houseName);
        args.putString("SessionToken", sessionToken);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        houseName = getArguments().getString("HouseName");
        sessionID = getArguments().getString("SessionToken");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        String title = getArguments().getString("title");
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_house_options, null);
        ((Button)view.findViewById(R.id.button_change_name)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeHouseNameDialogFragment f = ChangeHouseNameDialogFragment.newInstance(houseName, sessionID);
                f.show(getFragmentManager(), null);
                HouseOptionsDialogFragment.this.getDialog().cancel();
            }
        });
        ((Button)view.findViewById(R.id.button_change_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeHousePasswordDialogFragment f = ChangeHousePasswordDialogFragment.newInstance(houseName, sessionID);
                f.show(getFragmentManager(), null);
                HouseOptionsDialogFragment.this.getDialog().cancel();
            }
        });
        ((Button)view.findViewById(R.id.button_leave_house)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeaveHouse(houseName);
                HouseOptionsDialogFragment.this.getDialog().cancel();
            }
        });
        builder.setView(view)
                // Add action buttons
                .setTitle(title)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        HouseOptionsDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void LeaveHouse(String name){
        if(true){
            new TaskHandler().leaveHouse(getActivity(), sessionID, name);
        }
    }

}
