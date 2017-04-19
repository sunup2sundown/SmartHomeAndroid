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
 * Created by Jhang Myong Ja on 4/15/2017.
 */

public class RenamePeripheralDialogFragment extends DialogFragment {
    private final String TAG = "RenameBoardDialogFragment";
    /* The activity that creates instance of dialog fragment must implement
    *  this interface in order to recieve event callbacks
     */
    private String sessionID;
    private String peripheralName;

    public static RenamePeripheralDialogFragment newInstance(String peripheralName) {
        RenamePeripheralDialogFragment frag = new RenamePeripheralDialogFragment();
        Bundle args = new Bundle();
        frag.peripheralName=peripheralName;
        args.putString("title", "Rename Peripheral" + peripheralName);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        sessionID = getArguments().getString("SessionToken");

        //Builder Class for dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Get Layout inflater for custom layout
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        //Inflate dialog with custom layout
        //null for parent view as in dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_rename_board, null))
                .setPositiveButton("Rename", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        String houseName = ((EditText)RenamePeripheralDialogFragment.this.getDialog()
                                .findViewById(R.id.dialog_house_name)).toString();
                        String oldName = peripheralName;
                        String newName = ((EditText)RenamePeripheralDialogFragment.this.getDialog()
                                .findViewById(R.id.dialog_new_peripheral_name)).toString();
                        renamePeripheral(houseName, oldName, newName);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        RenamePeripheralDialogFragment.this.getDialog().cancel();
                    }
                });
        //Create Dialog object and return it
        return builder.create();
    }

    private void renamePeripheral(String houseName, String oldName, String newName){
        if(true){
            new TaskHandler().renameBoard(getContext(), houseName, oldName, newName, sessionID);
        }
    }
}
