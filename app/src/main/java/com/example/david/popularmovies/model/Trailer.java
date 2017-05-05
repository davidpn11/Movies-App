package com.example.david.popularmovies.model;

import com.google.auto.value.AutoValue;

/**
 * Created by david on 04/05/17.
 */
@AutoValue
public abstract class Trailer {
    //public abstract String thumbnail();
    public abstract String source();

    public static Trailer create(String source) {
        return new AutoValue_Trailer(source);
    }
}
