package com.example.chahat.movies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by chahat on 18/7/17.
 */

public class FavouriteContract {

    public static final String CONTENT_AUTHORITY = "com.example.chahat.movies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_FAVOURITE_MOVIE = "favourite_movie";
    public static final String PATH_FAVOURITE_MOVIE_VIDEO = "movie_video";
    public static final String PATH_FAVOURITE_MOVIE_REVIEW = "movie_review";

    public static final class FavouriteMovieDetailEntry implements BaseColumns{

        public static final Uri CONTENT_URI_MOVIE = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE_MOVIE).build();

        public static final String MOVIE_TABLE_NAME = "favourite_movie_detail";
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
    }

    public static final class FavouriteMovieVideoEntry implements BaseColumns{

        public static final Uri CONTENT_URI_VIDEO = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE_MOVIE_VIDEO).build();

        public static final String VIDEO_TABLE_NAME = "favourite_movie_video";
        public static final String COLUMN_MOVIE_ID = "video_movie_id";
        public static final String COLUMN_VIDEO_KEY = "video_key";
        public static final String COLUMN_VIDEO_NAME = "video_name";
    }

    public static final class FavouriteMovieReviewEntry implements BaseColumns{

        public static final Uri CONTENT_URI_REVIEW = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE_MOVIE_REVIEW).build();

        public static final String MOVIE_TABLE_NAME = "favourite_movie_review";
        public static final String COLUMN_MOVIE_ID = "review_movie_id";
        public static final String COLUMN_REVIEW_AUTHOR = "author";
        public static final String COLUMN_REVIEW_CONTENT = "content";
    }

}
