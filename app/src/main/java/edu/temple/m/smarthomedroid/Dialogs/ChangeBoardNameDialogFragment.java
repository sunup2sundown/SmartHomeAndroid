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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Handlers.refresh;
import edu.temple.m.smarthomedroid.R;

import static edu.temple.m.smarthomedroid.SystemSettingsFragment.mpass2;

/**
 * Created by Matthew White on 3/17/2017.
 */

public class ChangeBoardNameDialogFragment extends DialogFragment {
    private String newboard,oldboard,sessionID,housename;
    private String check_name=" ";
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog);
    }
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
                        while (done==0){

                        }
                        setdone(0);
                        ChangeBoardNameDialogFragment.this.getDialog().cancel();
                        if(check_name.equals("name")){
                            mpass2.msg2("New name is invalid, please try again!");
                        }else{
                            if(error.equals("yes")){
                                mpass2.msg2("Failed, please try again!");
                            }else if(error.equals("no")){
                                mpass2.msg2("Renamed board!");
                                update.config_update();
                            }
                        }
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
        String resp2;
        String resp;
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
            resp2 = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/board/checkboardnameavailability", jsonObject2);
            if(resp2.equals("1")) {
                resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/board/changeboardname", jsonObject);
                if (resp != null) {
                    Log.d(TAG, "changeboard : " + resp);
                    if(resp.equals("\"0 No Errors\"")){
                        error="no";
                    }else{
                        error="yes";
                    }
                }else{
                    error="yes";
                }
                setdone(1);
                return null;
            }else {
                check_name="name";
                setdone(1);
                return null;
            }
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
