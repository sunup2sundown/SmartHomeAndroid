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
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Adapters.GridAdapter;
import edu.temple.m.smarthomedroid.Adapters.RulesAdapter;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Handlers.refresh;
import edu.temple.m.smarthomedroid.Objects.Relay2;
import edu.temple.m.smarthomedroid.R;

import static java.lang.Thread.sleep;

/**
 * Created by Matthew White on 3/17/2017.
 */

public class ListRulesDialog extends DialogFragment implements RulesAdapter.dialog_cancel {
    private String newboard,oldboard,sessionID,housename;
    private final String TAG = "ChangeBoardDialog";
    private EditText username, password;
    private refresh update;
    private int done=0;
    private int can=0;
    private RulesAdapter rAdapter;
    private ArrayList<String> relayList;
    private JSONArray rules;
    private ListView lv;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog);
    }
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
        sessionID = getArguments().getString("SessionToken");
        housename = getArguments().getString("HouseName");
        new getrules().execute();
        while(done==0){
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setdone(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Get a Layout Inflater for custom layout
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_list_rule, null);
        builder.setView(v);
        builder.setPositiveButton("Back", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        ListRulesDialog.this.getDialog().cancel();
                    }
                });
        builder.setTitle("LIST OF CURRENT RULES");
        relayList = new ArrayList<String>();
        rAdapter = new RulesAdapter(getActivity().getApplicationContext(), relayList,sessionID,housename,ListRulesDialog.this);
        if (rules!=null){
            int o=0;
            for(;o<rules.length();o++){
                Log.d(TAG,"get in: "+rules);
                try {
                    JSONObject check = rules.getJSONObject(o);
                    String name1 = check.getString("AutomationRuleName");
                    rAdapter.add(name1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            lv = (ListView)v.findViewById(R.id.listview_rule);
            lv.setAdapter(rAdapter);
        }
        //Create Dialog object and return it

        return builder.create();
    }

    @Override
    public void can() {
        ListRulesDialog.this.getDialog().cancel();
    }


    private class getrules extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        JSONArray i;
        String session = sessionID;
        String housena = housename;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("sessionToken", session)
                    .put("houseName",housena);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            //Make a request to url and get response
            Log.d(TAG, "object : " + jsonObject.toString());
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/getcurrentrulesbyhouse", jsonObject);
            if (resp != null) {
                Log.d(TAG, "list rule : " + resp);
                try {
                    i = new JSONArray(resp);
                    rules = i.getJSONArray(0);
                    Log.d(TAG, "Resp: " + rules.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
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
