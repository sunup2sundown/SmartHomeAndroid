package edu.temple.m.smarthomedroid.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import edu.temple.m.smarthomedroid.R;

/**
 * Created by M on 4/9/2017.
 */

public class ChangeHouseNameDialogFragment extends DialogFragment {
    private final String TAG = "ChangeHouseNameDialog";
    ChangeHouseNameDialogListener mListener;

    /* The activity that creates instance of dialog fragment must implement
    *  this interface in order to recieve event callbacks
     */
    public interface ChangeHouseNameDialogListener{
        public void onChangeHouseNameDialogPositiveClick(DialogFragment dialog);
        public void onChangeHouseNameDialogNegativeClick(DialogFragment dialog);
    }

    //Override the Fragment.onAttach method to instantiate listener
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        //Verify that the host activity implements the callback interface
        try{
            //Instantiate listener so events can be sent to host
            mListener = (ChangeHouseNameDialogListener) activity;
        } catch(ClassCastException e){
            //Activity doesn't implement
            throw new ClassCastException(activity.toString() + " must implement ChangeHouseNameDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Builder Class for dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Get Layout inflater for custom layout
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        //Inflate dialog with custom layout
        //null for parent view as in dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_change_housename, null))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        //Send the positive button event back to host activity
                        mListener.onChangeHouseNameDialogPositiveClick(ChangeHouseNameDialogFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        //send the negative button event back to host activity
                        mListener.onChangeHouseNameDialogNegativeClick(ChangeHouseNameDialogFragment.this);
                    }
                });
        //Create Dialog object and return it
        return builder.create();
    }

    public static ChangeHouseNameDialogFragment newInstance(String houseName) {
        ChangeHouseNameDialogFragment frag = new ChangeHouseNameDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Rename " + houseName);
        frag.setArguments(args);
        return frag;
    }

}
