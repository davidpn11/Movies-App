package com.example.david.popularmovies.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

/**
 * Created by david on 04/05/17.
 */
@AutoValue
public abstract class Movie implements Parcelable {
    public abstract String id();
    public abstract String title();
    public abstract String releaseDate();
    public abstract String voteAverage();
    public abstract String posterPath();
    public abstract String overview();

    public static Movie create(String id, String title, String releaseDate, String voteAverage,
                                String posterPath, String overview) {
        return new AutoValue_Movie(id,title,releaseDate,voteAverage,posterPath,overview);
    }
}
