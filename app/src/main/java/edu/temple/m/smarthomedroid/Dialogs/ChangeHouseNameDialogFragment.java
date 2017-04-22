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

public class ChangeHouseNameDialogFragment extends DialogFragment {
    private final String TAG = "ChangeHouseDialog";

    private String sessionID;

    public static ChangeHouseNameDialogFragment newInstance() {
        ChangeHouseNameDialogFragment frag = new ChangeHouseNameDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Change House Name");
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
        builder.setView(inflater.inflate(R.layout.dialog_change_housename, null))
                // Add action buttons
                .setTitle(title)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText oldHouseName = (EditText) ChangeHouseNameDialogFragment.this.getDialog().findViewById(R.id.change_house_name_dialog_old_name);
                        EditText housePassword = (EditText) ChangeHouseNameDialogFragment.this.getDialog().findViewById(R.id.change_house_name_dialog_password);
                        EditText newHouseName = (EditText) ChangeHouseNameDialogFragment.this.getDialog().findViewById(R.id.change_house_name_dialog_new_name);
                        ChangeHouse(oldHouseName.getText().toString(), housePassword.getText().toString(), newHouseName.getText().toString());
                        // switch to the new house...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ChangeHouseNameDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void ChangeHouse(String oldName, String password, String newName){
        if(true){
            String result;
            result = new TaskHandler().changeHouseName(getContext(), oldName, password, newName, sessionID);
        }
    }
}
