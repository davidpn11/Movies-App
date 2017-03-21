package com.example.david.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by david on 20/03/17.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private JSONArray movies_array;
    final private String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185//";

    // Constructor
    public ImageAdapter(Context c,JSONArray movies_array) {
        mContext = c;
        this.movies_array = movies_array;
    }

    public int getCount() {
        return movies_array.length();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }



    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
        }
        else
        {
            imageView = (ImageView) convertView;
        }


        imageView.setLayoutParams(new GridView.LayoutParams(-1, WindowManager.LayoutParams.WRAP_CONTENT));


        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        try{
            final JSONObject movie_data = movies_array.getJSONObject(position);
            //Log.e("Current Movie",movie_data.toString());
            String poster_path = movie_data.getString("poster_path");

            Picasso.with(mContext).load(BASE_POSTER_PATH+poster_path).into(imageView);
            Log.e("listerner","listener");

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext,DetailsActivity.class);
                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    it.putExtra("movie_data",movie_data.toString());
                    mContext.startActivity(it);
                }
            });

        }catch (JSONException e){
            e.printStackTrace();
        }


       // imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }



    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.fight, R.drawable.inter, R.drawable.logan
    };
}