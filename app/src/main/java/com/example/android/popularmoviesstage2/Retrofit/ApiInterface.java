package com.example.android.popularmoviesstage2.Retrofit;

import com.example.android.popularmoviesstage2.Movie;
import com.example.android.popularmoviesstage2.MovieResponse;
import com.example.android.popularmoviesstage2.ReviewResponse;
import com.example.android.popularmoviesstage2.VideoData;
import com.example.android.popularmoviesstage2.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<VideoResponse> getVideoData(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getReviewData(@Path("id") int id, @Query("api_key") String apiKey);

}
