package com.studios.flipacoin.whichmovie.utils;

import android.net.Uri;
import android.util.Log;

import com.studios.flipacoin.whichmovie.ui.HomeActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Vicente_Lee on 1/19/18.
 */

public class NetworkUtils {

    public static String buildUrlString(String groupBy, int paginationNumber) {


        Uri builder = Uri.parse(HomeActivity.BASE_URL).buildUpon()
                .appendPath(groupBy)
                .appendQueryParameter("api_key", HomeActivity.API_KEY)
                .appendQueryParameter("language", "en-US")
                .appendQueryParameter("page", String.valueOf(paginationNumber))
                .build();

        Log.i("URLString", builder.toString());
        return builder.toString();
    }

    public static String getResponse(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        StringBuilder out = new StringBuilder();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            Log.i("network", out.toString());
            reader.close();
        } finally {
            urlConnection.disconnect();
        }
        return out.toString();
    }

}
