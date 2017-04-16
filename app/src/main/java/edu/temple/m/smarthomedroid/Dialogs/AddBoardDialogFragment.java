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

public class AddBoardDialogFragment extends DialogFragment {
    private String sessionID;

    public static AddBoardDialogFragment newInstance() {
        AddBoardDialogFragment frag = new AddBoardDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Add New Board");
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
        builder.setView(inflater.inflate(R.layout.dialog_add_board, null))
                // Add action buttons
                .setTitle(title)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String houseName =
                                ((EditText) AddBoardDialogFragment.this.getDialog().findViewById(R.id.dialog_house_name)).toString();
                        String boardName =
                                ((EditText) AddBoardDialogFragment.this.getDialog().findViewById(R.id.dialog_board_name)).toString();
                        String boardSerialNo =
                                ((EditText) AddBoardDialogFragment.this.getDialog().findViewById(R.id.dialog_board_serial_no)).toString();
                        newBoard(houseName, boardName, boardSerialNo);
                        // switch to the new house...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddBoardDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void newBoard(String houseName, String boardName, String boardSerialNo){
        if(true){
            new TaskHandler().addBoard("ConfigFragment:", houseName, boardName, boardSerialNo, sessionID);
        }
    }
}
