package com.example.android.popularmoviesstage2.preferences;

import android.content.ContentValues;
import android.database.Cursor;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;

public class FavouriteDetails extends AppCompatActivity {
    private String currentMovieId;
    private String currentMoviePosterPath;
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    private final String posterSize = "w500";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_details);

        Bundle dataBundle = getIntent().getExtras();
        final String savedTmdbID = dataBundle.getString("tmdbId");
        final String savedPosterPath = dataBundle.getString("poster path");
        final String savedMovieTitle = dataBundle.getString("movie title");
        final String savedUserRating = dataBundle.getString("user rating");
        final String savedReleaseDate = dataBundle.getString("release date");
        final String savedPlotSynopsis = dataBundle.getString("plot synopsis");
        setTitle(savedMovieTitle);
        currentMovieId = savedTmdbID;
        currentMoviePosterPath =savedPosterPath;
        DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Date date = null;
        try {
            date = inputDateFormat.parse(savedReleaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String correctDateFormat = outputDateFormat.format(date);

        inflateImageView();

        TextView movieTitle = (TextView) findViewById(R.id.saved_movie_title);
        movieTitle.setText(savedMovieTitle);
        TextView userRating = (TextView) findViewById(R.id.saved_user_rating);
        userRating.setText(savedUserRating);
        TextView releaseDate = (TextView) findViewById(R.id.saved_release_date);
        releaseDate.setText(correctDateFormat);
        TextView plotSynopsis = (TextView) findViewById(R.id.saved_movie_plot_synopsis);
        plotSynopsis.setText(savedPlotSynopsis);

        final FloatingActionButton favFab = (FloatingActionButton) findViewById(R.id.saved_floating_button);
        boolean favourite = isMovieInDatabase(currentMovieId);
        if (favourite){
        favFab.setImageResource(R.drawable.ic_favorite_white_24px);}
        else{
            favFab.setImageResource(R.drawable.ic_favorite_border_white_24px);
        }

        favFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isFavourite = isMovieInDatabase(currentMovieId);
                if (isFavourite){
                    getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(currentMovieId).build(),null,null);
                    favFab.setImageResource(R.drawable.ic_favorite_border_white_24px);
                    Toast.makeText(getApplicationContext(),"Movie is removed from favorite :(",Toast.LENGTH_SHORT).show();

                }else {
                    saveMovieToDatabase(savedTmdbID,savedMovieTitle,savedPosterPath,savedUserRating,savedReleaseDate,savedPlotSynopsis);
                    favFab.setImageResource(R.drawable.ic_favorite_white_24px);

                }

            }
        });



    }
    private void inflateImageView(){
        ImageView posterDisplay = (ImageView) findViewById(R.id.saved_poster_view);
        Picasso.with(this).load(IMAGE_BASE_URL+posterSize+currentMoviePosterPath).into(posterDisplay);
    }

    private void saveMovieToDatabase(String movieId,String movieTitle,String posterPath,String userRating,String releaseDate,String plotSynopsis){
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_TMDB_ID,movieId);
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,movieTitle);
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH,posterPath);
        values.put(MovieContract.MovieEntry.COLUMN_USER_RATING,userRating);
        values.put(MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSIS,plotSynopsis);
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,releaseDate);
        Uri newUri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,values);
        if(newUri != null){
            Toast.makeText(this,"Movie saved to favourites <3 ",Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isMovieInDatabase(String id){
        String[] projection = {

                MovieContract.MovieEntry.COLUMN_TMDB_ID,
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                MovieContract.MovieEntry.COLUMN_USER_RATING,
                MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSIS,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE};

        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,projection,
                MovieContract.MovieEntry.COLUMN_TMDB_ID + "= '"+id+"'",null,null);
        if (cursor!=null){
            if (cursor.getCount()>0){
                return true;
            }
            cursor.close();
        }
        return false;
    }
}

