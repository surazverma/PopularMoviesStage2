package com.example.android.popularmoviesstage2;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Suraz Verma on 1/16/2018.
 */

public class ReviewData  {
    @SerializedName("author")
    private String mReviewAuthor;

    @SerializedName("content")
    private String mReviewContent;

    public ReviewData(String author,String content){
        this.mReviewAuthor = author;
        this.mReviewContent = content;
    }
    public String getAuthor(){return mReviewAuthor;}
    public String getReviewContent(){return mReviewContent;}
}
