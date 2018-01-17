package com.example.android.popularmoviesstage2.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Suraz Verma on 12/30/2017.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmoviesstage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns{

        public static final String TABLE_MOVIE = "movies";
        public static final String COLUMN_MOVIE_ID = BaseColumns._ID;
        public static final String COLUMN_TMDB_ID = "TMDB_ID";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_USER_RATING = "user_rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_PLOT_SYNOPSIS = "plot_synopsis";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_DIR_TYPE
                = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"
                +TABLE_MOVIE;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+TABLE_MOVIE;


    }
}
