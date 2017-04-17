package edu.temple.m.smarthomedroid;

import android.os.AsyncTask;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.temple.m.smarthomedroid.Adapters.HouseAdapter;
import edu.temple.m.smarthomedroid.Dialogs.ChangeHouseNameDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeHousePasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeUserPasswordDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeUsernameDialogFragment;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler2;
import edu.temple.m.smarthomedroid.Handlers.JSONHandler;
import edu.temple.m.smarthomedroid.Objects.House;

import static java.lang.Thread.sleep;

public class HomeActivity extends AppCompatActivity
        implements HouseAdapter.OnHouseAdapterItemClickListener,
        ChangeUsernameDialogFragment.ChangeUsernameDialogListener,
        ChangeUserPasswordDialogFragment.ChangeUserPasswordDialogListener,
        ChangeHouseNameDialogFragment.ChangeHouseNameDialogListener,
        ChangeHousePasswordDialogFragment.ChangeHousePasswordDialogListener{
    private final String TAG = "HomeActivity";
    //Drawer & Toolbar declarations
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView navDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private JSONObject houses;
    //Fragment Management Declarations
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    //Session Data
    String userId, sessionId;


    private String usern, userPassword, newUserPassword;
    private String houseName, newHouseName, housePassword, newHousePassword;
    private String  sessionToken;
    private ArrayList<House> houseList;
    private Spinner listhouse;
    FragmentManager dialogManager;


    /**
     * Class Methods
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_home);

        usern = "Tom Brady";
        sessionToken = "018C98BB-C886-44B1-8667-DA304872B452";

        //Receive session ID and Username from Login Activity
        Intent prevIntent = getIntent();
        sessionId = prevIntent.getStringExtra("SessionId");
        userId = prevIntent.getStringExtra("Username");

        Log.d(TAG, "Username: " + userId);
        Log.d(TAG, "SessionToken: " + sessionId);



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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        Fragment fragment = null;
        boolean activityClosing= false;

        bundle.putString("Username", userId);
        bundle.putString("SessionToken", sessionId);

        //Generate Fragment ONLY WHEN FRAGMENT IS COMPLETED to make sure it will
        //show properly
        switch(menuItem.getItemId()){
            case R.id.nav_dashboard:
                fragment = new Dashboard();
                break;
            case R.id.nav_sensor:
                fragment = new RelayFragment();
                break;
            case R.id.nav_relay:
                fragment = new RelayFragment();
                break;
            case R.id.nav_config:
                fragment = new ConfigFragment();
                break;
            case R.id.nav_setting:
                fragment = new UserSettingsFragment();
                break;
            case R.id.nav_system:
                fragment = new RelayFragment();
                break;
            case R.id.nav_logout:
                activityClosing = true;
                Intent mIntent = new Intent(HomeActivity.this, LoginActivity.class);
                finish();
                startActivity(mIntent);
                break;
            case R.id.nav_share:
                fragment = new RelayFragment();
                break;
            case R.id.nav_send:
                fragment = new RelayFragment();
                break;
            default:

        }

        if(!activityClosing && fragment != null) {
            //Set Fragment Arguments
            fragment.setArguments(bundle);
            //Insert the fragment by replacing any existing fragments
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment);
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

    public void onHouseAdapterItemRenameClick(String houseName) {
        ChangeHouseNameDialogFragment f = ChangeHouseNameDialogFragment.newInstance(houseName);
        f.show(fragmentManager, null);
    }
    public void onHouseAdapterItemChangePasswordClick(String houseName){
        ChangeHousePasswordDialogFragment f = ChangeHousePasswordDialogFragment.newInstance(houseName);
        f.show(fragmentManager, null);
    }

    /* Dialog Fragment Listeners Implementations
     *
     */

    @Override
    public void onChangeUsernameDialogPositiveClick(DialogFragment dialog){

    }

    @Override
    public void onChangeUsernameDialogNegativeClick(DialogFragment dialog){
        dialog.getDialog().cancel();
    }

    @Override
    public void onChangeUserPasswordDialogPositiveClick(DialogFragment dialog){

    }

    @Override
    public void onChangeHouseNameDialogPositiveClick(DialogFragment dialog){
        houseName = ((EditText)dialog.getDialog().findViewById(R.id.change_house_name_dialog_old_name)).getText().toString();
        housePassword = ((EditText)dialog.getDialog().findViewById(R.id.change_house_name_dialog_password)).getText().toString();
        newHouseName = ((EditText)dialog.getDialog().findViewById(R.id.change_house_name_dialog_new_name)).getText().toString();
        (new ChangeHouseName()).execute();
    }

    @Override
    public void onChangeHousePasswordDialogPositiveClick(DialogFragment dialog){

    }

    // "on negative click" functions
    @Override
    public void onChangeUserPasswordDialogNegativeClick(DialogFragment dialog){
        dialog.getDialog().cancel();
    }
    @Override
    public void onChangeHouseNameDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }
    @Override
    public void onChangeHousePasswordDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }

    /**
     * HTTP Calls --Async Task
     */
    private class ChangeUserName extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String user = usern;
        String session = sessionToken;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("password", user);
                jsonObject.put("sessionToken", sessionToken);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/changeusername", jsonObject);

            if(resp != null){
                Log.d(TAG, "Change Username: " + resp);
            }

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
        String session = sessionToken;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("password", pw);
                jsonObject.put("sessionToken", sessionToken);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/changepassword", jsonObject);

            if(resp != null){
                Log.d(TAG, "Change Password: " + resp);
            }

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
        String session = sessionToken;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("oldHouseName", name);
                jsonObject.put("housePassword", password);
                jsonObject.put("newHouseName", newName);
                jsonObject.put("sessionToken", sessionToken);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/house/changehousename", jsonObject);

            if(resp != null){
                Log.d(TAG, "Change House Name: " + resp);
            }

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
        String session = sessionToken;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("houseName", name);
                jsonObject.put("oldHousePassword", password);
                jsonObject.put("newHousePassword", newPassword);
                jsonObject.put("sessionToken", sessionToken);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/house/changehousepassword", jsonObject);

            if(resp != null){
                Log.d(TAG, "Change House Password: " + resp);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    private class JoinHouse extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        String name = houseName;
        String password = housePassword;
        String session = sessionToken;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                jsonObject.put("houseName", name);
                jsonObject.put("housePassword", password);
                jsonObject.put("sessionToken", sessionToken);
            } catch(JSONException e){
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/house/joinhouse", jsonObject);

            if(resp != null){
                Log.d(TAG, "Join House: " + resp);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }
}

