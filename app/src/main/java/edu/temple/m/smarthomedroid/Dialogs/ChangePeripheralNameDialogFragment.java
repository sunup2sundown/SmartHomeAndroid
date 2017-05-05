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

import org.json.JSONException;
import org.json.JSONObject;

import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Handlers.refresh;
import edu.temple.m.smarthomedroid.R;

import static java.lang.Thread.sleep;

/**
 * Created by Jhang Myong Ja on 4/15/2017.
 */
public class ChangePeripheralNameDialogFragment extends DialogFragment {
    private String sessionID,housename,oldname;
    private String TAG = "AddBoardDialog";
    private refresh update;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        update = (refresh) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        sessionID = getArguments().getString("SessionToken");
        housename = getArguments().getString("HouseName");
        oldname = getArguments().getString("OldName");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_rename_peripheral, null))
                // Add action buttons
                .setTitle("Change Peripheral's Name")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText tt = (EditText) ChangePeripheralNameDialogFragment.this.getDialog().findViewById(R.id.dialog_new_peripheral_name);
                        String newname = tt.getText().toString();
                        Log.d("Newname :", newname);
                        new rename(sessionID,housename,oldname,newname).execute();
                        try {
                            sleep(1800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ChangePeripheralNameDialogFragment.this.getDialog().cancel();
                        update.config_update();
                        // switch to the new house...
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ChangePeripheralNameDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private class rename extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        JSONObject check = new JSONObject();
        String session,house,oldname,newname;

        public rename(String sesstoken,String housename,String oldname,String newname){
            this.session=sesstoken;
            this.house=housename;
            this.oldname=oldname;
            this.newname=newname;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
                jsonObject.put("sessionToken", this.session)
                    .put("oldPeripheralName",this.oldname)
                    .put("houseName",this.house)
                    .put("newPeripheralName",this.newname);
                check.put("peripheralName",this.newname)
                    .put("houseName",this.house)
                    .put("sessionToken",this.session);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }
        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String checkname=sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/peripheral/checkpheriperalnameavailability", check);
            if(checkname!=null){
                Log.d(TAG, "Check Name : " + checkname);
            }
            if(checkname.equalsIgnoreCase("1")){
                String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/peripheral/changeperipheralname", jsonObject);
                if (resp != null) {
                    Log.d(TAG, "ChangePeripheralName : " + resp);
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
