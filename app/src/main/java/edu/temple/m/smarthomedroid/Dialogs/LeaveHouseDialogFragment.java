package edu.temple.m.smarthomedroid.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

import edu.temple.m.smarthomedroid.Handlers.TaskHandler;
import edu.temple.m.smarthomedroid.R;

/**
 * Created by M on 4/9/2017.
 */

public class LeaveHouseDialogFragment extends DialogFragment {
    private final String TAG = "LeaveHouseDialog";

    private String sessionID;

    public static LeaveHouseDialogFragment newInstance() {
        LeaveHouseDialogFragment frag = new LeaveHouseDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Leave House");
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        sessionID = getArguments().getString("SessionToken");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        String title = getArguments().getString("title");
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_leave_house, null))
                // Add action buttons
                .setTitle(title)
                .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText houseName = (EditText) LeaveHouseDialogFragment.this.getDialog().findViewById(R.id.dialog_leave_house_name);
                        LeaveHouse(houseName.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LeaveHouseDialogFragment.this.getDialog().cancel();
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