package com.example.chahat.movies.utilities;

import com.example.chahat.movies.MovieObject;
import com.example.chahat.movies.MovieReviewObject;
import com.example.chahat.movies.MovieVideoObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chahat on 18/7/17.
 */

public class JsonUtils {

    public static List<MovieObject> getMovieJsonUtils(String jsonResponse){

        try {
            JSONObject movieJson = new JSONObject(jsonResponse);

            JSONArray jsonArrayResult = movieJson.getJSONArray("results");

            List<MovieObject> list = new ArrayList<>();

            for (int i=0;i<jsonArrayResult.length();i++){

                JSONObject jsonObject = jsonArrayResult.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String poster = jsonObject.getString("poster_path");
                String title = jsonObject.getString("original_title");
                String overview = jsonObject.getString("overview");
                int rating = jsonObject.getInt("vote_average");
                String date = jsonObject.getString("release_date");

                MovieObject movieObject = new MovieObject();
                movieObject.setId(id);
                movieObject.setMoviePoster(poster);
                movieObject.setOriginalTitle(title);
                movieObject.setOverview(overview);
                movieObject.setRating(rating);
                movieObject.setReleaseDate(date);

                list.add(movieObject);
            }

            return list;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<MovieVideoObject> getMovieVideoJsonUtil(String jsonResponse){

        try {
            JSONObject movieObject = new JSONObject(jsonResponse);
            JSONArray videoArray = movieObject.getJSONArray("results");

            List<MovieVideoObject> list = new ArrayList<>();

            for (int i=0;i<videoArray.length();i++){

                JSONObject videoJsonObject = videoArray.getJSONObject(i);

                String id = videoJsonObject.getString("id");
                String key = videoJsonObject.getString("key");
                String name = videoJsonObject.getString("name");
                String site = videoJsonObject.getString("site");
                String type = videoJsonObject.getString("type");

                MovieVideoObject videoObject = new MovieVideoObject();
                videoObject.setId(id);
                videoObject.setKey(key);
                videoObject.setName(name);
                videoObject.setSite(site);
                videoObject.setType(type);

                list.add(videoObject);
            }

            return list;

        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<MovieReviewObject> getMovieReviewJsonUtil(String jsonResponse){
        try {

            List<MovieReviewObject> reviewList = new ArrayList<>();

            JSONObject reviewObject = new JSONObject(jsonResponse);
            JSONArray reviewArray = reviewObject.getJSONArray("results");

            for (int i=0;i<reviewArray.length();i++){

                JSONObject jsonObject = reviewArray.getJSONObject(i);

                String author = jsonObject.getString("author");
                String content = jsonObject.getString("content");

                MovieReviewObject movieReviewObject = new MovieReviewObject();
                movieReviewObject.setAuthor(author);
                movieReviewObject.setContent(content);

                reviewList.add(movieReviewObject);
            }

            return reviewList;

        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
}
