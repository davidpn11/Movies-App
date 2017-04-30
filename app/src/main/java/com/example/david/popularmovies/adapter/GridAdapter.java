package com.example.david.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.popularmovies.R;
import com.example.david.popularmovies.ui.DetailsActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by david on 25/04/17.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private JSONArray movies_array;
    private Context mContext;

    public GridAdapter(Context context,JSONArray movies_array) {
        this.movies_array = movies_array;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_cardview, null);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            final JSONObject movie_data = movies_array.getJSONObject(position);
            holder.bindMovieData(movie_data,mContext);


        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        return movies_array.length();

    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView poster_img;
        private CardView card_view;
        private TextView movie_title;
        private JSONObject movie_data;
        final private String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185//";

        public ViewHolder(View itemView) {
            super(itemView);
            poster_img = (ImageView) itemView.findViewById(R.id.poster_image);
            card_view = (CardView) itemView.findViewById(R.id.cv);
            movie_title= (TextView) itemView.findViewById(R.id.movie_title);
            card_view.setOnClickListener(this);
        }

        public void bindMovieData(JSONObject data,Context mContext){
            try{
                movie_data = data;
                String poster_path = BASE_POSTER_PATH+movie_data.getString("poster_path");
                Picasso.with(mContext).load(poster_path).resize(dp2px(220), 0).into(poster_img);
                movie_title.setText(movie_data.getString("original_title"));
            }catch (JSONException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public int dp2px(int dp) {
            WindowManager wm = (WindowManager) mContext
                    .getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics displaymetrics = new DisplayMetrics();
            display.getMetrics(displaymetrics);
            return (int) (dp * displaymetrics.density + 0.5f);
        }

        @Override
        public void onClick(View view) {

            Context context = view.getContext();

            if(movie_data != null){

                Intent it = new Intent(view.getContext(),DetailsActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                it.putExtra("movie_data",movie_data.toString());
                context.startActivity(it);
            }
        }
    }
}