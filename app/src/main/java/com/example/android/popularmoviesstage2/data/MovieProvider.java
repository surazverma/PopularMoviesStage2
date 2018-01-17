package com.example.android.popularmoviesstage2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Suraz Verma on 12/30/2017.
 */

public class MovieProvider extends ContentProvider {
    private static final String LOG_TAG = MovieProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenDBHelper;

    private static final int MOVIES = 100;
    private static final int MOVIE_WITH_ID = 101;


    private static  UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,MovieContract.MovieEntry.TABLE_MOVIE, MOVIES);
        matcher.addURI(authority,MovieContract.MovieEntry.TABLE_MOVIE + "/#" , MOVIE_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenDBHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mOpenDBHelper.getReadableDatabase();

        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case MOVIES:
                cursor = database.query(MovieContract.MovieEntry.TABLE_MOVIE,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
                break;
            case MOVIE_WITH_ID:
                selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID +"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MovieContract.MovieEntry.TABLE_MOVIE,
                                projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase database;
        database = mOpenDBHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri))
        {
            case MOVIES:


                long id = database.insert(MovieContract.MovieEntry.TABLE_MOVIE,null,values);
                if (id>0){
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI,id);
                }else
                {
                    throw new android.database.SQLException("Failed to insert data for this uri"+uri);
                }
                break;
               default:
                   throw new UnsupportedOperationException("Unknown Uri:"+uri);


        }

        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mOpenDBHelper.getWritableDatabase();
        int rowsDeleted;


        switch (sUriMatcher.match(uri)){
            case MOVIES:

                rowsDeleted = database.delete(MovieContract.MovieEntry.TABLE_MOVIE,selection,selectionArgs);
                break;
            case MOVIE_WITH_ID:
                selection= MovieContract.MovieEntry.COLUMN_TMDB_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(MovieContract.MovieEntry.TABLE_MOVIE,selection,selectionArgs);
            break;
            default:
                throw new IllegalArgumentException("Cannot perform Delete for this URI" + uri);

        }
        if (rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mOpenDBHelper.getWritableDatabase();
        int rowsUpdated ;
        if (values == null){
            throw new IllegalArgumentException("Values cannot be null");
        }
        switch (sUriMatcher.match(uri)){
            case MOVIES: {
                rowsUpdated = database.update(MovieContract.MovieEntry.TABLE_MOVIE, values, selection, selectionArgs);
                break;
            }
            case MOVIE_WITH_ID:
            {
                selection = MovieContract.MovieEntry.COLUMN_TMDB_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = database.update(MovieContract.MovieEntry.TABLE_MOVIE,values,selection,selectionArgs);
                break;
            }
            default:
            {throw new IllegalArgumentException("Cannot update the database unfortunately");}
        }
        if (rowsUpdated>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdated;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch(sUriMatcher.match(uri)){
            case MOVIES:
            {return MovieContract.MovieEntry.CONTENT_DIR_TYPE;}

            case MOVIE_WITH_ID:
            {return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;}

            default:
            { throw new UnsupportedOperationException("Wrong URI type"+uri);}
        }
    }
}
