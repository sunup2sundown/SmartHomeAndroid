package edu.temple.m.smarthomedroid.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

import edu.temple.m.smarthomedroid.R;

/**
 * Created by Matthew White on 3/17/2017.
 */

public class LoginDialogFragment extends DialogFragment {

    private final String TAG = "Login Dialog";
    private EditText username, password;
    LoginDialogListener mListener;

    public interface LoginDialogListener{
        public void onLoginDialogPositiveClick(DialogFragment dialog);
        public void onLoginDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog);
    }
    //Override the Fragment.onAttach method to instantiate listener
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        //Verify that the host activity implements the callback interface
        try{
            //Instantiate listener so events can be sent to host
            mListener = (LoginDialogFragment.LoginDialogListener) activity;
        } catch(ClassCastException e){
            //Activity doesn't implement
            throw new ClassCastException(activity.toString() + " must implement LoginDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Builder Class for dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Get a Layout Inflater for custom layout
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //Inflate Dialog with custom layout
        //null as parent view because its in dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_login, null))
                //Add Action Buttons
                .setPositiveButton("Log in", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        mListener.onLoginDialogPositiveClick(LoginDialogFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        mListener.onLoginDialogNegativeClick(LoginDialogFragment.this);
                    }
                });
        //Create Dialog object and return it
        return builder.create();
    }
}
