package edu.temple.m.smarthomedroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.R;

import static edu.temple.m.smarthomedroid.ConfigFragment.dat;
import static java.lang.Thread.sleep;

/**
 * Created by Matthew White on 3/17/2017.
 */

public class RemoveBoardDialog extends DialogFragment {
    private String board,sessionID,housename;
    private final String TAG = "ChangeBoardDialog";
    private EditText username, password;
    //ChangeBoardDialogListener mListener;

    //public interface ChangeBoardDialogListener{
    //    public void onLoginDialogPositiveClick(DialogFragment dialog);
    //    public void onLoginDialogNegativeClick(DialogFragment dialog);
    //}

    //Override the Fragment.onAttach method to instantiate listener
    /*@Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        //Verify that the host activity implements the callback interface
        try{
            //Instantiate listener so events can be sent to host
            mListener = (ChangeBoardNameDialogFragment.ChangeBoardDialogListener) activity;
        } catch(ClassCastException e){
            //Activity doesn't implement
            throw new ClassCastException(activity.toString() + " must implement ChangeBoardDialogListener");
        }
    }*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Builder Class for dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        board = getArguments().getString("OldBoard");
        sessionID = getArguments().getString("SessionToken");
        housename = getArguments().getString("HouseName");
        //Get a Layout Inflater for custom layout
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setMessage("Are you sure that you want to remove this board?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        new remove().execute();
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        RemoveBoardDialog.this.getDialog().cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        RemoveBoardDialog.this.getDialog().cancel();
                    }
                });
        //Create Dialog object and return it
        return builder.create();
    }

    private class remove extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String session = sessionID;
        String housena = housename;
        String bname = board;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("SessionToken", session)
                    .put("HouseName",housena)
                    .put("BoardName",bname);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            //Make a request to url and get response
            Log.d(TAG, "object : " + jsonObject.toString());
                String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/board/removeboard", jsonObject);
                if (resp != null) {
                    Log.d(TAG, "remove : " + resp);
                }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }
}
