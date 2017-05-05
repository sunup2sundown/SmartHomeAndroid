package edu.temple.m.smarthomedroid.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

import edu.temple.m.smarthomedroid.Handlers.TaskHandler;
import edu.temple.m.smarthomedroid.NotifySettingsFragmentListener;
import edu.temple.m.smarthomedroid.R;

import static edu.temple.m.smarthomedroid.UserSettingsFragment.mpass;

/**
 * Created by M on 4/9/2017.
 */

public class ChangeHouseNameDialogFragment extends DialogFragment {
    private final String TAG = "ChangeHouseDialog";
    private int i;
    private String sessionID;
    private String houseName;
    private NotifySettingsFragmentListener mListener;
    public void onAttach(Context context){
        super.onAttach(context);
        mListener = (NotifySettingsFragmentListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog);
    }
    public static ChangeHouseNameDialogFragment newInstance(int index, String houseName, String sessionToken) {
        ChangeHouseNameDialogFragment frag = new ChangeHouseNameDialogFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putString("title", "Rename House \"" + houseName + "\"");
        args.putString("SessionToken", sessionToken);
        args.putString("HouseName", houseName);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        houseName = getArguments().getString("HouseName");
        sessionID = getArguments().getString("SessionToken");
        i = getArguments().getInt("Index");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        String title = getArguments().getString("title");
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_change_housename, null))
                // Add action buttons
                .setTitle(title)
                .setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText housePassword = (EditText) ChangeHouseNameDialogFragment.this.getDialog().findViewById(R.id.change_house_name_dialog_password);
                        EditText newHouseName = (EditText) ChangeHouseNameDialogFragment.this.getDialog().findViewById(R.id.change_house_name_dialog_new_name);
                        changeHouseName(houseName, housePassword.getText().toString(), newHouseName.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ChangeHouseNameDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void changeHouseName(String oldName, String password, String newName){
        if(new TaskHandler().changeHouseName(getContext(), oldName, password, newName, sessionID)){
            mpass.msg("House's Name Changed Successfully!");
            mListener.updateSettingsFragment();
        }else{
            mpass.msg("Failed, please try again!");
        }
    }
}
