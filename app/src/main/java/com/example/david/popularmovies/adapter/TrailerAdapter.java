package com.example.david.popularmovies.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.popularmovies.R;
import com.example.david.popularmovies.model.Trailer;
import com.example.david.popularmovies.ui.DetailsActivity;
import com.example.david.popularmovies.ui.MainActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by david on 25/04/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private ArrayList<Trailer> trailerList;
    private Context mContext;

    public TrailerAdapter(Context context,ArrayList<Trailer> trailers_array) {
        this.trailerList = trailers_array;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_cell, null);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bindTrailerData(trailerList.get(position),mContext);
    }

    @Override
    public int getItemCount() {

        return trailerList.size();

    }

    public void setTrailerList(ArrayList<Trailer> reviews){
        trailerList = reviews;
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.trailer_thumbnail) ImageView trailer_thumbnail;
        Trailer trailer;
        Context mContext;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            trailer_thumbnail.setOnClickListener(this);
        }

        public void bindTrailerData(Trailer data,Context context){
            try{
                mContext = context;
                trailer = data;
                String thumbnail = generateThumbnail(trailer.source());
                Picasso.with(mContext).load(thumbnail).into(trailer_thumbnail);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        protected String generateThumbnail(String id) {
            String url = "http://img.youtube.com/vi/" + id + "/mqdefault.jpg";
            return url;
        }

        public void watchYoutubeVideo(String id) {

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + id));
                mContext.startActivity(intent);
            }

        @Override
        public void onClick(View view) {

            if(trailer != null){
                try {
                    watchYoutubeVideo(trailer.source());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }
}