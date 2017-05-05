package com.example.david.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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
import android.widget.Toast;

import com.example.david.popularmovies.R;
import com.example.david.popularmovies.model.Movie;
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

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private ArrayList<Movie> movieList;
    private Context mContext;

    public GridAdapter(Context context,ArrayList<Movie> movies_array) {
        this.movieList = movies_array;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_cardview, null);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindMovieData(movieList.get(position),mContext);
    }

    @Override
    public int getItemCount() {

        return movieList.size();

    }

    public void updateMovies(ArrayList<Movie> array){
        movieList = array;
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        @BindView(R.id.poster_image) ImageView poster_img;
        @BindView(R.id.cv) CardView card_view;
        @BindView(R.id.movie_title) TextView movie_title;

        private Movie movie;
        final private String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185//";

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            card_view.setOnClickListener(this);
        }

        public void bindMovieData(Movie data,Context mContext){
            try{
                movie = data;
                String poster_path = BASE_POSTER_PATH+movie.posterPath();
                Picasso.with(mContext).load(poster_path).resize(dp2px(220), 0).into(poster_img);
                movie_title.setText(movie.title());

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public int dp2px(int dp) {
            WindowManager wm = (WindowManager) mContext
                    .getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics displaymetrics = new DisplayMetrics();
            display.getMetrics(displaymetrics);
            return (int) (dp * displaymetrics.density + 0.5f);
        }

        @Override
        public void onClick(View view) {

            Context context = view.getContext();

            if(movie != null){

                Intent it = new Intent(view.getContext(),DetailsActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();

                b.putParcelable("movie_data",movie);
                it.putExtras(b);
                context.startActivity(it);
            }
        }
    }
}