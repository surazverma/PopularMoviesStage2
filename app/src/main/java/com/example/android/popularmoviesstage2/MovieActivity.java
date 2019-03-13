package com.example.android.popularmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.Retrofit.ApiClient;
import com.example.android.popularmoviesstage2.Retrofit.ApiInterface;
import com.example.android.popularmoviesstage2.preferences.FavouriteActivity;
import com.example.android.popularmoviesstage2.preferences.SettingsActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private ProgressBar mLoadingIndicator;
    private static final int MOVIE_LOADER_ID = 1;
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = ""; //Please Enter API_KEY.
    private RecyclerView moviesRecyclerView;
    private ImageAdapter imageAdapter;
    private TextView mOfflineTextView;
    private String sortOrder;
    private ArrayList<Movie> moviesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        setTitle("Popular Movies");

        mLoadingIndicator = findViewById(R.id.loading_circle);
        mOfflineTextView =  findViewById(R.id.internet_connection_error_text);
        mOfflineTextView.setVisibility(View.GONE);
        setupSharedPreferences();

        ConnectivityManager check = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = check.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnectedOrConnecting()){
            mLoadingIndicator.setVisibility(View.VISIBLE);
            initViews();
        } else{
            mOfflineTextView.setVisibility(View.VISIBLE);
        }
    }

    private void initViews(){

        moviesRecyclerView = (RecyclerView) findViewById(R.id.poster_recycler_view);
        moviesRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager = new GridLayoutManager(getApplicationContext(),4);
            moviesRecyclerView.setLayoutManager(layoutManager);
        }else{
            moviesRecyclerView.setLayoutManager(layoutManager);}

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> callPopular = apiService.getPopularMovies(API_KEY);
        Call<MovieResponse> callTopRated = apiService.getTopRatedMovies(API_KEY);
        if(sortOrder.equals(getString(R.string.pref_by_popular_value))){
            callPopular.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    mLoadingIndicator.setVisibility(View.GONE);
                    moviesList = response.body().getResults();
                    imageAdapter = new ImageAdapter(MovieActivity.this,moviesList);
                    moviesRecyclerView.setAdapter(imageAdapter);
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Toast.makeText(MovieActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            callTopRated.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    mLoadingIndicator.setVisibility(View.GONE);
                    moviesList = response.body().getResults();
                    imageAdapter = new ImageAdapter(MovieActivity.this,moviesList);
                    moviesRecyclerView.setAdapter(imageAdapter);
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Toast.makeText(MovieActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                }
            });
        }

    }
    private void setupSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadMoviePreferences(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void loadMoviePreferences(SharedPreferences sharedPreferences){
        sortOrder = sharedPreferences.getString(getString(R.string.pref_sortOrder_key)
                ,getString(R.string.pref_by_popular_value));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sorting){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_favourite)
        {   Intent intent = new Intent(this,FavouriteActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_sortOrder_key))){
            loadMoviePreferences(sharedPreferences);
            initViews();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}
