package com.example.david.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity {

    private TextView rating, release_date, sinopsis;
    private ImageView poster_image;
    final private String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185//";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        rating = (TextView) findViewById(R.id.movie_rating);
        release_date = (TextView) findViewById(R.id.release_date);
        sinopsis = (TextView) findViewById(R.id.movie_sinopsis);
        poster_image = (ImageView) findViewById(R.id.post_image);


        Intent it = getIntent();
        String movie_data = it.getStringExtra("movie_data");
        //text.setText(data);
        try{
            JSONObject movie_obj = new JSONObject(movie_data);
            String rating_text = movie_obj.getString("vote_average")+"/10";
            Calendar c = Calendar.getInstance();

            String date = getDate(movie_obj.getString("release_date"));
            setTitle(movie_obj.getString("original_title"));
            rating.setText(rating_text);
            release_date.setText(date);
            sinopsis.setText(movie_obj.getString("overview"));
            setPoster(poster_image, movie_obj.getString("poster_path"));


        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void setPoster(ImageView poster_image, String poster_path){
        Picasso.with(this).load(BASE_POSTER_PATH+poster_path).into(poster_image);
    }

    private String getDate(String date){

        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8,date.length());

        return  month+"/"+day+"/"+year;



    }
}
