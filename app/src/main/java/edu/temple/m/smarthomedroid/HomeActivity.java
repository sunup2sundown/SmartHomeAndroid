package edu.temple.m.smarthomedroid;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
UserSettingsFragment.OnFragment4AttachedListener {
    //set up 4 fragments, we're going to use them and not adding new every time user click on items
    final SensorFragment frag = new SensorFragment();
    final RelayFragment frag2 = new RelayFragment();
    final ConfigFragment frag3 = new ConfigFragment();
    final UserSettingsFragment frag4 = new UserSettingsFragment();
    private TextView greet;
    String userid;
    JSONObject dta = new JSONObject();
    JSONObject dta2 = new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_home);
        //hard-coding dummy data
        try {
            dta.put("ss1", 60);
            dta.put("ss2", 72);
            dta.put("ss3",1000000);
            dta.put("ss4",1000000);
            dta.put("ss5",2000000);
            dta.put("ss6",1000000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            dta2.put("ss1",0);
            dta2.put("ss2",1);
            dta2.put("ss3",0);
            dta2.put("ss4",0);
            dta2.put("ss5",1);
            dta2.put("ss6",1);
            dta2.put("ss7",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //get toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");

        //set up greeting string based on user device's time setting
        greet = (TextView)findViewById(R.id.greeting);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if(timeOfDay >= 0 && timeOfDay < 12){
            greet.setText("Good morning "+userid);
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            greet.setText("Good afternoon "+userid);
        }else if(timeOfDay >= 16 && timeOfDay < 20){
            greet.setText("Good evening "+userid);
        }else if(timeOfDay >= 20 && timeOfDay < 24){
            greet.setText("Good night "+userid);
        }

        //pass json object to fragments
        Bundle bundle1=new Bundle();
        bundle1.putString("data",dta.toString());
        frag.setArguments(bundle1);

        Bundle bundle2=new Bundle();
        bundle2.putString("data2",dta2.toString());
        frag2.setArguments(bundle2);
        FragmentManager fra = getFragmentManager();

        //add all fragments here with tags, but only show sensors fragment first , which is fragment1
        fra.beginTransaction().add(R.id.contentframe,frag4,"setting");
        fra.beginTransaction().add(R.id.contentframe,frag3,"config");
        fra.beginTransaction().add(R.id.contentframe,frag2,"relays");
        fra.beginTransaction().add(R.id.contentframe,frag,"sensors").commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        TextView name = (TextView)header.findViewById(R.id.Name);
        name.setText(userid+"'s SmartHome GateWay");
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.voice){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentmanager = getFragmentManager();
        if (id == R.id.sensor) {
            //check if the sensor fragment in the list, if not create new and add
            if(fragmentmanager.findFragmentByTag("sensors")==null){
                Bundle bundle1=new Bundle();
                bundle1.putString("data",dta.toString());
                frag.setArguments(bundle1);
                fragmentmanager.beginTransaction().add(R.id.contentframe,frag,"sensors").commit();
            }else{
                //if it's in the list, just show it and hide others
                fragmentmanager.beginTransaction().show(frag).commit();
                fragmentmanager.beginTransaction().hide(frag2).commit();
                fragmentmanager.beginTransaction().hide(frag3).commit();
                fragmentmanager.beginTransaction().hide(frag4).commit();
            }
        } else if (id == R.id.relay) {
            //same thing as sensor fragment above
            if(fragmentmanager.findFragmentByTag("relays")==null){
                Bundle bundle2=new Bundle();
                bundle2.putString("data2",dta2.toString());
                frag2.setArguments(bundle2);
                fragmentmanager.beginTransaction().add(R.id.contentframe,frag2,"relays").commit();
            }else{
                fragmentmanager.beginTransaction().hide(frag).commit();
                fragmentmanager.beginTransaction().show(frag2).commit();
                fragmentmanager.beginTransaction().hide(frag3).commit();
                fragmentmanager.beginTransaction().hide(frag4).commit();
            }

        } else if (id == R.id.config) {
            fragmentmanager.beginTransaction().hide(frag).commit();
            fragmentmanager.beginTransaction().hide(frag2).commit();
            fragmentmanager.beginTransaction().show(frag3).commit();
            fragmentmanager.beginTransaction().hide(frag4).commit();
        } else if (id == R.id.setting) {
            if(fragmentmanager.findFragmentByTag("relays")==null){
                Bundle bundle4=new Bundle();
                bundle4.putString("userid",userid);
                frag4.setArguments(bundle4);
                fragmentmanager.beginTransaction().add(R.id.contentframe,frag4,"setting").commit();
            }else{
                fragmentmanager.beginTransaction().hide(frag).commit();
                fragmentmanager.beginTransaction().hide(frag2).commit();
                fragmentmanager.beginTransaction().hide(frag3).commit();
                fragmentmanager.beginTransaction().show(frag4).commit();
            }
        } else if (id == R.id.out) {
            //go back to login activity, finish current activity
            Intent myIntentOut = new Intent(HomeActivity.this, LoginActivity.class);
            finish();
            startActivity(myIntentOut);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public String getUsername(){
        return userid;
    }

}

