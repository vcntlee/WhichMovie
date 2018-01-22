package com.studios.flipacoin.whichmovie.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.studios.flipacoin.whichmovie.MovieAdapter;
import com.studios.flipacoin.whichmovie.R;
import com.studios.flipacoin.whichmovie.data.Movie;
import com.studios.flipacoin.whichmovie.utils.NetworkUtils;
import com.studios.flipacoin.whichmovie.utils.ProcessJsonResponse;

import java.net.URL;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnItemClickListener{

    private ArrayList<Movie> mMovies;
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private TextView mErrorMessage;
    private ProgressBar mProgressBar;

    // begin: fields for URL
    public static final String BASE_URL =  "https://api.themoviedb.org/3/movie";
    private static int paginationNumber = 1;
    private static int totalPages;
    private static String groupBy = "popular";
    public static final String API_KEY = "";
    // end: fields for URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mRecyclerView = findViewById(R.id.rv_movies);
        mErrorMessage = findViewById(R.id.tv_error_message);
        mProgressBar = findViewById(R.id.pb_loading);

        GridLayoutManager layoutManager = new GridLayoutManager(HomeActivity.this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        mMovieAdapter = new MovieAdapter(mMovies, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                final int DOWN_DIRECTION = 1;
                if (!recyclerView.canScrollVertically(DOWN_DIRECTION)) {
                    if (paginationNumber < totalPages) {
                        paginationNumber++;
                        populateMovieGrid();
                    }
                }
            }
        });

        populateMovieGrid();
    }

    private void populateMovieGrid() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        new FetchMoviesTask().execute(NetworkUtils.buildUrlString(groupBy, paginationNumber));
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.most_popular:
                groupBy = "popular";
                paginationNumber = 1;
                mMovies = new ArrayList<>();
                populateMovieGrid();
                break;
            case R.id.top_rated:
                groupBy = "top_rated";
                paginationNumber = 1;
                mMovies = new ArrayList<>();
                populateMovieGrid();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class detailClass = DetailActivity.class;
        Intent intent = new Intent(context, detailClass);
        String title = movie.getTitle();
        String releaseDate = movie.getReleaseDate();
        String posterPath = movie.getPosterPath();
        String voteAverage = movie.getVoteAverage();
        String overview = movie.getOverview();

        intent.putExtra("title", title);
        intent.putExtra("releaseDate", releaseDate);
        intent.putExtra("posterPath", posterPath);
        intent.putExtra("voteAverage", voteAverage);
        intent.putExtra("overview", overview);

        startActivity(intent);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isNetworkAvailable()) {
                Log.i("network", "network not available");
                showErrorMessage();
            }
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            try {
                url = new URL(strings[0]);
                String response = NetworkUtils.getResponse(url);
                totalPages = ProcessJsonResponse.getTotalPages(response);
                mMovies = ProcessJsonResponse.getListOfMovies(response, mMovies);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (mMovies != null) {
                mMovieAdapter.setMovieThumbnails(mMovies);
            }
            else {
                showErrorMessage();
                Log.e(HomeActivity.class.getSimpleName(), "mPosterPaths is null or mMovieAdapter is null");
            }
        }
    }

}
