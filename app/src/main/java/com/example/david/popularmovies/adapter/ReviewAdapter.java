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
import com.example.david.popularmovies.model.Review;
import com.example.david.popularmovies.ui.DetailsActivity;
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

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<Review> reviewList;

    public ReviewAdapter(ArrayList<Review> reviews_array) {
        this.reviewList = reviews_array;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_cell, null);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bindReviewData(reviewList.get(position));
    }


    @Override
    public int getItemCount() {

        return reviewList.size();

    }
    public void setReviewList(ArrayList<Review> reviews){
        reviewList = reviews;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.review_author) TextView review_author;
        @BindView(R.id.review_content) TextView review_content;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bindReviewData(Review data){
            try{
                review_author.setText(data.author());
                review_content.setText(data.content());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}