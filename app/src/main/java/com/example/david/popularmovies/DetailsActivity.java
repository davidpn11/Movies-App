package com.example.david.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView text = (TextView) findViewById(R.id.text);

        Intent it = getIntent();
        String data = it.getStringExtra("movie_data");
        text.setText(data);
    }
}
