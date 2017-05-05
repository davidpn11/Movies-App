package com.example.david.popularmovies.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.example.david.popularmovies.db.FavoritesContract.FavoritesEntry;
import com.example.david.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by david on 30/04/17.
 */

public class FavoritesUtils {


    public static ArrayList<Movie> getAllData(Context context) {

        try {
            Cursor cursor = context.getContentResolver().query(FavoritesEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    FavoritesEntry.COlUMN_MOVIE_ID);

            int id_index = cursor.getColumnIndex(FavoritesEntry.COlUMN_MOVIE_ID);
            int title_index = cursor.getColumnIndex(FavoritesEntry.COLUMN_TITLE);
            int release_date_index = cursor.getColumnIndex(FavoritesEntry.COLUMN_RELEASE_DATE);
            int vote_average_index = cursor.getColumnIndex(FavoritesEntry.COLUMN_VOTE_AVERAGE);
            int poster_path_index = cursor.getColumnIndex(FavoritesEntry.COLUMN_POSTER_PATH);
            int overview_index = cursor.getColumnIndex(FavoritesEntry.COLUMN_OVERVIEW);
            ArrayList<Movie> list = new ArrayList<>();

            JSONArray favs_array = new JSONArray();
            while (cursor.moveToNext()) {
                /*JSONObject fav_obj = new JSONObject();
                fav_obj.put("id", cursor.getInt(id_index));
                fav_obj.put("original_title", cursor.getString(title_index));
                fav_obj.put("release_date", cursor.getString(release_date_index));
                fav_obj.put("vote_average", cursor.getString(vote_average_index));
                fav_obj.put("poster_path", cursor.getString(poster_path_index));
                fav_obj.put("overview", cursor.getString(overview_index));
                favs_array.put(fav_obj);*/
                Movie m = Movie.create(cursor.getString(id_index),cursor.getString(title_index),
                        cursor.getString(release_date_index),cursor.getString(vote_average_index),
                        cursor.getString(poster_path_index),cursor.getString(overview_index));
                list.add(m);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean checkFavorite(Context context, String movie_id){

        String selection = FavoritesEntry.COlUMN_MOVIE_ID+" = '"+movie_id+"'";
        Cursor cursor = context.getContentResolver().query(FavoritesEntry.CONTENT_URI,
                null,
                selection,
                null,
                FavoritesEntry.COlUMN_MOVIE_ID
        );

        if(cursor.getCount()>0){
            return true;
        }else {
            return false;
        }
    }


    public static boolean addFavorite(Context context, Movie movie_obj){
        ContentValues contentValues = new ContentValues();
        try{

            contentValues.put(FavoritesEntry.COlUMN_MOVIE_ID,movie_obj.id());
            contentValues.put(FavoritesEntry.COLUMN_TITLE,movie_obj.title());
            contentValues.put(FavoritesEntry.COLUMN_VOTE_AVERAGE,movie_obj.voteAverage());
            contentValues.put(FavoritesEntry.COLUMN_RELEASE_DATE,movie_obj.releaseDate());
            contentValues.put(FavoritesEntry.COLUMN_POSTER_PATH,movie_obj.posterPath());
            contentValues.put(FavoritesEntry.COLUMN_OVERVIEW,movie_obj.overview());
            Log.v("Content values", contentValues.toString());

            Uri uri = context.getContentResolver().insert(FavoritesEntry.CONTENT_URI, contentValues);
            if(uri != null){
                return true;
            }else{
                throw new Exception();
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteFavorite(Context context, String id){

            Uri uri = FavoritesEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(id).build();
            context.getContentResolver().delete(uri, null, null);
            return true;
    }
}
