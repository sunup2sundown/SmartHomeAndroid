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

/**
 * Created by Jhang Myong Ja on 4/15/2017.
 */

public class AddPeripheralDialogFragment extends DialogFragment {

    private String sessionID;

    public static AddPeripheralDialogFragment newInstance() {
        AddPeripheralDialogFragment frag = new AddPeripheralDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Add New Peripheral");
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
        builder.setView(inflater.inflate(R.layout.dialog_add_peripheral, null))
                // Add action buttons
                .setTitle(title)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String houseName =
                                ((EditText) AddPeripheralDialogFragment.this.getDialog().findViewById(R.id.dialog_house_name)).toString();
                        String boardName =
                                ((EditText) AddPeripheralDialogFragment.this.getDialog().findViewById(R.id.dialog_board_name)).toString();
                        String peripheralName =
                                ((EditText) AddPeripheralDialogFragment.this.getDialog().findViewById(R.id.dialog_peripheral_name)).toString();
                        String peripheralModel =
                                ((EditText) AddPeripheralDialogFragment.this.getDialog().findViewById(R.id.dialog_peripheral_model)).toString();
                        String pinConnection =
                                ((EditText) AddPeripheralDialogFragment.this.getDialog().findViewById(R.id.dialog_pin_connection)).toString();
                        newPeripheral(houseName, boardName, peripheralName, peripheralModel, pinConnection);
                        // switch to the new house...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddPeripheralDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void newPeripheral(String houseName, String boardName, String peripheralName, String peripheralModel, String pinConnection){
        if(true){
            new TaskHandler().addPeripheral("ConfigFragment:", houseName, boardName, peripheralName, peripheralModel, pinConnection, sessionID);
        }
    }
}
