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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.popularmovies.R;
import com.example.david.popularmovies.adapter.ReviewAdapter;
import com.example.david.popularmovies.adapter.TrailerAdapter;
import com.example.david.popularmovies.model.Movie;
import com.example.david.popularmovies.model.Review;
import com.example.david.popularmovies.model.Trailer;
import com.example.david.popularmovies.utils.FavoritesUtils;
import com.example.david.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;
import com.example.david.popularmovies.db.FavoritesContract.FavoritesEntry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.movie_rating) TextView rating;
    @BindView(R.id.movie_sinopsis) TextView sinopsis;
    @BindView(R.id.release_date) TextView release_date;
    @BindView(R.id.reviews_header) TextView reviews_header;
    @BindView(R.id.trailers_header) TextView trailers_header;
    @BindView(R.id.post_image) ImageView poster_image;
    @BindView(R.id.trailer_progressbar) ProgressBar trailer_progressbar;
    @BindView(R.id.reviews_progressbar) ProgressBar reviews_progressbar;
    @BindView(R.id.reviews_recyclerview) RecyclerView reviewsRecyclerView;
    @BindView(R.id.trailer_recyclerview) RecyclerView trailersRecyclerView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    private MenuItem favoriteIcon;
    private boolean isFavorite;
    final private String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185//";
    private Movie mMovie;

    private ArrayList<Review> mReviewList;
    private ArrayList<Trailer> mTrailerList;

    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent it = getIntent();
        Bundle b = it.getExtras();
        if (b != null) {
            mMovie = b.getParcelable("movie_data");
        }
        try{
            setMovieDetails();
            LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this);
            LinearLayoutManager trailersLayoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL, false);
            reviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
            trailersRecyclerView.setLayoutManager(trailersLayoutManager);

            mReviewList = new ArrayList<>();
            reviewAdapter = new ReviewAdapter(mReviewList);

            mTrailerList = new ArrayList<>();
            trailerAdapter = new TrailerAdapter(getApplicationContext(),mTrailerList);

            reviewsRecyclerView.setAdapter(reviewAdapter);
            trailersRecyclerView.setAdapter(trailerAdapter);

            new fetchReviewsAsync().execute(mMovie.id());
            new fetchTrailersAsync().execute(mMovie.id());


        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
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
        if(id == R.id.menu_share){
            shareMovieTrailer();
        }
        return super.onOptionsItemSelected(item);
    }

    public void shareMovieTrailer(){
        Trailer t = mTrailerList.get(0);
        String url = "http://www.youtube.com/watch?v" + t.source();
        String msg = "Hey! Check out this trailer: "+url;
        Intent it = new Intent(android.content.Intent.ACTION_SEND);
        it.setType("text/plain");
        it.putExtra(android.content.Intent.EXTRA_SUBJECT, "Popular Movies");
        it.putExtra(android.content.Intent.EXTRA_TEXT, msg);
        startActivity(Intent.createChooser(it,getResources().getString(R.string.share_string)));
    }

    public void checkFavorite(){
        try{
            if(FavoritesUtils.checkFavorite(this,mMovie.id())){
                favoriteIcon.setIcon(R.mipmap.ic_favorite);
                isFavorite = true;
            }else{
                isFavorite = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addFavorite(){
        if(FavoritesUtils.addFavorite(getApplicationContext(),mMovie)){
            favoriteIcon.setIcon(R.mipmap.ic_favorite);
            Toast.makeText(this, getResources().getString(R.string.movie_added), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteFavorite(){
        try{
            String stringId = mMovie.id();
            if(FavoritesUtils.deleteFavorite(this,stringId)){
                favoriteIcon.setIcon(R.mipmap.ic_favorite_border);
                Toast.makeText(this,getResources().getString(R.string.movie_deleted), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setMovieDetails(){
        try{
            String rating_text = mMovie.voteAverage()+"/10";

            String date = getDate(mMovie.releaseDate());
            setTitle(mMovie.title());
            rating.setText(rating_text);
            release_date.setText(date);
            sinopsis.setText(mMovie.overview());
            setPoster(poster_image, mMovie.posterPath());
        }catch (Exception e){
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


    private ArrayList<Review> getReviews(JSONArray reviews){
        JSONObject obj;
        ArrayList<Review> list = new ArrayList<>();
        try{
            for(int i = 0; i < reviews.length();i++){
                obj = reviews.getJSONObject(i);
                list.add(Review.create(obj.getString("author"),obj.getString("content")));
            }
            return list;

        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }

    }

    private ArrayList<Trailer> getTrailers(JSONArray trailers){
        JSONObject obj;
        ArrayList<Trailer> list = new ArrayList<>();
        try{
            for(int i = 0; i < trailers.length();i++){
                obj = trailers.getJSONObject(i);
                list.add(Trailer.create(obj.getString("source")));
            }
            return list;

        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }

    }

    private class fetchReviewsAsync extends AsyncTask<String, Void, String> {


            @Override
            protected void onPreExecute() {
                reviews_progressbar.setVisibility(View.VISIBLE);
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

            reviews_progressbar.setVisibility(View.GONE);

            if (moviesDbResults != null && !moviesDbResults.equals("")) {

                try{
                    JSONObject movie_set = new JSONObject(moviesDbResults);

                    mReviewList = getReviews(new JSONArray(movie_set.getString("results")));
                    if(mReviewList.size()> 0) {
                        reviewAdapter.setReviewList(mReviewList);
                        reviewAdapter.notifyDataSetChanged();

                    }else{
                        reviews_header.setText(getResources().getString(R.string.no_reviews));
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }


    private class fetchTrailersAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            trailer_progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            URL movieReviewUrl = NetworkUtils.buildTrailersUrl(params[0]);
            String moviesDbResults = null;
            try {
                moviesDbResults = NetworkUtils.getResponseFromHttpUrl(movieReviewUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("results",moviesDbResults);
            return moviesDbResults;
        }

        protected void onPostExecute(String moviesDbResults) {

            trailer_progressbar.setVisibility(View.GONE);

            if (moviesDbResults != null && !moviesDbResults.equals("")) {

                try{
                    JSONObject movie_set = new JSONObject(moviesDbResults);
                      mTrailerList = getTrailers(new JSONArray(movie_set.getString("youtube")));
                    if(mTrailerList.size() > 0) {
                        trailerAdapter.setTrailerList(mTrailerList);
                        trailerAdapter.notifyDataSetChanged();
                    }else{
                        trailers_header.setText(getResources().getString(R.string.no_trailers));
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }


}
