package edu.temple.m.smarthomedroid.Dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

import edu.temple.m.smarthomedroid.Handlers.TaskHandler;
import edu.temple.m.smarthomedroid.R;


public class NewHouseDialogFragment extends DialogFragment {

    private String sessionID;

    public static NewHouseDialogFragment newInstance() {
        NewHouseDialogFragment frag = new NewHouseDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Create New House");
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
        builder.setView(inflater.inflate(R.layout.dialog_newhouse, null))
                // Add action buttons
                .setTitle(title)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText houseName = (EditText) NewHouseDialogFragment.this.getDialog().findViewById(R.id.dialog_newhouse_name);
                        EditText housePassword = (EditText) NewHouseDialogFragment.this.getDialog().findViewById(R.id.dialog_newhouse_password);
                        createHouse(houseName.getText().toString(), housePassword.getText().toString());
                        // switch to the new house...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewHouseDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void createHouse(String name, String password){
        if(true){
            new TaskHandler().createHouse("SettingsFragment:", name, password, sessionID);
        }
    }
}