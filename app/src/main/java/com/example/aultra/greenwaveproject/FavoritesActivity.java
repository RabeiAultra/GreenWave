package com.example.aultra.greenwaveproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,  AdapterView.OnItemClickListener  {


    private ListView list1;
    ArrayList<Song> songsList;
    private static String url = "http://greenwave.comli.com/api/getFavorites.php?id=1";
    static final String SONG_ID = "SONG_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        GetSongs a = new GetSongs(url);
        a.execute();
        list1 = (ListView) findViewById(R.id.list1);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
        getMenuInflater().inflate(R.menu.activity_favorites, menu);
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
            startActivity(new Intent(FavoritesActivity.this,activity_profile.class).setAction(Intent.ACTION_VIEW));
        } else if (id == R.id.nav_album) {
            startActivity(new Intent(FavoritesActivity.this,AlbumActivity.class).setAction(Intent.ACTION_VIEW));
        }else if (id == R.id.nav_favorite) {
            startActivity(new Intent(FavoritesActivity.this,FavoritesActivity.class).setAction(Intent.ACTION_VIEW));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(FavoritesActivity.this,SettingActivity.class).setAction(Intent.ACTION_VIEW));
        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(FavoritesActivity.this,LoginActivity.class).setAction(Intent.ACTION_VIEW));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Song item=  (Song)list1.getItemAtPosition(position);
        Toast.makeText(getBaseContext(), "you choose "+item.getTitle()+" songs", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(FavoritesActivity.this, SongActivity.class);
        intent.putExtra(SONG_ID,item.getId());
        startActivity(intent);
    }



    private class GetSongs extends AsyncTask<Void, Void, Void> {

        private String url;
        private ProgressDialog pDialog;
        private String TAG = SongsActivity.class.getSimpleName();

        // URL to get contacts JSON
        GetSongs(String url) {
            Log.i("order","Second");
            this.url = url;
            Log.i("url",url);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(FavoritesActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            HttpHandler sh = new HttpHandler();
            songsList = new ArrayList<>();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("Favorites");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("id");
                        String title = c.getString("title");
                        String image = c.getString("image");
                        String duration = c.getString("duration");

                        songsList.add(new Song(id, title, image, duration));

                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();


            SongsAdapter adapter1 = new SongsAdapter(FavoritesActivity.this,songsList);
            list1.setAdapter(adapter1);
            list1.setOnItemClickListener(FavoritesActivity.this);

            for (int i = 0; i <  songsList.size(); i++) {

                Log.i("Array",  songsList.get(i).toString());
            }

        }
    }
}
