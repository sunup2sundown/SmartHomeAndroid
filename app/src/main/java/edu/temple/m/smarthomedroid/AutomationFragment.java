package edu.temple.m.smarthomedroid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.temple.m.smarthomedroid.Adapters.GridAdapter;
import edu.temple.m.smarthomedroid.Dialogs.AddBoardDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ListRulesDialog;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Objects.Relay2;

import static java.lang.Thread.sleep;

/**
 * Created by DongBinChoi on 4/28/2017.
 */

public class AutomationFragment extends Fragment {
    Bundle bundle;
    private int done,gooduser;
    private final String TAG = "AutomationFragment";

    private String userID,sessionID,str_rulename,houseName,str_condperi,
                    str_condname,str_actioncate,str_actionperi,
                    str_actionname,str_actionparam,str_condval,str_para;

    private Spinner house, condperi, condname,actioncate,
                    actionperi, actionname, actionparam;

    private EditText rulename, condval,paramet;
    private Button listrule,addrule;
    private JSONArray listhouse,condperi_list,cond_name,action_cate,actperi_list,
            action_namelist, action_param;

    private synchronized void setdone(int n){
        this.done=n;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Create new view from relay fragment layout
        View view = inflater.inflate(R.layout.fragment_automation, container, false);
        gooduser=1;
        bundle = new Bundle();
        done=0;
        str_rulename="";
        str_condval="";
        //Receive argument bundle from Home Activity
        userID = getArguments().getString("Username");
        sessionID = getArguments().getString("SessionToken");
        bundle.putString("SessionToken", sessionID);
        house = (Spinner) view.findViewById(R.id.auto_house_spinner);
        condperi = (Spinner) view.findViewById(R.id.auto_condiperiname);
        condname = (Spinner) view.findViewById(R.id.auto_conditioname);
        actionperi = (Spinner) view.findViewById(R.id.auto_actionperi);
        actionname = (Spinner) view.findViewById(R.id.auto_actionname);
        actionparam = (Spinner) view.findViewById(R.id.auto_actionparam);
        rulename = (EditText) view.findViewById(R.id.auto_rulename);
        condval = (EditText) view.findViewById(R.id.auto_condvalue);
        actioncate = (Spinner) view.findViewById(R.id.auto_actionperi_cate);
        listrule = (Button)view.findViewById(R.id.currentlist);
        addrule = (Button)view.findViewById(R.id.addrule);
        paramet = (EditText) view.findViewById(R.id.para);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //make house list
        additem();
        house.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String currenthouse;
                        Object item = parent.getItemAtPosition(pos);
                        currenthouse = item.toString();
                        houseName=currenthouse;
                        Log.d(TAG,currenthouse);
                        new getpe(currenthouse).execute();
                        while(done==0){
                            try {
                                sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        setdone(0);
                        List<String> list1 = new ArrayList<String>();
                        List<String> list2 = new ArrayList<String>();
                        List<String> list3 = new ArrayList<String>();
                        if (condperi_list != null) {
                            int o = 0;
                            for (; o < condperi_list.length(); o++) {
                                try {
                                    list1.add(condperi_list.getJSONObject(o).getString("PeripheralName"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            setspinner(condperi,list1);
                        } else {
                            Log.d(TAG, "cond peri list : null");
                        }
                        if (cond_name != null) {
                            int o = 0;
                            for (; o < cond_name.length(); o++) {
                                try {
                                    list2.add(cond_name.getJSONObject(o).getString("AutomationConditionName"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            setspinner(condname,list2);
                        } else {
                            Log.d(TAG, "cond list : null");
                        }
                        if (action_cate != null) {
                            int o = 0;
                            for (; o < action_cate.length(); o++) {
                                try {
                                    list3.add(action_cate.getJSONObject(o).getString("PeripheralCategoryName"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            setspinner(actioncate,list3);
                        } else {
                            Log.d(TAG, "cate : null");
                        }
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        condperi.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        str_condperi = item.toString();
                        Log.d(TAG,str_condperi);
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        condname.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        str_condname = item.toString();
                        Log.d(TAG,str_condname);
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        str_condval=condval.getText().toString();
        actioncate.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        str_actioncate = item.toString();
                        Log.d(TAG,str_actioncate);
                        new getactionperi(houseName,str_actioncate).execute();
                        while(done==0){
                            try {
                                sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        setdone(0);
                        List<String> list1 = new ArrayList<String>();
                        List<String> list2 = new ArrayList<String>();
                        if (actperi_list != null) {
                            int o = 0;
                            for (; o < actperi_list.length(); o++) {
                                try {
                                    list1.add(actperi_list.getJSONObject(o).getString("PeripheralName"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            setspinner(actionperi,list1);
                        } else {
                            Log.d(TAG, "action peri list : null");
                        }
                        if (action_namelist != null) {
                            int o = 0;
                            for (; o < action_namelist.length(); o++) {
                                try {
                                    list2.add(action_namelist.getJSONObject(o).getString("AutomationActionName"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            setspinner(actionname,list2);
                        } else {
                            Log.d(TAG, "action name list : null");
                        }
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        actionperi.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        str_actionperi = item.toString();
                        Log.d(TAG,str_actionperi);
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        actionname.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        str_actionname = item.toString();
                        Log.d(TAG,str_actionname);
                        new getparam(str_actionname).execute();
                        while(done==0){
                            try {
                                sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        setdone(0);
                        List<String> list1 = new ArrayList<String>();
                        if(action_param!=null){
                            int o = 0;
                            for (; o < action_param.length(); o++) {
                                try {
                                    list1.add(action_param.getJSONObject(o).getString("AutomationActionParameterDescription"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            setspinner(actionparam,list1);
                        } else {
                            Log.d(TAG, "action param list : null");
                        }
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        actionparam.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        if(parent.getItemAtPosition(pos)!=null&&!(parent.getItemAtPosition(pos).toString().equals("None"))){
                            Object item = parent.getItemAtPosition(pos);
                            str_actionparam = item.toString();
                        }else{
                            str_actionparam = null;
                        }
                        Log.d(TAG,str_actionname);
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        addrule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str_actionparam!=null){
                    str_actionparam=paramet.getText().toString();
                }
                Log.d("TESTTEST",str_actionparam);
                str_rulename = rulename.getText().toString();
                str_condval = condval.getText().toString();
                if(str_rulename.isEmpty()||str_rulename==null){
                    Toast.makeText(getActivity(),"Please provide rule name", Toast.LENGTH_LONG).show();
                }else if(str_condval.isEmpty()||str_condval==null){
                    Toast.makeText(getActivity(),"Please provide conddition value", Toast.LENGTH_LONG).show();
                }else{
                    new setrule().execute();
                    while(done==0){
                        try {
                            sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    setdone(0);
                    if(gooduser==0){
                        Toast.makeText(getActivity(),"Invalid Rule name, please try again!", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(),"Rule added successfully!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        listrule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundleaddboard= new Bundle();
                bundleaddboard.putString("SessionToken",sessionID);
                bundleaddboard.putString("HouseName",houseName);
                FragmentManager dialogManager0;
                dialogManager0 = getActivity().getSupportFragmentManager();
                ListRulesDialog fr = new ListRulesDialog();
                fr.setArguments(bundleaddboard);
                fr.show(dialogManager0,null);
            }
        });
    }

    public void setspinner(Spinner spin,List<String> list){
            ArrayAdapter<String> dataAdapter;
            dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.item_spinner, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(dataAdapter);
    }

    public void additem() {
        List<String> list1 = new ArrayList<String>();
        new gethouse().execute();
        while(done==0){
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setdone(0);
        if (listhouse != null) {
            int o = 0;
            for (; o < listhouse.length(); o++) {
                try {
                    list1.add(listhouse.getJSONObject(o).getString("HouseName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ArrayAdapter<String> dataAdapter;
            dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.item_spinner, list1);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            house.setAdapter(dataAdapter);
        } else {
            Log.d(TAG, "null");
        }
    }

    public void onBackPressed() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }

    //task for houses
    private class gethouse extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String session = sessionID;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                jsonObject.put("sessionToken", session);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            JSONArray ii = null;
            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/house/listhouses", jsonObject);
            if (resp != null) {
                Log.d(TAG, "Auto Gethous: " + resp);
                try {
                    ii = new JSONArray(resp);
                    listhouse = ii.getJSONArray(0);
                    Log.d(TAG, "Resp: " + listhouse);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            setdone(1);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    //Get peri lists
    private class getpe extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        String session = sessionID;
        String housename;
        public getpe(String house_name){
            this.housename=house_name;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                jsonObject.put("sessionToken", session);
                jsonObject2.put("SessionToken",session)
                        .put("HouseName",housename);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            JSONArray i,ii,iii;
            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/getautomationconditions", jsonObject);
            String resp2 = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/sensor/getsensorsbyhouse",jsonObject2);
            String resp3 = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/peripheral/getactionperipheralcategories",jsonObject2);
            if(resp != null){
                Log.d(TAG, "Get auto conditions: " + resp);
                try {
                    ii=new JSONArray(resp);
                    cond_name=ii.getJSONArray(0);
                    Log.d("condname =", cond_name.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(resp2 != null){
                Log.d(TAG, "Get sensors: " + resp2);
                try {
                    iii=new JSONArray(resp2);
                    condperi_list=iii.getJSONArray(0);
                    Log.d("peri =", condperi_list.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(resp3 != null){
                Log.d(TAG, "Get cate: " + resp3);
                try {
                    i=new JSONArray(resp3);
                    action_cate=i.getJSONArray(0);
                    Log.d("cate =", action_cate.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            setdone(1);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    //task for action peripheral
    private class getactionperi extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        String session = sessionID;
        String house,cate;

        public getactionperi(String house,String cate){
            this.house=house;
            this.cate=cate;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                jsonObject.put("sessionToken", session)
                        .put("houseName",house)
                        .put("peripheralCategoryName",cate);
                jsonObject2.put("SessionToken",session)
                        .put("PeripheralCategoryName",cate);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            JSONArray ii,iii;
            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/getperipheralsbyhouseandcategory", jsonObject);
            String resp2 = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/getautomationactionsbyperipheralcategory", jsonObject2);
            if (resp != null) {
                Log.d(TAG, "Action peri resp: " + resp);
                try {
                    ii = new JSONArray(resp);
                    actperi_list = ii.getJSONArray(0);
                    Log.d(TAG, "action peri array: " + actperi_list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (resp2 != null) {
                Log.d(TAG, "Action name resp: " + resp2);
                try {
                    iii = new JSONArray(resp2);
                    action_namelist = iii.getJSONArray(0);
                    Log.d(TAG, "action name array: " + action_namelist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            setdone(1);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    //****get param
    private class getparam extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String session = sessionID;
        String action;

        public getparam(String actioname){
            this.action=actioname;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                jsonObject.put("sessionToken", session)
                        .put("automationActionName",action);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            JSONArray ii;
            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/getactionparameternames", jsonObject);
            if (resp != null) {
                Log.d(TAG, "Action peri resp: " + resp);
                try {
                    ii = new JSONArray(resp);
                    action_param = ii.getJSONArray(0);
                    Log.d(TAG, "action param array: " + action_param);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            setdone(1);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    //******ADD RULE!
    private class setrule extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        JSONObject checkname = new JSONObject();
        String session = sessionID;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                jsonObject.put("SessionToken", session)
                        .put("HouseName",houseName)
                        .put("RuleName",str_rulename)
                        .put("ConditionPeripheralName",str_condperi)
                        .put("AutomationConditionName",str_condname)
                        .put("AutomationConditionValue",str_condval)
                        .put("ActionPeripheralName",str_actionperi)
                        .put("AutomationActionName",str_actionname)
                        .put("AutomationActionParameter",str_actionparam);
                Log.d("addrule json ",jsonObject.toString());
                checkname.put("sessionToken",session).put("houseName",houseName).put("ruleName",str_rulename);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            JSONArray ii;
            //Make a request to url and get response
            String check =sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/checkrulename", checkname);
            if(check!=null){
                Log.d(TAG,check);
            }
            if(check.equals("1")){
                gooduser=1;
                String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/createautomationrule", jsonObject);
                if (resp != null) {
                    Log.d("addrule resp: ",resp);
                }
            }else{
                gooduser=0;
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