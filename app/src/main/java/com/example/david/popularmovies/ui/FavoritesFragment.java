package com.example.david.popularmovies.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.david.popularmovies.utils.FavoritesUtils;
import com.example.david.popularmovies.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FavoritesFragment extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    private JSONArray favorite_movies;
    private ArrayList<Movie> favoritesList;
    private GridAdapter mGridAdapter;

    public FavoritesFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_favorites, container, false);
            ButterKnife.bind(this,view);
            setHasOptionsMenu(true);

            favorite_movies = new JSONArray();
            favoritesList = new ArrayList<>();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),getGridSize());
            recyclerView.setLayoutManager(gridLayoutManager);
            mGridAdapter = new GridAdapter(getContext(),favoritesList);
            recyclerView.setAdapter(mGridAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new fetchFavoritesAsync().execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favorites,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_refresh){
            new fetchFavoritesAsync().execute();
        }

        return super.onOptionsItemSelected(item);

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

    private class fetchFavoritesAsync extends AsyncTask<Void,Void,ArrayList<Movie>>{
        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {
            return FavoritesUtils.getAllData(getContext());

        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            favoritesList = movies;
            mGridAdapter.updateMovies(favoritesList);
            mGridAdapter.notifyDataSetChanged();
        }
    }

}
