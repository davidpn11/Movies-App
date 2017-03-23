package com.example.david.popularmovies;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private GridView grid;
    final private String[] SORT_OPTIONS = {"popular","top_rated"};

    private int selected_item = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Popular Movies");

        grid = (GridView) findViewById(R.id.gridview);

        new fetchMoviesAsync().execute(SORT_OPTIONS[selected_item]);
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

    class fetchMoviesAsync extends AsyncTask<String, Void, String> {


    public ProgressDialog dialog = new ProgressDialog(MainActivity.this);


    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Please Wait...");
        this.dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        URL movieUrl = NetworkUtils.buildUrl(params[0]);
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
                JSONArray movie_array = new JSONArray(movie_set.getString("results"));

                ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext(),movie_array);
                grid.setAdapter(imageAdapter);

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}

}


