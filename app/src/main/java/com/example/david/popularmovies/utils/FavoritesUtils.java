package com.example.david.popularmovies.utils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.david.popularmovies.db.FavoritesContract.FavoritesEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by david on 30/04/17.
 */

public class FavoritesUtils {


    public static void getAllData(Context context) {

        try {
            Cursor cursor = context.getContentResolver().query(FavoritesEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    FavoritesEntry.COlUMN_MOVIE_ID);

            Log.e("cursor", cursor.toString());
            int id_index = cursor.getColumnIndex(FavoritesEntry.COlUMN_MOVIE_ID);
            int title_index = cursor.getColumnIndex(FavoritesEntry.COLUMN_TITLE);
            int release_date_index = cursor.getColumnIndex(FavoritesEntry.COLUMN_RELEASE_DATE);
            int vote_average_index = cursor.getColumnIndex(FavoritesEntry.COLUMN_VOTE_AVERAGE);
            int poster_path_index = cursor.getColumnIndex(FavoritesEntry.COlUMN_MOVIE_ID);


            JSONArray favs_array = new JSONArray();
            while (cursor.moveToNext()) {
                JSONObject fav_obj = new JSONObject();
                fav_obj.put("id", cursor.getInt(id_index));
                fav_obj.put("title", cursor.getString(title_index));
                fav_obj.put("release_date", cursor.getString(release_date_index));
                fav_obj.put("vote_average", cursor.getString(vote_average_index));
                fav_obj.put("poster_path", cursor.getString(poster_path_index));
                favs_array.put(fav_obj);
            }
            Log.v("favs",favs_array.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static boolean checkFavorite(Context context, String movie_id){

        String selection = FavoritesEntry.COlUMN_MOVIE_ID+" = '"+movie_id+"'";
        Log.v("checkFav",selection);
        Cursor cursor = context.getContentResolver().query(FavoritesEntry.CONTENT_URI,
                null,
                selection,
                null,
                FavoritesEntry.COlUMN_MOVIE_ID
        );

        if(cursor.getCount()>0){
            Log.v("checkFav","Exist");
            return true;
        }else {
            Log.v("checkFav","Do notExist");
            return false;
        }
    }

}
