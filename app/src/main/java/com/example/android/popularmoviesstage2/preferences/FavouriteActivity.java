package com.example.android.popularmoviesstage2.preferences;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.data.MovieContract;
import com.example.android.popularmoviesstage2.data.MovieCursorAdapter;

public class FavouriteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private GridView databaseGridView;
    private MovieCursorAdapter movieCursorAdapter;
    private static final int MOVIE_CURSOR_LOADER_ID = 110;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        setTitle("Favourites");
        databaseGridView = (GridView) findViewById(R.id.favourite_movie_grid_view);
        movieCursorAdapter = new MovieCursorAdapter(this,null);
        if (this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE)
        {
            databaseGridView.setNumColumns(4);
        }


        getSupportLoaderManager().initLoader(MOVIE_CURSOR_LOADER_ID,null,this);




    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] projection = {
                MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                MovieContract.MovieEntry.COLUMN_TMDB_ID,
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                MovieContract.MovieEntry.COLUMN_USER_RATING,
                MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSIS,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
        };
        return new CursorLoader(this,
                MovieContract.MovieEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {




        movieCursorAdapter.swapCursor(data);
        databaseGridView.setAdapter(movieCursorAdapter);





    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieCursorAdapter.swapCursor(null);
    }
}
