package com.example.android.popularmoviesstage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Suraz Verma on 12/30/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = MovieDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME  = "movies.db";
    private static final int DATABASE_VERSION = 1;
    public MovieDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE "+ MovieContract.MovieEntry.TABLE_MOVIE
                + " (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY,"
                +MovieContract.MovieEntry.COLUMN_TMDB_ID + " INTEGER , "
                +MovieContract.MovieEntry.COLUMN_MOVIE_TITLE +" TEXT, "
                +MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT, "
                +MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT, "
                +MovieContract.MovieEntry.COLUMN_USER_RATING + " TEXT, "
                +MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSIS + " TEXT); ";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG,"Upgrading Database from version " + oldVersion + " to "+newVersion+". OLD DATA WILL BE DESTROYED");

        db.execSQL("DROP TABLE IF EXISTS" +MovieContract.MovieEntry.TABLE_MOVIE);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = ' "+
                MovieContract.MovieEntry.TABLE_MOVIE+"'");
        onCreate(db);
    }
}
