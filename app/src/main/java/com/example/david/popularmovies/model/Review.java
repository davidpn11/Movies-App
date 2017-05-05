package com.example.david.popularmovies.model;

import com.google.auto.value.AutoValue;

/**
 * Created by david on 04/05/17.
 */
@AutoValue
public abstract class Review{
    public abstract String author();
    public abstract String content();

    public static Review create(String author, String content) {
        return new AutoValue_Review(author, content);
    }
}