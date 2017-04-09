package edu.temple.m.smarthomedroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.temple.m.smarthomedroid.R;

/**
 * Created by Jhang Myong Ja on 4/8/2017.
 */

public class HouseOptionsDialogFragment extends DialogFragment {
    public static HouseOptionsDialogFragment newInstance(String houseName) {
        HouseOptionsDialogFragment frag = new HouseOptionsDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", houseName);
        frag.setArguments(args);
        return frag;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentManager fm = getFragmentManager();
        View v = inflater.inflate(R.layout.dialog_house, container, false);
        ((Button)v.findViewById(R.id.button_change_house_name))
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeHouseNameDialogFragment f = new ChangeHouseNameDialogFragment();
                f.show(fm, null);
            }
        });
        ((Button)v.findViewById(R.id.button_change_house_pw))
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeHousePasswordDialogFragment f = new ChangeHousePasswordDialogFragment();
                f.show(fm, null);
            }
        });
        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        String title = getArguments().getString("title");
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_house, null))
                // Add action buttons
                .setTitle(title)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        HouseOptionsDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
