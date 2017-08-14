package com.example.chahat.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chahat on 18/7/17.
 */

public class FavouriteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    public FavouriteDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_MOVIE_TABLE =
                "CREATE TABLE " + FavouriteContract.FavouriteMovieDetailEntry.MOVIE_TABLE_NAME + " ( " +

                        FavouriteContract.FavouriteMovieDetailEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavouriteContract.FavouriteMovieDetailEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                        FavouriteContract.FavouriteMovieDetailEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                        FavouriteContract.FavouriteMovieDetailEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                        FavouriteContract.FavouriteMovieDetailEntry.COLUMN_RATING +  " INTEGER, " +
                        FavouriteContract.FavouriteMovieDetailEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                        FavouriteContract.FavouriteMovieDetailEntry.COLUMN_TITLE + " TEXT NOT NULL " +
                        " ); ";

        final String CREATE_VIDEO_TABLE =
                "CREATE TABLE " + FavouriteContract.FavouriteMovieVideoEntry.VIDEO_TABLE_NAME + " ( " +

                        FavouriteContract.FavouriteMovieVideoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavouriteContract.FavouriteMovieVideoEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                        FavouriteContract.FavouriteMovieVideoEntry.COLUMN_VIDEO_KEY + " TEXT NOT NULL, " +
                        FavouriteContract.FavouriteMovieVideoEntry.COLUMN_VIDEO_NAME + " TEXT NOT NULL " +
                        " ); ";

        final String CREATE_REVIEW_TABLE =
                "CREATE TABLE " + FavouriteContract.FavouriteMovieReviewEntry.MOVIE_TABLE_NAME + " ( " +

                        FavouriteContract.FavouriteMovieReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavouriteContract.FavouriteMovieReviewEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                        FavouriteContract.FavouriteMovieReviewEntry.COLUMN_REVIEW_AUTHOR + " TEXT NOT NULL, " +
                        FavouriteContract.FavouriteMovieReviewEntry.COLUMN_REVIEW_CONTENT + " TEXT NOT NULL " +
                        " ); ";


        sqLiteDatabase.execSQL(CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(CREATE_VIDEO_TABLE);
        sqLiteDatabase.execSQL(CREATE_REVIEW_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
