package com.example.david.popularmovies.ui;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.popularmovies.R;
import com.example.david.popularmovies.adapter.ReviewAdapter;
import com.example.david.popularmovies.adapter.TrailerAdapter;
import com.example.david.popularmovies.utils.FavoritesUtils;
import com.example.david.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;
import com.example.david.popularmovies.db.FavoritesContract.FavoritesEntry;
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
    private MenuItem favoriteIcon;
    private boolean isFavorite;
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
            FavoritesUtils.getAllData(this);
            new fetchReviewsAsync().execute(movie_obj.getString("id"));
            new fetchTrailersAsync().execute(movie_obj.getString("id"));


        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details,menu);
        favoriteIcon = menu.findItem(R.id.menu_favorite);
        checkFavorite();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if(id == R.id.menu_favorite){
            if(isFavorite){
                deleteFavorite();
                isFavorite = false;
            }else{
                addFavorite();
                isFavorite = true;
            }

        }
        return true;
    }

    public void checkFavorite(){
        try{
            if(FavoritesUtils.checkFavorite(this,movie_obj.getString("id"))){
                favoriteIcon.setIcon(R.mipmap.ic_favorite);
                isFavorite = true;
            }else{
                isFavorite = false;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }



    public void addFavorite(){
        ContentValues contentValues = new ContentValues();
        try{
            contentValues.put(FavoritesEntry.COlUMN_MOVIE_ID,movie_obj.getString("id"));
            contentValues.put(FavoritesEntry.COLUMN_TITLE,movie_obj.getString("original_title"));
            contentValues.put(FavoritesEntry.COLUMN_VOTE_AVERAGE,movie_obj.getString("vote_average"));
            contentValues.put(FavoritesEntry.COLUMN_RELEASE_DATE,movie_obj.getString("release_date"));
            contentValues.put(FavoritesEntry.COLUMN_POSTER_PATH,movie_obj.getString("poster_path"));
            Uri uri = getContentResolver().insert(FavoritesEntry.CONTENT_URI, contentValues);
            if(uri != null){
                Toast.makeText(this, "Movie added to Favorites!", Toast.LENGTH_SHORT).show();
            }
            favoriteIcon.setIcon(R.mipmap.ic_favorite);
        }catch (JSONException e){
            e.printStackTrace();

        }

    }


    public void deleteFavorite(){
        try{
            String stringId = movie_obj.getString("id");
            Uri uri = FavoritesEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(stringId).build();
            getContentResolver().delete(uri, null, null);
            favoriteIcon.setIcon(R.mipmap.ic_favorite_border);
            Toast.makeText(this, "Movie deleted from Favorites!", Toast.LENGTH_SHORT).show();
        }catch (JSONException e){
            e.printStackTrace();
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

    private class fetchReviewsAsync extends AsyncTask<String, Void, String> {


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


    private class fetchTrailersAsync extends AsyncTask<String, Void, String> {

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
