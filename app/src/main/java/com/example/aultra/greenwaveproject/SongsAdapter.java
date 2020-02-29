package com.example.aultra.greenwaveproject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static com.example.aultra.greenwaveproject.AlbumActivity.ALBUM_ID;


public class SongsAdapter extends BaseAdapter implements ListView.OnItemClickListener{
    final String SONGS_ID="SONGS_ID";

    ArrayList<Song> list1;

    Context context;

    public SongsAdapter(Context c,ArrayList<Song> list1){
        this.list1=list1;
        context=c;
    }
    @Override
    public int getCount() {
        return list1.size();
    }

    @Override
    public Object getItem(int position) {
        return list1.get(position);
    }

    @Override
    public long getItemId(int postion) {
        return postion;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.song_listview,parent,false);
      //   TextView id = (TextView) row.findViewById(R.id.id);
        TextView title = (TextView) row.findViewById(R.id.song_name);
        TextView duration = (TextView) row.findViewById(R.id.period_of_songs);
        final ImageView img1 = (ImageView) row.findViewById(R.id.img1);


        Song temp =list1.get(position);
        title.setText(temp.getTitle());
        duration.setText(temp.getDuration());
        final String url1="http://greenwave.comli.com/images/"+temp.getImage();
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    img1.setImageBitmap(loadImage(url1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        return row;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
/*
        Songs item=  (Songs) adapterView.getItemAtPosition(position);

        Log.i("SONGS_ID",item.getID());

    /*    Intent intent = new Intent(SongsAdapter.this, SongActivity.class);
        intent.putExtra(SONGS_ID,item.getID());
        startActivity(intent);
        */
    }








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