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

public class ChangeHousePasswordDialogFragment extends DialogFragment {
    private final String TAG = "ChangeHousePasswordDialog";
    private String houseName;
    private String sessionID;
    private Listener mListener;
    public interface Listener{
        void onChangeHousePasswordDialogPositiveClick(String housename, DialogFragment d, String sessionToken);
    }

    public static ChangeHousePasswordDialogFragment newInstance(String houseName, String sessionToken) {
        ChangeHousePasswordDialogFragment frag = new ChangeHousePasswordDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Change Password of \"" + houseName + "\"");
        args.putString("SessionToken", sessionToken);
        args.putString("HouseName", houseName);
        frag.setArguments(args);
        return frag;
    }

    //Override the Fragment.onAttach method to instantiate listener
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        //Verify that the host activity implements the callback interface
        try{
            //Instantiate listener so events can be sent to host
            mListener = (ChangeHousePasswordDialogFragment.Listener) activity;
        } catch(ClassCastException e){
            //Activity doesn't implement
            throw new ClassCastException(activity.toString() + " must implement LoginDialogListener");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        houseName = getArguments().getString("HouseName");
        sessionID = getArguments().getString("SessionToken");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        String title = getArguments().getString("title");
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_change_house_password, null))
                // Add action buttons
                .setTitle(title)
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener
                                .onChangeHousePasswordDialogPositiveClick
                                        (houseName, ChangeHousePasswordDialogFragment.this, sessionID);
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