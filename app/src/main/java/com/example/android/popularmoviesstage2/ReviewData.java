package com.example.android.popularmoviesstage2;

/**
 * Created by Suraz Verma on 1/16/2018.
 */

public class ReviewData  {
    private String mReviewAuthor;
    private String mReviewContent;

    public ReviewData(String author,String content){
        this.mReviewAuthor = author;
        this.mReviewContent = content;
    }
    public String getAuthor(){return mReviewAuthor;}
    public String getReviewContent(){return mReviewContent;}
}
