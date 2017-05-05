package com.example.david.popularmovies.utils;

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

        import android.net.Uri;

        import android.util.Log;

        import java.io.IOException;
        import java.io.InputStream;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    final static String MOVIESDB_BASE_URL =
            "http://api.themoviedb.org/3";

    final static String API_KEY = "api_key";
    private static final String API_KEY_VALUE = com.example.david.popularmovies.BuildConfig.API_KEY;



    public static URL buildBaseUrl(String sort) {

        String endpoint = null;

        if(sort.equals("top_rated")){
            endpoint = "/movie/top_rated";
        }else if(sort.equals("popular")){
            endpoint = "/movie/popular";
        }



        Uri builtUri = Uri.parse(MOVIESDB_BASE_URL+endpoint).buildUpon()
                .appendQueryParameter(API_KEY, API_KEY_VALUE)
                .build();
        Log.e("URL",builtUri.toString());
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildReviewsUrl(String id) {

        String endpoint = "/movie/"+id+"/reviews";

        Uri builtUri = Uri.parse(MOVIESDB_BASE_URL+endpoint).buildUpon()
                .appendQueryParameter(API_KEY, API_KEY_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static URL buildTrailersUrl(String id) {

        String endpoint = "/movie/"+id+"/trailers";

        Uri builtUri = Uri.parse(MOVIESDB_BASE_URL+endpoint).buildUpon()
                .appendQueryParameter(API_KEY, API_KEY_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}