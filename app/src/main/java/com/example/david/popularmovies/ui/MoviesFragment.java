package com.example.david.popularmovies.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.david.popularmovies.R;
import com.example.david.popularmovies.adapter.GridAdapter;
import com.example.david.popularmovies.model.Movie;
import com.example.david.popularmovies.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MoviesFragment extends Fragment {


    private JSONArray mMoviesArray;
    private ArrayList<Movie> moviesList;
    private GridAdapter mGridAdapter;
    private SharedPreferences sharedPref;
    private String sort_key;
    private int selected_item;
    final private String[] SORT_OPTIONS = {"popular","top_rated"};
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    public MoviesFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);

        startSortOption();

        mMoviesArray = new JSONArray();
        moviesList = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),getGridSize());

        recyclerView.setLayoutManager(gridLayoutManager);
        mGridAdapter = new GridAdapter(getContext(),moviesList);
        recyclerView.setAdapter(mGridAdapter);

        new fetchMoviesAsync().execute(sharedPref.getString(sort_key,"popular"));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_refresh){
            new fetchMoviesAsync().execute(sharedPref.getString(sort_key,"popular"));
        }else if (id == R.id.menu_sort){

            AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());
            //alt_bld.setIcon(R.drawable.icon);
            alt_bld.setTitle("Sort by:");

            alt_bld.setSingleChoiceItems(R.array.sort_options, selected_item, new DialogInterface
                    .OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    setSortOption(item);
                    if(item == 0){
                        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Popular Movies");
                    }else{
                        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Top Rated Movies");
                    }


                    new fetchMoviesAsync().execute(sharedPref.getString(sort_key,"popular"));
                    dialog.dismiss();

                }
            });
            AlertDialog alert = alt_bld.create();
            alert.show();

            return true;
        }else{
        return true;
        }

        return super.onOptionsItemSelected(item);

    }

    public void startSortOption(){
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        sort_key = getResources().getString(R.string.shared_pref_sort);
        String sort_opt = sharedPref.getString(sort_key,"");

        if(sort_opt.length() == 0){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(sort_key, "popular");
            selected_item = 0;
            editor.apply();
        }
    }

    public void setSortOption(int item){
        SharedPreferences.Editor editor = sharedPref.edit();
        selected_item = item;
        if(item ==0){
            editor.putString(sort_key, "popular");
        }else{
            editor.putString(sort_key, "top_rated");
        }
        editor.apply();

    }

    public int getGridSize(){
        int orientation = getContext().getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            return 2;
        }
        else{
            return 3;
        }
    }
    private ArrayList<Movie> getMovies(JSONArray movies){
        JSONObject obj;
        ArrayList<Movie> list = new ArrayList<>();
        try{
            for(int i = 0; i < movies.length();i++){
                obj = movies.getJSONObject(i);
                Movie m = Movie.create(obj.getString("id"),obj.getString("original_title"),
                        obj.getString("release_date"),obj.getString("vote_average"),
                        obj.getString("poster_path"),obj.getString("overview"));
                list.add(m);
            }
            return list;

        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }

    }

    private class fetchMoviesAsync extends AsyncTask<String, Void, String> {


        public ProgressDialog dialog = new ProgressDialog(getContext());


        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please Wait...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            URL movieUrl = NetworkUtils.buildBaseUrl(params[0]);
            String moviesDbResults = null;
            try {
                moviesDbResults = NetworkUtils.getResponseFromHttpUrl(movieUrl);
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
                    JSONObject movie_set = new JSONObject(moviesDbResults);

                    mMoviesArray = new JSONArray(movie_set.getString("results"));
                    moviesList = getMovies(new JSONArray(movie_set.getString("results")));
                    mGridAdapter.updateMovies(moviesList);
                    mGridAdapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
