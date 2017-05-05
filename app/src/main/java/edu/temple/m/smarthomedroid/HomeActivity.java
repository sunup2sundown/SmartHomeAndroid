package edu.temple.m.smarthomedroid;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import edu.temple.m.smarthomedroid.Dashboard.DataPassListener;

import edu.temple.m.smarthomedroid.Dialogs.ChangeHousePasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangePasswordDialogFragment;


import edu.temple.m.smarthomedroid.Dialogs.ChangeUsernameDialogFragment;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler;

import edu.temple.m.smarthomedroid.Handlers.ProgressHandler;
import edu.temple.m.smarthomedroid.Handlers.TaskHandler;
import edu.temple.m.smarthomedroid.Handlers.VoiceHandler;
import edu.temple.m.smarthomedroid.Handlers.refresh;

import static java.lang.Thread.sleep;


public class HomeActivity extends AppCompatActivity
        implements ChangeUsernameDialogFragment.ChangeUsernameDialogListener
        , ChangePasswordDialogFragment.ChangePasswordDialogListener
        , UserSettingsFragment.OnHouseItemClickListener
      //  , PeripheralAdapter.OnPeripheralAdapterItemClickListener
        , ChangeHousePasswordDialogFragment.Listener
         //, BoardAdapter.OnBoardAdapterItemClickListener
        , NotifySettingsFragmentListener
        , DataPassListener
        , refresh
        , UserSettingsFragment.maketoast
        , SystemSettingsFragment.maketoast2
        {
    private Fragment fragment;
    private final String TAG = "HomeActivity";
    //Drawer & Toolbar declarations
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView navDrawer;
    private ActionBarDrawerToggle drawerToggle;
    //Fragment Management Declarations
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private String housename_dashboard;
    //Session Data
    String userId, sessionId;
    String response;
    String userPassword;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    String fragmentToStart;

    private String houseName, newHouseName, housePassword, newHousePassword;
    private Spinner listhouse;
    FragmentManager dialogManager;
    private JSONArray houses;
            /**
     * Class Methods
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_home);

        fragmentToStart = "";


        //Receive session ID and Username from Login Activity
        Intent prevIntent = getIntent();
        sessionId = prevIntent.getStringExtra("SessionId");
        userId = prevIntent.getStringExtra("Username");

        Button voiceButton = (Button)findViewById(R.id.voice);
        // Check to see if a recognition activity is present
// if running on AVD virtual device it will give this message. The mic
// required only works on an actual android device
        PackageManager pm = getPackageManager();
        List activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            //voiceButton.setOnClickListener(this);
        } else {
            voiceButton.setEnabled(false);
            voiceButton.setText("Recognizer not present");
        }

        //get toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get Drawer View
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        navDrawer = (NavigationView)findViewById(R.id.nav_view);
        //Setup Drawer View
        setupDrawerContent(navDrawer);
        Bundle bundle = new Bundle();
        Fragment fragment = null;
        boolean activityClosing= false;
        bundle.putString("Username", userId);
        bundle.putString("SessionToken", sessionId);
        fragment = new Dashboard();
        fragment.setArguments(bundle);
        //Insert the fragment by replacing any existing fragments
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            FragmentManager fm = getSupportFragmentManager();
            fm.popBackStack();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        //Sync the toggle state after onRestoreInstanceState has occured.
        drawerToggle.syncState();
    }

    /**
     * App Bar Setup
     * @return
     */
    private ActionBarDrawerToggle setupDrawerToggle(){
        //Pass in valid toolbar reference
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }

    /**
     * App Bar Methods
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.smart_home, menu);
        menu.findItem(R.id.voice).setShowAsAction(1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the House/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.voice){
            //TODO: Implement Voice Recognition and Commands
            Log.d(TAG, "Clicked Voice Button");
            startVoiceRecognitionActivity();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView nView){
        nView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                }
        );
    }

    /**
     * Navigation Drawer Menu Setup & Methods
     * @param menuItem
     */
    public void selectDrawerItem(MenuItem menuItem){
        //Create a new Fragment and specify fragment to show
        //based on nav item clicked
        //We do not need to save entire fragment b/c we want to
        //reload data on create anyway
        Bundle bundle = new Bundle();
        fragment = null;
        boolean activityClosing= false;

        bundle.putString("Username", userId);
        bundle.putString("SessionToken", sessionId);//This line i use token for test, for final release we pass tokenID
        bundle.putString("HouseName",housename_dashboard);

        //Generate Fragment ONLY WHEN FRAGMENT IS COMPLETED to make sure it will
        //show properly
        switch(menuItem.getItemId()){
            case R.id.nav_dashboard:
                fragment = new Dashboard();
                break;
            case R.id.nav_sensor:
                fragment = new SensorFragment();
                break;
            case R.id.nav_relay:
                fragment = new RelayFragment();
                break;
            case R.id.nav_autom:
                fragment = new AutomationFragment();
                break;
            case R.id.nav_config:
                fragment = new SystemSettingsFragment();
                break;
            case R.id.nav_setting:
                fragment = new UserSettingsFragment();
                break;
            case R.id.nav_camera:
                fragment = new CameraFragment();
                break;
            case R.id.nav_logout:
                activityClosing = true;
                Intent mIntent = new Intent(HomeActivity.this, LoginActivity.class);
                finish();
                startActivity(mIntent);
                break;
            default:

        }

        if(!activityClosing && fragment != null) {
            //Set Fragment Arguments
            fragment.setArguments(bundle);
            //Insert the fragment by replacing any existing fragments
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment).addToBackStack(null);
            fragmentTransaction.commit();
        }

        //Highlight selected item
        menuItem.setChecked(true);
        //Set Action Bar title
        setTitle(menuItem.getTitle());
        //Close Navigation Drawer
        mDrawer.closeDrawers();
    }

    // Interface functions


/*
    public void onHouseAdapterItemChangePasswordClick(String houseName){
        ChangeHousePasswordDialogFragment f = ChangeHousePasswordDialogFragment.newInstance(houseName);
        f.show(fragmentManager, null);
    }
    */

    @Override
    public void onHouseItemClick(int index, String houseName, String sessionToken) {
        UserSettingsFragment.HouseOptionsDialogFragment f = UserSettingsFragment.HouseOptionsDialogFragment.newInstance(index, houseName, sessionToken);
        f.show(fragmentManager, null);
    }
/*
    @Override
    public void onPeripheralAdapterItemClick(String peripheralName){
        ChangePeripheralNameDialogFragment f = new ChangePeripheralNameDialogFragment(peripheralName);
        f.show(fragmentManager, null);
    }
*/

    /* Dialog Fragment Listeners Implementations
     *
     */


    @Override
    public void onChangeUsernameDialogPositiveClick(DialogFragment dialog){
        EditText name = (EditText) dialog.getDialog().findViewById(R.id.change_username_dialog_username);
        if (new TaskHandler().changeUsername(this, name.getText().toString(), sessionId)) {
            userId = name.getText().toString();
            Toast.makeText(dialog.getContext(),"Username is changed!", Toast.LENGTH_LONG).show();
            Log.d("changeUsername", "true");
            updateSettingsFragment();
        } else {
            Toast.makeText(dialog.getContext(),"Failed, please try again!", Toast.LENGTH_LONG).show();
            Log.d("changeUsername", "false");
        }
    }

    @Override
    public void onChangePasswordDialogPositiveClick(DialogFragment dialog){
        EditText pw1 = (EditText)dialog.getDialog().findViewById(R.id.change_password_dialog_password);
        EditText pw2 = (EditText)dialog.getDialog().findViewById(R.id.change_password_dialog_confirm_pw);
        if(pw1.getText().toString().equals(pw2.getText().toString())) {
            new TaskHandler().changeUserPassword(this, pw1.getText().toString(), sessionId);
        } else{
            Toast.makeText(this, "\"Confirm Password\" must match \"New Password\"", Toast.LENGTH_SHORT).show();
        }
    }
/*
    @Override
    public void onChangeHouseNameDialogPositiveClick(DialogFragment dialog){
        houseName = ((EditText)dialog.getDialog().findViewById(R.id.change_house_name_dialog_old_name)).getText().toString();
        housePassword = ((EditText)dialog.getDialog().findViewById(R.id.change_house_name_dialog_password)).getText().toString();
        newHouseName = ((EditText)dialog.getDialog().findViewById(R.id.change_house_name_dialog_new_name)).getText().toString();

        (new ChangeHouseName()).execute();
    }
*/
    @Override
    public void onChangeHousePasswordDialogPositiveClick(String houseName, DialogFragment dialog, String sessionToken){

        String oldHousePassword = ((EditText) dialog.getDialog()
                .findViewById(R.id.change_house_password_dialog_password)).getText().toString();
        String newHousePassword = ((EditText) dialog.getDialog()
                .findViewById(R.id.change_house_password_dialog_new_password)).getText().toString();
        String confirmPassword = ((EditText) dialog.getDialog()
                .findViewById(R.id.change_house_password_dialog_confirm_password)).getText().toString();
        if (newHousePassword.equals(confirmPassword)) {
            new TaskHandler().changeHousePassword(this, houseName, oldHousePassword, newHousePassword, sessionToken);
        }
        else {
            Toast.makeText(this, "\"Confirm Password\" must match \"New Password\"", Toast.LENGTH_SHORT).show();
        }

    }

    public void onSwitchHouseDialogPositiveClick(String houseName, String housePw, String sessionToken){
        new JoinHouse().execute(houseName, housePw, sessionToken);
    }
    // "on negative click" functions
    @Override
    public void onChangeUsernameDialogNegativeClick(DialogFragment dialog){
        dialog.getDialog().cancel();
    }
    @Override
    public void onChangePasswordDialogNegativeClick(DialogFragment dialog){
        dialog.getDialog().cancel();
    }
    /*
    @Override
    public void onChangeHouseNameDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }
    @Override
    public void onChangeHousePasswordDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }
*/
    @Override
    public void updateSettingsFragment(){
        if (fragment instanceof UserSettingsFragment){
            Bundle bundle = new Bundle();
            bundle.putString("Username", userId);
            ((UserSettingsFragment)fragment).refresh(bundle);
        }
    }

    @Override
    public void passData(String data) {
        this.housename_dashboard = data;
    }

    /**
     *
     */
    public void startVoiceRecognitionActivity(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Smart Home Speech Recognition");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK){
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            VoiceHandler vh = new VoiceHandler(getApplicationContext(), housename_dashboard , sessionId, matches);
            fragmentToStart = vh.getResult();
        }
    }

    @Override
    public void onPostResume(){
        super.onPostResume();

        switch(fragmentToStart){
            case "RelayFragment":
                    startRelaysTab();
                break;
            case "SettingsFragment":
                startSettingsTab(false);
                break;
            case "Logout":
                Intent mIntent = new Intent(HomeActivity.this, LoginActivity.class);
                finish();
                startActivity(mIntent);
                break;
            case "SystemSettingsFragment":
                startConfigTab();
                break;
            case "SensorFragment":
                startSensorTab();
                break;
            case "DashboardFragment":
                startDashboardTab();
                break;
            case "CameraFragment":
                startCameraTab();
                break;
            default:
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void startRelaysTab(){
        Log.d(TAG, "Starting relay tab");
        Bundle bundle = new Bundle();
        fragment = new RelayFragment();

        bundle.putString("Username", userId);
        bundle.putString("SessionToken", sessionId);//This line i use token for test, for final release we pass tokenID
        bundle.putString("HouseName",housename_dashboard);

        if(fragment != null) {
            //Set Fragment Arguments
            fragment.setArguments(bundle);
            //Insert the fragment by replacing any existing fragments
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    private void startSensorTab(){
        Log.d(TAG, "Starting Sensor tab");
        Bundle bundle = new Bundle();
        fragment = new SensorFragment();

        bundle.putString("Username", userId);
        bundle.putString("SessionToken", sessionId);//This line i use token for test, for final release we pass tokenID
        bundle.putString("HouseName",housename_dashboard);

        if(fragment != null) {
            //Set Fragment Arguments
            fragment.setArguments(bundle);
            //Insert the fragment by replacing any existing fragments
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    private void startDashboardTab(){
        Log.d(TAG, "Starting Dashboard tab");
        Bundle bundle = new Bundle();
        Fragment fragment = new Dashboard();

        bundle.putString("Username", userId);
        bundle.putString("SessionToken", sessionId);//This line i use token for test, for final release we pass tokenID
        bundle.putString("HouseName",housename_dashboard);

        if(fragment != null) {
            //Set Fragment Arguments
            fragment.setArguments(bundle);
            //Insert the fragment by replacing any existing fragments
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    private void startConfigTab(){
        Log.d(TAG, "Starting Config tab");
        Bundle bundle = new Bundle();
        fragment = new SystemSettingsFragment();

        bundle.putString("Username", userId);
        bundle.putString("SessionToken", sessionId);//This line i use token for test, for final release we pass tokenID
        bundle.putString("HouseName",housename_dashboard);

        if(fragment != null) {
            //Set Fragment Arguments
            fragment.setArguments(bundle);
            //Insert the fragment by replacing any existing fragments
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    private void startSettingsTab(boolean reload){
        Log.d(TAG, "Starting Settings tab");
        Bundle bundle = new Bundle();
        fragment = new UserSettingsFragment();

        bundle.putString("Username", userId);
        bundle.putString("SessionToken", sessionId);//This line i use token for test, for final release we pass tokenID
        bundle.putString("HouseName",housename_dashboard);

        if(fragment != null) {
            //Set Fragment Arguments
            fragment.setArguments(bundle);
            //Insert the fragment by replacing any existing fragments
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment);
            if (reload) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        }
    }

    private void startCameraTab(){
        Log.d(TAG, "Starting Camera tab");
        Bundle bundle = new Bundle();
        fragment = new CameraFragment();

        bundle.putString("Username", userId);
        bundle.putString("SessionToken", sessionId);//This line i use token for test, for final release we pass tokenID
        bundle.putString("HouseName",housename_dashboard);

        if(fragment != null) {
            //Set Fragment Arguments
            fragment.setArguments(bundle);
            //Insert the fragment by replacing any existing fragments
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment);
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        }
    }

            @Override
            public void msg(String m) {
                Toast.makeText(getApplicationContext(),m,Toast.LENGTH_LONG).show();
            }

            @Override
            public void msg2(String m) {
                Toast.makeText(getApplicationContext(),m,Toast.LENGTH_LONG).show();
            }


            /**
     * HTTP Calls --Async Task
     */
    private class ChangeUserName extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String user = userId;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("password", user);
                jsonObject.put("sessionToken", sessionId);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/changeusername", jsonObject);

            Log.d(TAG, "Change Username: " + resp);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    private class ChangeUserPassword extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String pw = userPassword;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("password", pw);
                jsonObject.put("sessionToken", sessionId);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/changepassword", jsonObject);

            Log.d(TAG, "Change Password: " + resp);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    private class ChangeHouseName extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String name = houseName;
        String password = housePassword;
        String newName = newHouseName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("oldHouseName", name);
                jsonObject.put("housePassword", password);
                jsonObject.put("newHouseName", newName);
                jsonObject.put("sessionToken", sessionId);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/house/changehousename", jsonObject);

            Log.d(TAG, "Change House Name: " + resp);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    private class ChangeHousePassword extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String name = houseName;
        String password = housePassword;
        String newPassword = newHousePassword;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("houseName", name);
                jsonObject.put("oldHousePassword", password);
                jsonObject.put("newHousePassword", newPassword);
                jsonObject.put("sessionToken", sessionId);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/house/changehousepassword", jsonObject);

            Log.d(TAG, "Change House Password: " + resp);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    private class JoinHouse extends AsyncTask<String, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();

            try{
                jsonObject.put("houseName", params[0]);
                jsonObject.put("housePassword", params[1]);
                jsonObject.put("sessionToken", params[2]);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/house/joinhouse", jsonObject);

            Log.d(TAG, "Join House: " + resp);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    private class GetBoardsByHouse extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... houseName) {
            JSONObject jsonObject = new JSONObject();
            String name = houseName[0];
            try{
                jsonObject.put("houseName", name);
                jsonObject.put("sessionToken", sessionId);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }

            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/house/getboardsbyhouse", jsonObject);

            Log.d(TAG, "Get Boards by House: " + resp);
            response = resp;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    // AsyncTask for API Call 18: Remove Peripheral
    private class RemovePeripheral extends AsyncTask<String, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... args) {
            JSONObject jsonObject = new JSONObject();
            String houseName = args[0];
            String peripheralName = args[1];
            try{
                jsonObject.put("peripheralName", peripheralName);
                jsonObject.put("houseName", houseName);
                jsonObject.put("sessionToken", sessionId);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }

            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/peripheral/removeperipheral", jsonObject);

            Log.d(TAG, "Get Boards by House: " + resp);
            response = resp;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }

    }

    // API Call 19: Check Peripheral Name Availability
    private class CheckPeripheralName extends AsyncTask<String, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... args) {
            JSONObject jsonObject = new JSONObject();
            String houseName = args[0];
            String peripheralName = args[1];
            try{
                jsonObject.put("peripheralName", peripheralName);
                jsonObject.put("houseName", houseName);
                jsonObject.put("sessionToken", sessionId);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }

            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/peripheral/checkperipheralnameavailability", jsonObject);

            Log.d(TAG, "Get Boards by House: " + resp);
            response = resp;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }

    }

    // hash function for password
    private static String hash_pass(String pw) {
        try {
            StringBuffer hexStr = new StringBuffer();
            byte[] hash;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(pw.getBytes(StandardCharsets.UTF_8));
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xFF & hash[i]);
                if (hex.length() == 1) {
                    hexStr.append('0');
                }
                hexStr.append(hex);
            }
            return hexStr.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
            @Override
            public void config_update() {
                Bundle bundle = new Bundle();
                Fragment fragment = new SystemSettingsFragment();
                bundle.putString("Username", userId);
                bundle.putString("SessionToken", sessionId);
                bundle.putString("HouseName",housename_dashboard);
                fragment.setArguments(bundle);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
}