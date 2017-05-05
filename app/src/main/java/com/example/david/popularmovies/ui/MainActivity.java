package com.example.david.popularmovies.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
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
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_launcher);


         viewPager.setAdapter(new MoviesPagerAdapter(getSupportFragmentManager(),
                 MainActivity.this,getResources().getStringArray(R.array.tab_titles)));
          tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
          tabLayout.setupWithViewPager(viewPager);
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu_movies) {
//        getMenuInflater().inflate(R.menu_movies.menu_movies,menu_movies);
//        return true;
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if(id == R.id.menu_refresh){
//            //new fetchMoviesAsync().execute(SORT_OPTIONS[selected_item]);
//            return true;
//        }else if (id == R.id.menu_sort){
//
//            AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
//            //alt_bld.setIcon(R.drawable.icon);
//            alt_bld.setTitle("Sort by:");
//
//            alt_bld.setSingleChoiceItems(R.array.sort_options, selected_item, new DialogInterface
//                    .OnClickListener() {
//                public void onClick(DialogInterface dialog, int item) {
//
//                    selected_item = item;
//                    if(selected_item==0){
//                        setTitle("Popular Movies");
//                    }else{
//                        setTitle("Top Rated Movies");
//                    }
//                  //  new fetchMoviesAsync().execute(SORT_OPTIONS[selected_item]);
//                    dialog.dismiss();
//
//                }
//            });
//            AlertDialog alert = alt_bld.create();
//            alert.show();
//
//            return true;
//        }else{
//        return true;
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}


