package com.example.android.popularmoviesstage2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.Retrofit.ApiClient;
import com.example.android.popularmoviesstage2.Retrofit.ApiInterface;
import com.example.android.popularmoviesstage2.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetails extends AppCompatActivity  {
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "";  //Please Enter API_KEY.
    private static final int TRAILER_LOADER_ID = 122;
    private Movie movie;
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    private final String posterSize = "w342";
    boolean favourite;
    private RecyclerView trailerRecyclerView;
    private ArrayList<VideoData> videoData = new ArrayList<>();
    private VideoResponse videoResponse = new VideoResponse();
    private ArrayList<ReviewData> reviewData = new ArrayList<>();
    private RecyclerView reviewRecyclerView;
    private ReviewResponse reviewResponse = new ReviewResponse();
    private int movieID;
    private String movieTitle ;
    private String posterPath ;
    private String plotSynopsis ;
    private String releaseDate ;
    private double userRating ;




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        TextView mMovieTitle =  findViewById(R.id.current_movie_title);
        ImageView mMoviePoster =  findViewById(R.id.current_movie_poster_image);
        TextView mPlotSynopsis = findViewById(R.id.current_movie_plot_synopsis);
        TextView mReleaseDate = findViewById(R.id.current_movie_release_date);
        TextView mUserRating = findViewById(R.id.current_movie_rating);





        Bundle incomingBundle = getIntent().getExtras();
        if (incomingBundle != null){
            movie = incomingBundle.getParcelable("movie");
        } else
        {
            movie = savedInstanceState.getParcelable("movie");
        }


        movieID = movie.getMovieId();
        movieTitle = movie.getMovieTitle();
        posterPath = movie.getPosterPath();
        plotSynopsis = movie.getPlotSynopsis();
        releaseDate = movie.getReleaseDate();
        userRating = movie.getUserRating();

        initTrailerView();
        initReviewView();
        setTitle(movieTitle);
        Picasso.with(getApplicationContext())
                .load(IMAGE_BASE_URL+posterSize+posterPath)
                .into(mMoviePoster);
        mMovieTitle.setText(movieTitle);
        mPlotSynopsis.setText(plotSynopsis);

        DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Date date = null;
        try {
            date = inputDateFormat.parse(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String correctDateFormat = outputDateFormat.format(date);
        mReleaseDate.setText(correctDateFormat);
        mUserRating.setText(String.valueOf(userRating));

        final String currentMovieID = Integer.toString(movieID);


//        ReviewLoader reviewLoader = new ReviewLoader(this,currentMovieID);
//        reviewLoader.initReviewView();



        final FloatingActionButton favouriteFab = (FloatingActionButton) findViewById(R.id.floating_action_button);
        favourite  = isMovieInDatabase(movieID);
        if(favourite){
            favouriteFab.setImageResource(R.drawable.ic_favorite_white_24px);
        }else{
            favouriteFab.setImageResource(R.drawable.ic_favorite_border_white_24px);
        }
        favouriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isFavourite = isMovieInDatabase(movieID);
                if(isFavourite){
                    getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(currentMovieID).build(),null,null);
                    favouriteFab.setImageResource(R.drawable.ic_favorite_border_white_24px);
                    Toast.makeText(getApplicationContext(),"Movie Removed From Favourites :(" ,Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    saveMovieToDatabase();
                    favouriteFab.setImageResource(R.drawable.ic_favorite_white_24px);
                }
            }

        });
    }

    private void initTrailerView(){
        trailerRecyclerView =  findViewById(R.id.trailer_recycler_view);
        trailerRecyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        trailerRecyclerView.setLayoutManager(layoutManager);

        ApiInterface detailsService = ApiClient.getClient().create(ApiInterface.class);

        Call<VideoResponse> videoResponseCall = detailsService.getVideoData(movieID,API_KEY);
        videoResponseCall.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                videoResponse = response.body();
                videoData = videoResponse.getResults();
                VideoAdapter videoAdapter = new VideoAdapter(MovieDetails.this,videoData);
                trailerRecyclerView.setAdapter(videoAdapter);
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                //TODO: Replace with actual error handling.
                Toast.makeText(MovieDetails.this, "Still error check throwable", Toast.LENGTH_SHORT).show();
            }
        });


//        LoaderManager loaderManager = getSupportLoaderManager();
//        loaderManager.initLoader(TRAILER_LOADER_ID,null,this);
    }

    private void initReviewView(){
        reviewRecyclerView = findViewById(R.id.review_recycler_view);
        reviewRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(layoutManager);

        ApiInterface reviewDetailsService = ApiClient.getClient().create(ApiInterface.class);

        Call<ReviewResponse> reviewResponseCall = reviewDetailsService.getReviewData(movieID,API_KEY);
        reviewResponseCall.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                reviewResponse = response.body();
                reviewData = reviewResponse.getResults();
                ReviewAdapter reviewAdapter = new ReviewAdapter(MovieDetails.this,reviewData);
                reviewRecyclerView.setAdapter(reviewAdapter);
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                //TODO Error Handling
                Toast.makeText(MovieDetails.this, "Review Error", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void saveMovieToDatabase(){
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_TMDB_ID,movie.getMovieId());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,movie.getMovieTitle());
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH,movie.getPosterPath());
        values.put(MovieContract.MovieEntry.COLUMN_USER_RATING,movie.getUserRating());
        values.put(MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSIS,movie.getPlotSynopsis());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,movie.getReleaseDate());
        Uri newUri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,values);
        if(newUri != null){
            Toast.makeText(this,"Movie saved to favourites <3",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isMovieInDatabase(int id){
        String[] projection = {

                MovieContract.MovieEntry.COLUMN_TMDB_ID,
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                MovieContract.MovieEntry.COLUMN_USER_RATING,
                MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSIS,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE};

        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,projection,
                MovieContract.MovieEntry.COLUMN_TMDB_ID + "= '"+id+"'",null,null);
        if (cursor!=null){
            if (cursor.getCount()>0){
                return true;
            }
            cursor.close();
        }
        return false;
    }





//    @Override
//    public Loader<ArrayList<VideoData>> onCreateLoader(int id, Bundle args) {
//        final String currentMovieID = Integer.toString(movie.getMovieId());
//        Uri baseUri = Uri.parse(TMDB_BASE_URL);
//        Uri.Builder uriBuilder = baseUri.buildUpon();
//        uriBuilder.appendPath(currentMovieID);
//        uriBuilder.appendPath("videos");
//        uriBuilder.appendQueryParameter("api_key",API_KEY);
//
//        final String finalURL = uriBuilder.toString();
//        return new AsyncTaskLoader<ArrayList<VideoData>>(this) {
//            @Override
//            protected void onStartLoading() {
//                super.onStartLoading();
//                forceLoad();
//            }
//
//            @Override
//            public ArrayList<VideoData> loadInBackground() {
//                if (finalURL == null){
//                    return null;
//                }
//                return NetworkUtils.fetchTrailerData(finalURL);
//            }
//        };
//
//    }
//
//    @Override
//    public void onLoadFinished(Loader<ArrayList<VideoData>> loader, ArrayList<VideoData> data) {
//
//        if (data!=null && !data.isEmpty()){
//            VideoAdapter videoAdapter = new VideoAdapter(this,data);
//        trailerRecyclerView.setAdapter(videoAdapter);}
//        else {
//            TextView emptyView = findViewById(R.id.no_trailer_text);
//            emptyView.setVisibility(View.VISIBLE);
//            trailerRecyclerView.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void onLoaderReset(Loader<ArrayList<VideoData>> loader) {
//
//    }
//
//    public class ReviewLoader implements LoaderManager.LoaderCallbacks<ArrayList<ReviewData>> {
//
//        private static final int REVIEW_LOADER_ID = 121;
//        private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
//        private static final String API_KEY = "";  //Please Enter API_KEY.
//        private Context LContext;
//        private String  movieID;
//
//        private RecyclerView reviewRecyclerView;
//        private ReviewLoader(Context rContext,String movieID){
//            this.LContext = rContext;
//            this.movieID = movieID;
//        }
//
//
//
//
//        public void initReviewView(){
//            reviewRecyclerView = (RecyclerView) findViewById(R.id.review_recycler_view);
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LContext);
//            reviewRecyclerView.setLayoutManager(layoutManager);
//            LoaderManager loaderManager = getSupportLoaderManager();
//            loaderManager.initLoader(REVIEW_LOADER_ID,null,this);
//        }
//
//

//        @Override
//        public Loader<ArrayList<ReviewData>> onCreateLoader(int id, Bundle args) {
//            Uri baseUri = Uri.parse(TMDB_BASE_URL);
//            Uri.Builder uriBuilder = baseUri.buildUpon();
//            uriBuilder.appendPath(movieID);
//            uriBuilder.appendPath("reviews");
//            uriBuilder.appendQueryParameter("api_key",API_KEY);
//            final String finalURL = uriBuilder.toString();
//
//            return new AsyncTaskLoader<ArrayList<ReviewData>>(LContext) {
//                @Override
//                protected void onStartLoading() {
//                    super.onStartLoading();
//                    forceLoad();
//                }
//
//                @Override
//                public ArrayList<ReviewData> loadInBackground() {
//                    if (finalURL == null){
//                        return null;
//                    }
//                    return NetworkUtils.fetchReviewData(finalURL);
//                }
//            };
//        }
//
//        @Override
//        public void onLoadFinished(Loader<ArrayList<ReviewData>> loader, ArrayList<ReviewData> data) {
//            if (data!=null && !data.isEmpty()){
//                ReviewAdapter reviewAdapter = new ReviewAdapter(LContext,data);
//                reviewRecyclerView.setAdapter(reviewAdapter);
//            }else{
//                TextView emptyView = (TextView)findViewById(R.id.no_reviews_text);
//                emptyView.setVisibility(View.VISIBLE);
//                reviewRecyclerView.setVisibility(View.GONE);
//            }
//        }
//
//        @Override
//        public void onLoaderReset(Loader<ArrayList<ReviewData>> loader) {
//
//        }
//    }
}

