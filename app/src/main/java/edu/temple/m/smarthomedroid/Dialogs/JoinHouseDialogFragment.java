package edu.temple.m.smarthomedroid.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import edu.temple.m.smarthomedroid.Handlers.TaskHandler;
import edu.temple.m.smarthomedroid.R;

/**
 * Created by M on 4/9/2017.
 */

public class JoinHouseDialogFragment extends DialogFragment {
    private final String TAG = "JoinHouseDialog";

    private String sessionID;

    public static JoinHouseDialogFragment newInstance() {
        JoinHouseDialogFragment frag = new JoinHouseDialogFragment();
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
        builder.setView(inflater.inflate(R.layout.dialog_join_house, null))
                // Add action buttons
                .setTitle(title)
                .setPositiveButton("Join", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText houseName = (EditText) JoinHouseDialogFragment.this.getDialog().findViewById(R.id.dialog_join_house_name);
                        EditText housePassword = (EditText) JoinHouseDialogFragment.this.getDialog().findViewById(R.id.dialog_join_house_password);
                        joinHouse(houseName.getText().toString(), housePassword.getText().toString());
                        // switch to the new house...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        JoinHouseDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void joinHouse(String name, String password){
        if(true) {
        }
    }
}
