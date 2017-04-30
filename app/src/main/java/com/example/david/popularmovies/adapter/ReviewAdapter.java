package com.example.david.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private JSONArray reviews_array;
    private Context mContext;

    public ReviewAdapter(Context context,JSONArray reviews_array) {
        this.reviews_array = reviews_array;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_cell, null);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            final JSONObject review_data = reviews_array.getJSONObject(position);
            holder.bindReviewData(review_data,mContext);


        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        return reviews_array.length();

    }



    class ViewHolder extends RecyclerView.ViewHolder{


        //private CardView card_view;
        private TextView review_author,review_content;

        public ViewHolder(View itemView) {
            super(itemView);

            //card_view = (CardView) itemView.findViewById(R.id.cv);
            review_author= (TextView) itemView.findViewById(R.id.review_author);
            review_content= (TextView) itemView.findViewById(R.id.review_content);
        }

        public void bindReviewData(JSONObject data,Context mContext){
            try{
                review_author.setText(data.getString("author"));
                review_content.setText(data.getString("content"));
            }catch (JSONException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}