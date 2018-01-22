package com.studios.flipacoin.whichmovie.utils;

import android.util.Log;

import com.studios.flipacoin.whichmovie.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vicente_Lee on 1/19/18.
 */

public class ProcessJsonResponse {
    public static ArrayList<Movie> getListOfMovies(String response, ArrayList<Movie> currentMovies) throws JSONException {
        ArrayList<Movie> movies;

        JSONObject jsonResponse = new JSONObject(response);
        JSONArray resultsArray = jsonResponse.getJSONArray("results");
        if (currentMovies == null) {
            movies = new ArrayList<>();
        } else {
            movies = currentMovies;
        }

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject movieJsonObject = resultsArray.getJSONObject(i);
            String title = movieJsonObject.getString("title");
            String releaseDate = movieJsonObject.getString("release_date");
            String posterPath = movieJsonObject.getString("poster_path");
            String voteAverage = movieJsonObject.getString("vote_average");
            String overview = movieJsonObject.getString("overview");
            movies.add(new Movie(title, releaseDate, posterPath, voteAverage, overview));
        }

        return movies;
    }

    public static int getTotalPages(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        String totalPages = jsonResponse.getString("total_pages");
        return Integer.valueOf(totalPages);
    }
}
