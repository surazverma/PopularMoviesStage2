package com.example.android.popularmoviesstage2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Suraz Verma on 12/30/2017.
 */

public class Movie implements Parcelable {
    private String mMovieTitle;
    private String mPosterPath;
    private String mPlotSynopsis;
    private double mUserRating;
    private String mReleaseDate;
    private int mMovieId;




    public Movie(String movieTitle,String posterPath, String plotSynopsis, double userRating,String releaseDate,int id){
        this.mMovieTitle = movieTitle;
        this.mPosterPath = posterPath;
        this.mPlotSynopsis = plotSynopsis;
        this.mUserRating = userRating;
        this.mReleaseDate = releaseDate;
        this.mMovieId = id;
    }

    protected Movie(Parcel in) {
        mMovieId = in.readInt();
        mMovieTitle = in.readString();
        mPosterPath = in.readString();
        mPlotSynopsis = in.readString();
        mUserRating = in.readDouble();
        mReleaseDate = in.readString();


    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    public int getMovieId(){
        return mMovieId;
    }
    public String getMovieTitle (){
        return mMovieTitle;
    }
    public String getPosterPath(){
        return mPosterPath;
    }
    public String getPlotSynopsis(){
        return mPlotSynopsis;
    }
    public double getUserRating(){
        return mUserRating;
    }
    public String getReleaseDate(){
        return mReleaseDate;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMovieId);
        dest.writeString(mMovieTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mPlotSynopsis);
        dest.writeDouble(mUserRating);
        dest.writeString(mReleaseDate);

    }
}
