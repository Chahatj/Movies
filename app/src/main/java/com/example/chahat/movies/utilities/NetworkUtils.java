package com.example.chahat.movies.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.chahat.movies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by chahat on 11/7/17.
 */

public class NetworkUtils {

    private static final String MOVIE_API = "https://api.themoviedb.org/3/movie";


    private static final String QUERY_PARAM = "api_key";
    private static final String api_key = BuildConfig.API_KEY;

    private static final String YOUTUBE = "http://www.youtube.com/watch";
    private static final String YOUTUBE_QUERY_PARAM = "v";


    public static URL buildURL(String searchBy){

          Uri uri = Uri.parse(MOVIE_API)
                    .buildUpon()
                    .appendPath(searchBy)
                    .appendQueryParameter(QUERY_PARAM,api_key)
                    .build();


        URL url = null;

        try {
            if (uri != null) {
                url = new URL(uri.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL builtParticularMovieVideoURL(int movieId){

        Uri uri = Uri.parse(MOVIE_API)
                .buildUpon()
                .appendEncodedPath(String.valueOf(movieId)+"/videos")
                .appendQueryParameter(QUERY_PARAM,api_key)
                .build();

        Log.d("MovieURi",uri.toString());

        URL url = null;

        try {
            url = new URL(uri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static URL builtParticularMovieReviewURL(int movieId){
        Uri uri = Uri.parse(MOVIE_API)
                .buildUpon()
                .appendEncodedPath(String.valueOf(movieId)+"/reviews")
                .appendQueryParameter(QUERY_PARAM,api_key)
                .build();

        URL url = null;

        try {
            if (uri!=null){
                url = new URL(uri.toString());
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException{

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasNext = scanner.hasNext();
            if (hasNext) {
                return scanner.next();
            } else {
                return null;
            }
        }finally {
            httpURLConnection.disconnect();
        }

    }

    public static Uri builtYouTubeUri(String id){

        return Uri.parse(YOUTUBE)
                .buildUpon()
                .appendQueryParameter(YOUTUBE_QUERY_PARAM,id)
                .build();

    }

}
