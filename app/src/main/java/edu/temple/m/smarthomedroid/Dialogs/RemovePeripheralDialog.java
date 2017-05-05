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

import static edu.temple.m.smarthomedroid.SystemSettingsFragment.mpass2;
import static java.lang.Thread.sleep;

/**
 * Created by Matthew White on 3/17/2017.
 */

public class RemovePeripheralDialog extends DialogFragment {
    private String periname,sessionID,housename;
    private String error=" ";
    private final String TAG = "ChangeBoardDialog";
    private EditText username, password;
    private refresh update;
    private int done=0;

    private synchronized void setdone(int n){
        this.done=n;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        update = (refresh) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Builder Class for dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        periname = getArguments().getString("OldName");
        sessionID = getArguments().getString("SessionToken");
        housename = getArguments().getString("HouseName");
        //Get a Layout Inflater for custom layout
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setMessage("Are you sure that you want to remove this peripheral?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        new remove().execute();
                        while(done==0){
                            try {
                                sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        setdone(0);
                        if(error.equals("no")){
                            mpass2.msg2("Removed peripheral successfully!");
                            RemovePeripheralDialog.this.getDialog().cancel();
                            update.config_update();
                        }else{
                            mpass2.msg2(error);
                            RemovePeripheralDialog.this.getDialog().cancel();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        RemovePeripheralDialog.this.getDialog().cancel();
                    }
                });
        //Create Dialog object and return it
        return builder.create();
    }

    private class remove extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String session = sessionID;
        String housena = housename;
        String pname = periname;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("sessionToken", session)
                    .put("houseName",housena)
                    .put("peripheralName",pname);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            //Make a request to url and get response
            Log.d(TAG, "removename json : " + jsonObject.toString());
                String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/peripheral/removeperipheral", jsonObject);
                if (resp != null) {
                    Log.d(TAG, "remove : " + resp);
                    if(resp.equals("\"Successfully removed peripheral\"")){
                        error="no";
                    }else{
                        error=resp;
                    }
                }else{
                    error="Failed, please try again!";
                }
            setdone(1);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }
}
