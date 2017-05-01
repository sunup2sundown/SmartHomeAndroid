package edu.temple.m.smarthomedroid.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Dialogs.ListRulesDialog;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Objects.Relay2;
import edu.temple.m.smarthomedroid.R;

import static android.content.ContentValues.TAG;
import static android.graphics.Typeface.BOLD;
import static java.lang.Thread.sleep;

/**
 * Created by quido on 4/16/17.
 */

public class RulesAdapter extends ArrayAdapter<String> {
        private Context context;
        private boolean useList = true;
        private String housename,session;
        private dialog_cancel done;
        private ListRulesDialog dialog2;

        public interface dialog_cancel{
                public void can();
        }
        /**
         * Constructor Method
         */
        public RulesAdapter(Context context, ArrayList<String> list, String session,String house, ListRulesDialog dialog) {
                super(context, android.R.layout.simple_list_item_1,list);
                this.housename=house;
                this.session=session;
                this.dialog2=dialog;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                final String item = (String) getItem(position);

                if (convertView == null) {
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.auto_rule_list, parent, false);
                }
                TextView name = (TextView) convertView.findViewById(R.id.textView6);
                name.setTextSize(20);
                //name.setTypeface(name.getTypeface(),BOLD);
                name.setText(item);
                name.isTextSelectable();
                name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View viewIn) {
                                try {
                                        new remove(housename,item,session).execute();
                                        try {
                                                sleep(1000);
                                        } catch (InterruptedException e) {
                                                e.printStackTrace();
                                        }
                                        dialog2.getDialog().cancel();
                                } catch (Exception except) {
                                        Log.e(TAG,"Failed :"+except.getMessage());
                                }
                        }
                });

                return convertView;
        }

        private class remove extends AsyncTask<Void, Void, Void> {
                JSONObject jsonObject = new JSONObject();
                JSONArray i;
                String sessionID,housename,rule;
                public remove(String house,String rulename,String session){
                        this.sessionID=session;
                        this.housename=house;
                        this.rule=rulename;
                }
                @Override
                protected void onPreExecute() {
                        super.onPreExecute();

                        try{
                                jsonObject.put("sessionToken", sessionID)
                                        .put("houseName",housename)
                                        .put("ruleName",rule);
                        } catch(JSONException e){
                                Log.e(TAG, "JSONException: " + e.getMessage());
                        }
                }

                @Override
                protected Void doInBackground(Void... params) {
                        HttpHandler sh = new HttpHandler();
                        Log.d(TAG, "object : " + jsonObject.toString());
                        String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/removeautomationrule", jsonObject);
                        if (resp != null) {
                                Log.d(TAG, "list rule : " + resp);
                        }
                        return null;
                }
                @Override
                protected void onPostExecute(Void result) {
                        super.onPostExecute(result);

                }
        }
}
