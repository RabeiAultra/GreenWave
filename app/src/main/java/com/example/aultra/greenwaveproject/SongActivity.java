package com.example.aultra.greenwaveproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SongActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static  String AUDIO_PATH ="";
    private  String url = "http://greenwave.comli.com/api/getSong.php?user_id=1&song_id=";
    static final String ALBUM_ID="ALBUM_ID";

    static  ImageButton buttonPlayPause,buttonFav;
    private int mediaFileLengthInSeconds;
    private MediaPlayer mediaPlayer;
    private int playbackPosition=0;
    private SeekBar songbar;
    private static boolean stop=true,mute=false;
    private PowerManager.WakeLock wakeLock;
    private Song song;
    private String songId;
    private Thread threadBar;
    private Toolbar toolbar;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        PowerManager powerManager = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        wakeLock.acquire();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        synchronized(this) {
            setSupportActionBar(toolbar);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null && bundle.containsKey(SongsActivity.SONG_ID)) {

                songId = bundle.getString(SongsActivity.SONG_ID);
                Log.i("Next_SongActivity",songId);
                url=url+songId;
                Log.i("url", url);

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


            GetSong getSong = new GetSong(url);
            getSong.execute();

    }

    @Override
    protected void onPause(){
        super.onPause();
      //  wakeLock.release();
        buttonPlayPause.setImageResource(android.R.drawable.ic_media_play);
        mediaPlayer.pause();

    }


    public void init(){
        songbar=(SeekBar)findViewById(R.id.songbar);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(AUDIO_PATH);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doClick(View view) {
        switch(view.getId()) {
            case R.id.playMedia:
                try {
                    playAudio(AUDIO_PATH,view);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.muteMedia:
                changeMute(view);break;

        }
    }

    private void handlingLayout(){
        AUDIO_PATH=song.getUrl();
        init();
        final ImageView img =(ImageView)findViewById(R.id.SongImg);
        final String urlImage="http://greenwave.comli.com/images/"+song.getImage();
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    img.setImageBitmap(loadImage(urlImage));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        buttonPlayPause = (ImageButton)findViewById(R.id.playMedia);
        buttonPlayPause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    playAudio(AUDIO_PATH,view);
                } catch (Exception e) {e.printStackTrace();}
            }
        });
        ImageButton btnNext = (ImageButton)findViewById(R.id.nextMedia);
        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SongActivity.this, SongActivity.class);
                String nextId=song.getNext();

                Log.i("Next_SongActivityClick",nextId);
                if(nextId.equals("-1")){
                    Toast.makeText(getApplicationContext(),"Can't move forward",Toast.LENGTH_LONG).show();
                }
                else {

                    finish();
                    overridePendingTransition( 0, 0);
                    intent.putExtra(SongsActivity.SONG_ID, nextId);
                    startActivity(intent);
                    overridePendingTransition( 0, 0);

                }
            }
        });

        ImageButton btnPrev = (ImageButton)findViewById(R.id.prevMedia);
        btnPrev.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SongActivity.this, SongActivity.class);
                String prevId=song.getPrevious();
                if(prevId.equals("-1")) {
                    Toast.makeText(getApplicationContext(),"Can't go back",Toast.LENGTH_LONG).show();
                }
               else
                {
                    finish();
                    overridePendingTransition( 0, 0);
                    intent.putExtra(SongsActivity.SONG_ID, prevId);
                    startActivity(intent);
                    overridePendingTransition( 0, 0);

                }
            }
        });


        songbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public  void onStopTrackingTouch(SeekBar seekBar) {
                int newPosition =songbar.getProgress()/1000;
                mediaPlayer.seekTo(newPosition);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {}

        });
        ImageButton muteButton = (ImageButton) findViewById(R.id.muteMedia);
        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doClick(v);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            public void onCompletion(MediaPlayer player) {
                stop=true;
                buttonPlayPause.setImageResource(android.R.drawable.ic_media_play);
                Log.i("song","stop!");
            }});
        buttonFav = (ImageButton)findViewById(R.id.favMedia);
        if(song.getIsFavorite()){
            buttonFav.setImageResource(R.drawable.fav_on);
            buttonFav.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    removFavorites(view,"1");
                }
            });

        }
        else{
            buttonFav.setImageResource(R.drawable.fav_off);
            buttonFav.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    addFavorites(view,"1");
                }
            });
        }


        TextView txt_title = (TextView)findViewById(R.id.txt_description);
        txt_title.setText(song.getTitle());
    }

    private void playAudio(String url,View view) throws Exception
    {
        buttonPlayPause= (ImageButton)view;
        if(!mediaPlayer.isPlaying()){
            Log.i("song","Start!");
            if(stop) { //If song change the seek bar to beginning
                Log.i("song","Restart!");
                mediaPlayer.seekTo(playbackPosition);
            }
            mediaPlayer.start();
            //Thread to move seekbar while music playing
            threadBar = new Thread( new ThreadSeekBarChange());
            threadBar.start();
            //Change to pause icon after click on start
            buttonPlayPause.setImageResource(android.R.drawable.ic_media_pause);
            stop=false;
        }else {
            Log.i("song","pause!");
            mediaPlayer.pause();
            //Change to play icon after click on pause
            buttonPlayPause.setImageResource(android.R.drawable.ic_media_play);
        }


    }


        //Mute and UnMute the Song
    private void changeMute(View view){
        ImageButton btn_mute =(ImageButton)view;
        Log.i("song","change Mute!");
        if(mute){
            btn_mute.setImageResource(android.R.drawable.ic_lock_silent_mode);
            Log.i("song","UnMute");
            mediaPlayer.setVolume(0.5f,0.5f);mute=false;
        }
        else { Log.i("song","Mute");mediaPlayer.setVolume(0,0);
            btn_mute.setImageResource(android.R.drawable.ic_lock_silent_mode_off);

            mute=true;}
    }

    //Add Song to Favorite
    private void addFavorites(View view,String id){
        ImageButton btn_fav =(ImageButton)view;
        btn_fav.setImageResource(R.drawable.fav_on);
        HttpHandler sh = new HttpHandler();
        String urlFav="http://greenwave.comli.com/api/addFavorites.php?user_id="+id+"&song_id="+songId;
        sh.makeServiceCall(urlFav);
    }
    //remove Song from Favorite
    private void removFavorites(View view,String id){
        ImageButton btn_fav =(ImageButton)view;
        btn_fav.setImageResource(R.drawable.fav_off);
        HttpHandler sh = new HttpHandler();
        String urlFav="http://greenwave.comli.com/api/removeFavorites.php?user_id="+id+"&song_id="+songId;
        sh.makeServiceCall(urlFav);
        Toast.makeText(getApplicationContext(),
               "Remove song from favorites done",
                Toast.LENGTH_LONG)
                .show();
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
        getMenuInflater().inflate(R.menu.album, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.OK) {
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
            startActivity(new Intent(SongActivity.this,activity_profile.class).setAction(Intent.ACTION_VIEW));
        } else if (id == R.id.nav_album) {
            startActivity(new Intent(SongActivity.this,AlbumActivity.class).setAction(Intent.ACTION_VIEW));
        }else if (id == R.id.nav_favorite) {
            startActivity(new Intent(SongActivity.this,FavoritesActivity.class).setAction(Intent.ACTION_VIEW));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(SongActivity.this,SettingActivity.class).setAction(Intent.ACTION_VIEW));
        } else if (id == R.id.nav_logout) {
            SharedPreferences myPref = getBaseContext().getSharedPreferences(LoginActivity.MY_SHARED_PREF, Context.MODE_PRIVATE);
            myPref.edit().clear().commit();
            startActivity(new Intent(SongActivity.this,LoginActivity.class).setAction(Intent.ACTION_VIEW));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class GetSong extends AsyncTask<Void, Void, Void> {
        private String url;
        private ProgressDialog pDialog;
        private String TAG = SongActivity.class.getSimpleName();

        // URL to get contacts JSON
        GetSong(String url){
            this.url=url;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SongActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONObject c= jsonObj.getJSONObject("Song");

                        String id = c.getString("id");
                        String title = c.getString("title");
                        String image = c.getString("image");
                        String description = c.getString("description");
                        String url = c.getString("url");
                        String duration = c.getString("description");
                        String next=c.getString("next");
                        String previous=c.getString("previous");
                        String isFavorite=c.getString("isFavorite");
                         song =new Song(id,title,image,description,url,duration,next,previous,isFavorite);
                            Log.i("Object: ",song.toString());
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
            handlingLayout();
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

        }

    }

    /*SeekBar Thread      */
    class ThreadSeekBarChange implements Runnable{
        @Override
        public void run() {
            int currentPosition = 0;
            mediaFileLengthInSeconds = mediaPlayer.getDuration()*1000;
            Log.i("bar",""+mediaFileLengthInSeconds);
            songbar.setMax(mediaFileLengthInSeconds);
            while(true){
                try {
                    Thread.sleep(1000);
                    currentPosition = mediaPlayer.getCurrentPosition()*1000;
                    songbar.setProgress(currentPosition);

                    Log.i("Song",currentPosition+"_"+mediaFileLengthInSeconds);
                } catch (InterruptedException e) {
                    return;
                } catch (Exception e) {
                    return;
                }
            }
        }

    }
     /*---------------------------------------------------------------------------*/

    public Bitmap loadImage(String u){
        InputStream in = null;
        try
        {
            URL url = new URL(u);
            URLConnection urlConn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.connect();

            in = httpConn.getInputStream();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(in);
    }
}


