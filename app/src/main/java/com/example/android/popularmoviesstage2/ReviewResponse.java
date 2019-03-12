package com.example.android.popularmoviesstage2;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class ReviewResponse {

    @SerializedName("id")
    private Integer id;

    @SerializedName("page")
    private Integer page;

    @SerializedName("results")
    private ArrayList<ReviewData> results = null;

    @SerializedName("total_pages")
    private Integer totalPages;

    @SerializedName("total_results")
    private Integer totalResults;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPage(Integer page) {
        this.page = page;
    }



    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPage() {
        return page;
    }

    public void setResults(ArrayList<ReviewData> results) {
        this.results = results;
    }

    public ArrayList<ReviewData> getResults() {
        return results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }
}
