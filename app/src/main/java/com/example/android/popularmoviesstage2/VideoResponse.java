package com.example.android.popularmoviesstage2;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VideoResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("results")
    ArrayList<VideoData> results = null;

    public void setId(int id) {
        this.id = id;
    }

    public void setResults(ArrayList<VideoData> results) {
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public ArrayList<VideoData> getResults() {
        return results;
    }
}
