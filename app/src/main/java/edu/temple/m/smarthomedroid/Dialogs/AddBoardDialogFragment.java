package edu.temple.m.smarthomedroid.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Handlers.TaskHandler;
import edu.temple.m.smarthomedroid.Handlers.refresh;
import edu.temple.m.smarthomedroid.R;

import static java.lang.Thread.sleep;

/**
 * Created by Jhang Myong Ja on 4/15/2017.
 */
public class AddBoardDialogFragment extends DialogFragment {
    private String sessionID,housename;
    private String TAG = "AddBoardDialog";
    private refresh update;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        update = (refresh) activity;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        sessionID = getArguments().getString("SessionToken");
        housename = getArguments().getString("HouseName");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_add_board, null))
                // Add action buttons
                .setTitle("Add Board")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String boardName =
                                ((EditText) AddBoardDialogFragment.this.getDialog().findViewById(R.id.dialog_board_name)).toString();
                        String boardSerialNo =
                                ((EditText) AddBoardDialogFragment.this.getDialog().findViewById(R.id.dialog_board_serial_no)).toString();
                        new add_board(sessionID,housename,boardName,boardSerialNo).execute();
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        AddBoardDialogFragment.this.getDialog().cancel();
                        update.config_update();
                        // switch to the new house...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddBoardDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private class add_board extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        JSONObject check = new JSONObject();
        String session,house,boardname,serial;

        public add_board(String sesstoken,String housename,String boardname,String serial){
            this.session=sesstoken;
            this.house=housename;
            this.boardname=boardname;
            this.serial=serial;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
                jsonObject.put("SessionToken", this.session)
                    .put("BoardSerialNumber",this.serial)
                    .put("HouseName",this.house)
                    .put("BoardName",this.boardname);
                check.put("BoardName",this.boardname)
                    .put("HouseName",this.house)
                    .put("SessionToken",this.session);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }
        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String checkname=sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/board/checkboardnameavailability", check);
            if(checkname!=null){
                Log.d("AHHHHHHHHH ",checkname);
            }
            if(checkname.equalsIgnoreCase("1")){
                String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/board/createboard", jsonObject);
                if (resp != null) {
                    Log.d(TAG, "CreateBoard : " + resp);
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
