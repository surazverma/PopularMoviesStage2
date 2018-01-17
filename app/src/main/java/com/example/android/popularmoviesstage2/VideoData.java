package com.example.android.popularmoviesstage2;

/**
 * Created by Suraz Verma on 1/14/2018.
 */

public class VideoData {
    private String mVideoKey;
    private String mVideoType;
    private String mVideoName;


    public VideoData(String videoKey,String videoName,String videoType){
        this.mVideoKey = videoKey;
        this.mVideoName = videoName;
        this.mVideoType = videoType;
    }



    public String getVideoKey(){return mVideoKey;}
    public String getVideoName(){return mVideoName;}
    public String getVideoType(){return mVideoType;}
}
