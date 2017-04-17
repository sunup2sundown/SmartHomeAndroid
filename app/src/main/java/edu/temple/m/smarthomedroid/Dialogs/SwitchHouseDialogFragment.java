package edu.temple.m.smarthomedroid.Dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

import edu.temple.m.smarthomedroid.R;

public class SwitchHouseDialogFragment extends DialogFragment {
    SwitchHouseDialogListener activity;
    String sessionToken;
    private final String TAG = "SwitchHouse";
    public static SwitchHouseDialogFragment newInstance() {
        SwitchHouseDialogFragment frag = new SwitchHouseDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Join House");
        frag.setArguments(args);
        return frag;
    }

    public void onAttach(Context context){
        super.onAttach(context);
        activity = (SwitchHouseDialogListener) context;
        sessionToken = getArguments().getString("SessionToken");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        String title = getArguments().getString("title");
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_switchhouse, null))
                // Add action buttons
                .setTitle(title)
                .setPositiveButton("Join", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String newHouseName = ((EditText) SwitchHouseDialogFragment.this.getDialog().findViewById(R.id.join_house_name)).toString();
                        String housePassword = ((EditText) SwitchHouseDialogFragment.this.getDialog().findViewById(R.id.join_house_password)).toString();
                        Log.d(TAG, newHouseName + ", " + housePassword);
                        activity.onSwitchHouseDialogPositiveClick(newHouseName, housePassword, sessionToken);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SwitchHouseDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public interface SwitchHouseDialogListener {
        void onSwitchHouseDialogPositiveClick(String newHouseName, String housePassword, String sessionToken);
    }
}