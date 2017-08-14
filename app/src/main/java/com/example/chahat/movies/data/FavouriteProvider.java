package com.example.chahat.movies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by chahat on 18/7/17.
 */

public class FavouriteProvider extends ContentProvider {

    private static final int CODE_MOVIE = 100;
    private static final int CODE_VIDEO = 200;
    private static final int CODE_REVIEW = 300;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private FavouriteDbHelper mDbHelper;

    private static UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = FavouriteContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, FavouriteContract.PATH_FAVOURITE_MOVIE,CODE_MOVIE);
        uriMatcher.addURI(authority, FavouriteContract.PATH_FAVOURITE_MOVIE_VIDEO,CODE_VIDEO);
        uriMatcher.addURI(authority,FavouriteContract.PATH_FAVOURITE_MOVIE_REVIEW,CODE_REVIEW);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {

        mDbHelper = new FavouriteDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings1, String s1) {

        Cursor cursor;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)){
            case CODE_MOVIE:
                cursor = db.query(FavouriteContract.FavouriteMovieDetailEntry.MOVIE_TABLE_NAME,strings,s,strings1,null,null,s1);
                break;
            case CODE_VIDEO:
                cursor = db.query(FavouriteContract.FavouriteMovieVideoEntry.VIDEO_TABLE_NAME,strings,s,strings1,null,null,s1);
                break;
            case CODE_REVIEW:
                cursor = db.query(FavouriteContract.FavouriteMovieReviewEntry.MOVIE_TABLE_NAME,strings,s,strings1,null,null,s1);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowInserted = 0;

        switch (sUriMatcher.match(uri)){
            case CODE_MOVIE:
                break;
            case CODE_VIDEO:
                db.beginTransaction();
                try {
                    for (ContentValues contentValues:values){
                        long id = db.insert(FavouriteContract.FavouriteMovieVideoEntry.VIDEO_TABLE_NAME,null,contentValues);
                        Log.d("id",id+"");
                        if (id!=-1){
                            rowInserted++;
                        }
                    }
                }finally {
                    if (rowInserted>0){
                        getContext().getContentResolver().notifyChange(uri,null);
                    }
                    db.endTransaction();
                }
                break;
            case CODE_REVIEW:
                db.beginTransaction();
                try {
                    for (ContentValues contentValues:values){
                        long id = db.insert(FavouriteContract.FavouriteMovieReviewEntry.MOVIE_TABLE_NAME,null,contentValues);
                        if (id!=-1){
                            rowInserted++;
                        }
                    }
                }finally {
                    if (rowInserted>0){
                        getContext().getContentResolver().notifyChange(uri,null);
                    }
                    db.endTransaction();
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }

        return rowInserted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {

        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Uri returnUri=null;

        switch (match){
            case CODE_MOVIE:
                try {

                    long id = db.insert(FavouriteContract.FavouriteMovieDetailEntry.MOVIE_TABLE_NAME,null,contentValues);

                    if ( id > 0 ) {
                        returnUri = ContentUris.withAppendedId(FavouriteContract.FavouriteMovieDetailEntry.CONTENT_URI_MOVIE, id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }

                }catch (SQLiteAbortException e){
                    e.printStackTrace();
                }
                break;
            case CODE_VIDEO:
                try {

                    long id = db.insert(FavouriteContract.FavouriteMovieVideoEntry.VIDEO_TABLE_NAME,null,contentValues);

                    if ( id > 0 ) {
                        returnUri = ContentUris.withAppendedId(FavouriteContract.FavouriteMovieVideoEntry.CONTENT_URI_VIDEO, id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }

                }catch (SQLiteAbortException e){
                    e.printStackTrace();
                }
                break;
            case CODE_REVIEW:
                try {

                    long id = db.insert(FavouriteContract.FavouriteMovieReviewEntry.MOVIE_TABLE_NAME,null,contentValues);

                    if ( id > 0 ) {
                        returnUri = ContentUris.withAppendedId(FavouriteContract.FavouriteMovieReviewEntry.CONTENT_URI_REVIEW, id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }

                }catch (SQLiteAbortException e){
                    e.printStackTrace();
                }
                break;
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {

        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowDeleted;

        if (s==null){
            s="1";
        }

        switch (match){
            case CODE_MOVIE:
                rowDeleted = db.delete(FavouriteContract.FavouriteMovieDetailEntry.MOVIE_TABLE_NAME,s,strings);
                break;
            case CODE_VIDEO:
                rowDeleted = db.delete(FavouriteContract.FavouriteMovieVideoEntry.VIDEO_TABLE_NAME,s,strings);
                break;
            case CODE_REVIEW:
                rowDeleted = db.delete(FavouriteContract.FavouriteMovieReviewEntry.MOVIE_TABLE_NAME,s,strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }

        if (rowDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

}
