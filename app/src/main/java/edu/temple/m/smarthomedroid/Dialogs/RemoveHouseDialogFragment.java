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

public class RemoveHouseDialogFragment extends DialogFragment {
    private final String TAG = "RemoveHouseDialog";

    private String sessionID;

    public static RemoveHouseDialogFragment newInstance() {
        RemoveHouseDialogFragment frag = new RemoveHouseDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Remove House");
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog);
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
        builder.setView(inflater.inflate(R.layout.dialog_remove_house, null))
                // Add action buttons
                .setTitle(title)
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText houseName = (EditText) RemoveHouseDialogFragment.this.getDialog().findViewById(R.id.dialog_remove_house_name);
                        EditText housePassword = (EditText) RemoveHouseDialogFragment.this.getDialog().findViewById(R.id.dialog_remove_house_password);
                        RemoveHouse(houseName.getText().toString(), housePassword.toString());
                        // switch to the new house...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RemoveHouseDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void RemoveHouse(String name, String password){
        if(true){
            new TaskHandler().removeHouse(getContext(), sessionID, name, password);
        }
    }
}