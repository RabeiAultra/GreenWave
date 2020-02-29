package com.example.aultra.greenwaveproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class activity_profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView uname;
    Button fav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        uname = (TextView)findViewById(R.id.tv_name);
        fav = (Button)findViewById(R.id.btn_fav);

        SharedPreferences readPref = getBaseContext().getSharedPreferences(LoginActivity.MY_SHARED_PREF ,MODE_PRIVATE);
        String user = readPref.getString(LoginActivity.NAME_FLAG, null);
        uname.setText(user);

        fav.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(activity_profile.this,FavoritesActivity.class).setAction(Intent.ACTION_VIEW));
            }
        });

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
        getMenuInflater().inflate(R.menu.activity_profile, menu);
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

        } else if (id == R.id.nav_album) {
            startActivity(new Intent(activity_profile.this,AlbumActivity.class).setAction(Intent.ACTION_VIEW));
        }else if (id == R.id.nav_favorite) {
            startActivity(new Intent(activity_profile.this,FavoritesActivity.class).setAction(Intent.ACTION_VIEW));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(activity_profile.this,SettingActivity.class).setAction(Intent.ACTION_VIEW));
        } else if (id == R.id.nav_logout) {
            SharedPreferences myPref = getBaseContext().getSharedPreferences(LoginActivity.MY_SHARED_PREF, Context.MODE_PRIVATE);
            myPref.edit().clear().commit();
            startActivity(new Intent(activity_profile.this,LoginActivity.class).setAction(Intent.ACTION_VIEW));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
