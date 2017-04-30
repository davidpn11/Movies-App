package com.example.david.popularmovies.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.popularmovies.R;
import com.example.david.popularmovies.adapter.GridAdapter;
import com.example.david.popularmovies.adapter.ReviewAdapter;
import com.example.david.popularmovies.adapter.TrailerAdapter;
import com.example.david.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.movie_rating) TextView rating;
    @BindView(R.id.movie_sinopsis) TextView sinopsis;
    @BindView(R.id.release_date) TextView release_date;
    @BindView(R.id.reviews_header) TextView reviews_header;
    @BindView(R.id.trailers_header) TextView trailers_header;
    @BindView(R.id.post_image) ImageView poster_image;
    @BindView(R.id.reviews_recyclerview) RecyclerView reviewsRecyclerView;
    @BindView(R.id.trailer_recyclerview) RecyclerView trailersRecyclerView;

    final private String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185//";
    private JSONObject movie_obj;
    private JSONArray mMovieReviews,mMoviesTrailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        Intent it = getIntent();
        String movie_data = it.getStringExtra("movie_data");

        try{
            movie_obj = new JSONObject(movie_data);
            setMovieDetails();
            LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this);
            LinearLayoutManager trailersLayoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL, false);
            reviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
            trailersRecyclerView.setLayoutManager(trailersLayoutManager);

            new fetchReviewsAsync().execute(movie_obj.getString("id"));
            new fetchTrailersAsync().execute(movie_obj.getString("id"));

        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setMovieDetails(){
        try{
            String rating_text = movie_obj.getString("vote_average")+"/10";

            String date = getDate(movie_obj.getString("release_date"));
            setTitle(movie_obj.getString("original_title"));
            rating.setText(rating_text);
            release_date.setText(date);
            sinopsis.setText(movie_obj.getString("overview"));
            setPoster(poster_image, movie_obj.getString("poster_path"));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    private void setPoster(ImageView poster_image, String poster_path){
        Picasso.with(this).load(BASE_POSTER_PATH+poster_path).into(poster_image);
    }

    private String getDate(String date){

        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8,date.length());

        return  month+"/"+day+"/"+year;
    }

    class fetchReviewsAsync extends AsyncTask<String, Void, String> {


            public ProgressDialog dialog = new ProgressDialog(DetailsActivity.this);


            @Override
            protected void onPreExecute() {
                this.dialog.setMessage("Please Wait...");
                this.dialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
            URL movieReviewUrl = NetworkUtils.buildReviewsUrl(params[0]);
            String moviesDbResults = null;
            try {
                moviesDbResults = NetworkUtils.getResponseFromHttpUrl(movieReviewUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Result",moviesDbResults);
            return moviesDbResults;
        }

        protected void onPostExecute(String moviesDbResults) {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (moviesDbResults != null && !moviesDbResults.equals("")) {

                try{
                    Log.v("REVIEWS",moviesDbResults);
                    JSONObject movie_set = new JSONObject(moviesDbResults);

                    mMovieReviews = new JSONArray(movie_set.getString("results"));
                    if(mMovieReviews.length()> 0) {
                        ReviewAdapter reviewAdapter = new ReviewAdapter(getApplicationContext(), mMovieReviews);
                        reviewsRecyclerView.setAdapter(reviewAdapter);
                    }else{
                        reviews_header.setText("This movie don't have reviews!");
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }


    class fetchTrailersAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            URL movieReviewUrl = NetworkUtils.buildTrailersUrl(params[0]);
            String moviesDbResults = null;
            try {
                moviesDbResults = NetworkUtils.getResponseFromHttpUrl(movieReviewUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Result",moviesDbResults);
            return moviesDbResults;
        }

        protected void onPostExecute(String moviesDbResults) {

            if (moviesDbResults != null && !moviesDbResults.equals("")) {

                try{
                    Log.v("TRAILERS",moviesDbResults);
                    JSONObject movie_set = new JSONObject(moviesDbResults);

                    mMoviesTrailers = new JSONArray(movie_set.getString("youtube"));

                    if(mMoviesTrailers.length() > 0) {
                        TrailerAdapter trailerAdapter = new TrailerAdapter(getApplicationContext(), mMoviesTrailers);
                        trailersRecyclerView.setAdapter(trailerAdapter);
                    }else{
                        trailers_header.setText("This movie don't have trailers!");
                    }



                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }


}
