package com.example.david.popularmovies.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.david.popularmovies.adapter.GridAdapter;
import com.example.david.popularmovies.utils.NetworkUtils;
import com.example.david.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{


    //@BindView(R.id.gridview) GridView grid;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    final private String[] SORT_OPTIONS = {"popular","top_rated"};


    private int selected_item = 0;
    private JSONArray mMoviesArray;
    private GridAdapter mGridAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Popular Movies");
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);



        mMoviesArray = new JSONArray();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,getGridSize());
        recyclerView.setLayoutManager(gridLayoutManager);

        mGridAdapter = new GridAdapter(this,mMoviesArray);
        recyclerView.setAdapter(mGridAdapter);
        new fetchMoviesAsync().execute(SORT_OPTIONS[selected_item]);
    }



    public int getGridSize(){
        int orientation=this.getResources().getConfiguration().orientation;
        if(orientation== Configuration.ORIENTATION_PORTRAIT){
           return 2;
        }
        else{
            return 3;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_refresh){
            new fetchMoviesAsync().execute(SORT_OPTIONS[selected_item]);
            return true;
        }else if (id == R.id.menu_sort){

            AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
            //alt_bld.setIcon(R.drawable.icon);
            alt_bld.setTitle("Sort by:");

            alt_bld.setSingleChoiceItems(R.array.sort_options, selected_item, new DialogInterface
                    .OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {

                    selected_item = item;
                    if(selected_item==0){
                        setTitle("Popular Movies");
                    }else{
                        setTitle("Top Rated Movies");
                    }
                    new fetchMoviesAsync().execute(SORT_OPTIONS[selected_item]);
                    dialog.dismiss();

                }
            });
            AlertDialog alert = alt_bld.create();
            alert.show();

            return true;
        }else{
        return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    class fetchMoviesAsync extends AsyncTask<String, Void, String> {


        public ProgressDialog dialog = new ProgressDialog(MainActivity.this);


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
                   // mGridAdapter.notifyDataSetChanged();
                    mGridAdapter = new GridAdapter(getApplicationContext(),mMoviesArray);
                    recyclerView.setAdapter(mGridAdapter);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }

}


