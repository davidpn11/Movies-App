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


    private static String SORT_POPULAR = "popular";
    private static String SORT_TOP_RATED = "top";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView grid = (GridView) findViewById(R.id.gridview);

        grid.setAdapter(new ImageAdapter(this));

        new fetchMoviesAsync().execute(SORT_POPULAR);
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

            return true;
        }else if (id == R.id.menu_sort){

            AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
            //alt_bld.setIcon(R.drawable.icon);
            alt_bld.setTitle("Sort by:");
            final String[] sort_name = {"Popular","Top Rated"};
            alt_bld.setSingleChoiceItems(R.array.sort_options, -1, new DialogInterface
                    .OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    Toast.makeText(getApplicationContext(),
                            "Group Name = "+sort_name[item], Toast.LENGTH_SHORT).show();
                    dialog.dismiss();// dismiss the alertbox after chose option

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
        Log.v("URL",movieUrl.toString());
        String moviesDbResults = null;
        try {
            moviesDbResults = NetworkUtils.getResponseFromHttpUrl(movieUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("Result",moviesDbResults);
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
                Log.v("Result",movie_array.length()+"");


            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}

}


