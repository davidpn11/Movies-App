package com.example.david.popularmovies.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by david on 03/05/17.
 */

public class MoviesPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[];
    private Context context;

    public MoviesPagerAdapter(FragmentManager fm, Context context,String[] titles) {
        super(fm);
        this.context = context;
        tabTitles = titles;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        //return MoviesFragment.newInstance();
        if(position == 0){
            return new MoviesFragment();
        }else {
            return new FavoritesFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}