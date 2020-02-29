package com.example.aultra.greenwaveproject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;



public class AlbumAdapter extends BaseAdapter {

    ArrayList<Album> list;


    Context context;
    public AlbumAdapter(Context c,ArrayList<Album> list){
        this.list=list;
        context=c;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int postion) {
        return postion;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.album_listview,parent,false);

        TextView title = (TextView) row.findViewById(R.id.singer_name);
        TextView num_of_songs = (TextView) row.findViewById(R.id.num_of_songs);
        TextView id = (TextView) row.findViewById(R.id.txt_album_id);
        final ImageView  img = (ImageView) row.findViewById(R.id.img);

        Album temp =list.get(position);
        title.setText(temp.getTitle());
        num_of_songs.setText(temp.getNum_of_songs());
        id.setText(temp.getId());
        final String url="http://greenwave.comli.com/images/"+temp.getImage();
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    img.setImageBitmap(loadImage(url));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        return row;
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
