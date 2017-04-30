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
import com.example.david.popularmovies.ui.DetailsActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by david on 25/04/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private JSONArray trailers_array;
    private Context mContext;

    public TrailerAdapter(Context context,JSONArray trailers_array) {
        this.trailers_array = trailers_array;
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
        try {
            final JSONObject trailer_data = trailers_array.getJSONObject(position);
            holder.bindReviewData(trailer_data,mContext);


        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        return trailers_array.length();

    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        //private CardView card_view;

        private ImageView trailer_thumbnail;
        private JSONObject trailer_data;
        Context mContext;
        public ViewHolder(View itemView) {
            super(itemView);

            //card_view = (CardView) itemView.findViewById(R.id.cv);
            trailer_thumbnail= (ImageView) itemView.findViewById(R.id.trailer_thumbnail);
            trailer_thumbnail.setOnClickListener(this);
        }

        public void bindReviewData(JSONObject data,Context context){
            try{
                mContext = context;
                trailer_data = data;
                String thumbnail = generateThumbnail(trailer_data.getString("source"));
                Picasso.with(mContext).load(thumbnail).into(trailer_thumbnail);

            }catch (JSONException e){
                e.printStackTrace();
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

            if(trailer_data != null){
                try {
                    watchYoutubeVideo(trailer_data.getString("source"));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }
}