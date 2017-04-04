package edu.temple.m.smarthomedroid.Dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import edu.temple.m.smarthomedroid.R;


public class NewHouseDialogFragment extends DialogFragment {
    public static NewHouseDialogFragment newInstance() {
        NewHouseDialogFragment frag = new NewHouseDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Create New House");
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
}