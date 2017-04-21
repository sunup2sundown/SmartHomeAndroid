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

public class ChangeHousePasswordDialogFragment extends DialogFragment {
    private final String TAG = "ChangeHousePasswordDialog";

    private String sessionID;

    public static ChangeHousePasswordDialogFragment newInstance() {
        ChangeHousePasswordDialogFragment frag = new ChangeHousePasswordDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Change House Password");
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
        builder.setView(inflater.inflate(R.layout.dialog_change_house_password, null))
                // Add action buttons
                .setTitle(title)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText houseName = (EditText) ChangeHousePasswordDialogFragment.this.getDialog().findViewById(R.id.change_house_password_dialog_name);
                        EditText oldHousePassword = (EditText) ChangeHousePasswordDialogFragment.this.getDialog().findViewById(R.id.change_house_password_dialog_password);
                        EditText newHousePassword = (EditText) ChangeHousePasswordDialogFragment.this.getDialog().findViewById(R.id.change_house_password_dialog_new_password);
                        ChangeHouse(houseName.getText().toString(), oldHousePassword.getText().toString(), newHousePassword.getText().toString());
                        // switch to the new house...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ChangeHousePasswordDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void ChangeHouse(String oldPassword, String password, String newPassword){
        if(true){
            new TaskHandler().changeHousePassword(getContext(), oldPassword, password, newPassword, sessionID);
        }
    }
}