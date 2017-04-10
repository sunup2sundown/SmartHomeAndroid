package edu.temple.m.smarthomedroid;

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
import android.view.Menu;
import android.view.MenuItem;

import edu.temple.m.smarthomedroid.Adapters.HouseAdapter;
import edu.temple.m.smarthomedroid.Dialogs.ChangeHouseNameDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.ChangeHousePasswordDialogFragment;

public class HomeActivity extends AppCompatActivity implements HouseAdapter.OnHouseAdapterItemClickListener{

    //Drawer & Toolbar declarations
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView navDrawer;
    private ActionBarDrawerToggle drawerToggle;

    //Fragment Management Declarations
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    //Session Data
    String userId, sessionId;

    /**
     * Class Methods
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_home);

        //Receive session ID and Username from Login Activity
        Intent prevIntent = getIntent();
        sessionId = prevIntent.getStringExtra("SessionId");
        userId = prevIntent.getStringExtra("userid");

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
        Fragment fragment = null;
        boolean activityClosing= false;

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
            case R.id.nav_config:
                fragment = new ConfigFragment();
                break;
            case R.id.nav_setting:
                fragment = new UserSettingsFragment();
                break;
            case R.id.nav_system:
                fragment = new SystemFragment();
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
            //Insert the fragment by replacing any existing fragments
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contentframe, fragment);
            fragmentTransaction.commit();
        }

        //Highlight selected item
        menuItem.setChecked(true);
        //Set Action Bar title
        setTitle(menuItem.getTitle());
        //Close Navigation Drawer
        mDrawer.closeDrawers();
    }

    public void onHouseAdapterItemRenameClick(String houseName){
        ChangeHouseNameDialogFragment f = ChangeHouseNameDialogFragment.newInstance(houseName);
        f.show(fragmentManager, null);
    }

    public void onHouseAdapterItemChangePasswordClick(String houseName){
        ChangeHousePasswordDialogFragment f = ChangeHousePasswordDialogFragment.newInstance(houseName);
        f.show(fragmentManager, null);

    }
}

