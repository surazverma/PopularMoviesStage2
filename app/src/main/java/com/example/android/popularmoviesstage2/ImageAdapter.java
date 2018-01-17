package com.example.android.popularmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Suraz Verma on 12/30/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{
    private final ArrayList<Movie> movies;
    private final Context context;
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    private final String posterSize = "w500";

    public ImageAdapter(Context context, ArrayList<Movie> movies)
    {
        this.context = context;
        this.movies = movies;
    }


    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        Picasso.with(context)
                .load(IMAGE_BASE_URL+posterSize +movies.get(i).getPosterPath())
                .into(holder.posterImage);
        final int adapterPosition = i;
        holder.posterImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Movie currentMovie = movies.get(adapterPosition);
                Bundle movieInfo = new Bundle();
                movieInfo.putParcelable("movie",currentMovie);
                Intent movieDetailsIntent = new Intent(context, MovieDetails.class);
                movieDetailsIntent.putExtras(movieInfo);
                (context).startActivity(movieDetailsIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final ImageView posterImage;
        public ViewHolder(View view){
            super(view);
            posterImage =  view.findViewById(R.id.movie_poster);
        }
    }
}
