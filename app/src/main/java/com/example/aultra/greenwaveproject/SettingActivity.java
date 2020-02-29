package com.example.aultra.greenwaveproject;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

public class SettingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String NOTIFICATION_FLAG = "notification";

    Switch notification;
    EditText username;
    EditText password;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //
        username = (EditText)findViewById(R.id.et_username);
        password = (EditText)findViewById(R.id.et_password);
        notification = (Switch)findViewById(R.id.switch1);
        update = (Button)findViewById(R.id.btn_update);

        //save the values in shared preferences
        setViewsValues();

        update.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String name = username.getText().toString();
                String pass = password.getText().toString();
                String not = notification.getText().toString();
                if(name == null || name.isEmpty() || pass == null || pass.isEmpty())return;
                else
                {
                    //save the values in shared preferences
                    SharedPreferences myPref = getBaseContext().getSharedPreferences(LoginActivity.MY_SHARED_PREF , Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPref.edit();
                    editor.putString(LoginActivity.NAME_FLAG,name);
                    editor.putString(LoginActivity.PASS_FLAG,pass);
                    editor.commit();
                    //go to album activity
                    startActivity(new Intent(SettingActivity.this,AlbumActivity.class).setAction(Intent.ACTION_VIEW));
                }
            }
        });


        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                //save the values in shared preferences
                SharedPreferences myPref = getBaseContext().getSharedPreferences(LoginActivity.MY_SHARED_PREF , Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = myPref.edit();
                editor.putBoolean(NOTIFICATION_FLAG , isChecked);
                editor.commit();

            }
        });

    }

    protected  void setViewsValues() {
        SharedPreferences readPref = getBaseContext().getSharedPreferences(LoginActivity.MY_SHARED_PREF ,MODE_PRIVATE);
        boolean defaultValue = false;
        boolean notif = readPref.getBoolean(NOTIFICATION_FLAG, defaultValue);
        notification.setChecked(notif);
        String user = readPref.getString(LoginActivity.NAME_FLAG, "null");
        username.setText(user);
        String pas = readPref.getString(LoginActivity.PASS_FLAG, "null");
        password.setText(pas);
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
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(SettingActivity.this,activity_profile.class).setAction(Intent.ACTION_VIEW));
        } else if (id == R.id.nav_album) {
            startActivity(new Intent(SettingActivity.this,AlbumActivity.class).setAction(Intent.ACTION_VIEW));
        }else if (id == R.id.nav_favorite) {
            startActivity(new Intent(SettingActivity.this,FavoritesActivity.class).setAction(Intent.ACTION_VIEW));
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {
            SharedPreferences myPref = getBaseContext().getSharedPreferences(LoginActivity.MY_SHARED_PREF,MODE_PRIVATE);
            myPref.edit().clear().commit();
            startActivity(new Intent(SettingActivity.this,LoginActivity.class).setAction(Intent.ACTION_VIEW));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

