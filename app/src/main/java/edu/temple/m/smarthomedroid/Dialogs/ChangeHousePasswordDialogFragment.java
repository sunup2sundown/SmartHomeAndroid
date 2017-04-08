package edu.temple.m.smarthomedroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import edu.temple.m.smarthomedroid.R;

/**
 * Created by Jhang Myong Ja on 4/8/2017.
 */

public class ChangeHousePasswordDialogFragment extends DialogFragment {
    public static ChangeHousePasswordDialogFragment newInstance() {
        ChangeHousePasswordDialogFragment frag = new ChangeHousePasswordDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "New Password for House");
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
        builder.setView(inflater.inflate(R.layout.dialog_change_house_pw, null))
                // Add action buttons
                .setTitle(title)
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // rename house
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ChangeHousePasswordDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
