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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Objects.DataHolder;
import edu.temple.m.smarthomedroid.R;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static edu.temple.m.smarthomedroid.ConfigFragment.dat;
import static java.lang.Thread.sleep;

/**
 * Created by Matthew White on 3/17/2017.
 */

public class ChangeBoardNameDialogFragment extends DialogFragment {
    private String newboard,oldboard,sessionID,housename;
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
        oldboard = getArguments().getString("OldBoard");
        sessionID = getArguments().getString("SessionToken");
        housename = getArguments().getString("HouseName");
        //Get a Layout Inflater for custom layout
        final LayoutInflater inflater = getActivity().getLayoutInflater();



        //Inflate Dialog with custom layout
        //null as parent view because its in dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_change_board_name, null))
                //Add Action Buttons
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        EditText name = (EditText) ChangeBoardNameDialogFragment.this.getDialog().findViewById(R.id.newboard);
                        newboard = name.getText().toString();
                        new changename().execute();
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ChangeBoardNameDialogFragment.this.getDialog().cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        ChangeBoardNameDialogFragment.this.getDialog().cancel();
                    }
                });
        //Create Dialog object and return it
        return builder.create();
    }

    private class changename extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        String session = sessionID;
        String housena = housename;
        String old = oldboard;
        String newb = newboard;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("SessionToken", session)
                    .put("HouseName",housena)
                    .put("OldBoardName",old)
                    .put("NewBoardName",newb);
                jsonObject2.put("HouseName",housena).put("SessionToken",session).put("BoardName",newb);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            //Make a request to url and get response
            Log.d(TAG, "object : " + jsonObject.toString());
            Log.d(TAG, "object2 : " + jsonObject2.toString());
            String resp2 = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/board/checkboardnameavailability", jsonObject2);
            if(resp2.equals("1")) {
                String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/board/changeboardname", jsonObject);
                if (resp != null) {
                    dat.setDistributor_id(newb);
                    Log.d(TAG, "changeboard : " + resp);
                }
            }else {
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }
}
