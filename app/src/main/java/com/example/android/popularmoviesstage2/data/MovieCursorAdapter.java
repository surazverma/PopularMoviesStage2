package com.example.android.popularmoviesstage2.data;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.preferences.FavouriteDetails;
import com.squareup.picasso.Picasso;

/**
 * Created by Suraz Verma on 1/13/2018.
 */

public class MovieCursorAdapter extends CursorAdapter {
    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE ="w500";

    public MovieCursorAdapter(Context context, Cursor cursor){
        super(context,cursor,0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.movie_list,parent,false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ImageView posterImage =  view.findViewById(R.id.movie_poster);
        int posterPathColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);


        int tmdbIdColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TMDB_ID);
        int movieTitleColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
        int userRatingColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_USER_RATING);
        int plotSynopsisColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSIS);
        int releaseDateColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);

        final String currentTmdbId = cursor.getString(tmdbIdColumnIndex);
        final String currentMovieTitle = cursor.getString(movieTitleColumnIndex);
        final String currentMoviePosterPath = cursor.getString(posterPathColumnIndex);
        final String currentUserRating = cursor.getString(userRatingColumnIndex);
        final String currentReleaseDate = cursor.getString(releaseDateColumnIndex);
        final String currentPlotSynopsis = cursor.getString(plotSynopsisColumnIndex);


        Picasso.with(context).load(IMAGE_URL+POSTER_SIZE+currentMoviePosterPath).into(posterImage);
        posterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailsIntent = new Intent(context, FavouriteDetails.class);
                detailsIntent.putExtra("tmdbId",currentTmdbId);
                detailsIntent.putExtra("poster path",currentMoviePosterPath);
                detailsIntent.putExtra("movie title",currentMovieTitle);
                detailsIntent.putExtra("user rating",currentUserRating);
                detailsIntent.putExtra("release date",currentReleaseDate);
                detailsIntent.putExtra("plot synopsis",currentPlotSynopsis);
                (context).startActivity(detailsIntent);

            }
        });

    }
}
