package com.example.chahat.movies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chahat.movies.data.FavouriteContract;
import com.example.chahat.movies.utilities.JsonUtils;
import com.example.chahat.movies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements MovieVideoAdapter.OnListItemClick,View.OnClickListener {

    private TextView tv_title,tv_release_date,tv_rating,tv_overview;
    private ImageView iv_movie,imageViewFavourite;

    private MovieVideoAdapter movieVideoAdapter;
    private MovieReviewAdapter movieReviewAdapter;


    private final int LOADER_VIDEO_ID = 0;
    private final int LOADER_REVIEW_ID = 1;
    private final int PREF_LOADER_VIDEO_ID = 2;
    private final int PREF_LOADER_REVIEW_ID = 3;
    private final int CHECK_FAV_LOADER = 4;

    private TextView textViewNoTrailer,textViewNoReview;
    private ProgressBar progressBarTrailer,progressBarReview;
    private MovieObject movieObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tv_title = (TextView) findViewById(R.id.tv_movie_title);
        tv_release_date = (TextView) findViewById(R.id.tv_release_date);
        tv_rating = (TextView) findViewById(R.id.tv_rating);
        tv_overview = (TextView) findViewById(R.id.tv_overview);
        iv_movie = (ImageView) findViewById(R.id.iv_movie);
        textViewNoTrailer = (TextView) findViewById(R.id.textViewNoTrailer);
        textViewNoReview = (TextView) findViewById(R.id.textViewNoReview);
        progressBarReview = (ProgressBar) findViewById(R.id.progress_review_indicator);
        progressBarTrailer = (ProgressBar) findViewById(R.id.progress_video_indicator);
        imageViewFavourite = (ImageView) findViewById(R.id.imageViewFavourite);
        imageViewFavourite.setOnClickListener(this);

        RecyclerView movieVideoRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewMovieVideo);

        movieVideoAdapter = new MovieVideoAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        movieVideoRecyclerView.setLayoutManager(layoutManager);
        movieVideoRecyclerView.setAdapter(movieVideoAdapter);

        RecyclerView movieReviewRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewMovieReview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        movieReviewRecyclerView.setLayoutManager(linearLayoutManager);

        movieReviewAdapter = new MovieReviewAdapter();
        movieReviewRecyclerView.setAdapter(movieReviewAdapter);

        Intent intent = getIntent();

        if (intent.hasExtra("MovieObject")){
            movieObject = (MovieObject) getIntent().getSerializableExtra("MovieObject");
            displayData(movieObject);
            initLoader();
        }

        getSupportLoaderManager().initLoader(CHECK_FAV_LOADER,null,checkFavLoader);
    }

    private void initLoader(){

        String sortType = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_sort_key),getString(R.string.pref_option_sort_value_popular));

        if (sortType.equals(getString(R.string.pref_option_sort_value_favourite))){
            Log.d("inFavourite","inFavourite");
            getSupportLoaderManager().initLoader(PREF_LOADER_VIDEO_ID,null,favouriteVideoLoader);
            getSupportLoaderManager().initLoader(PREF_LOADER_REVIEW_ID,null,favouriteReviewLoader);
        }else {
            getSupportLoaderManager().initLoader(LOADER_VIDEO_ID,null,videoLoader);
            getSupportLoaderManager().initLoader(LOADER_REVIEW_ID,null,reviewLoader);
        }
    }

    private final LoaderManager.LoaderCallbacks<List<MovieVideoObject>> videoLoader = new LoaderManager.LoaderCallbacks<List<MovieVideoObject>>() {
            @Override
            public Loader<List<MovieVideoObject>> onCreateLoader(int id, final Bundle args) {

                return new AsyncTaskLoader<List<MovieVideoObject>>(getApplicationContext()) {

                    List<MovieVideoObject> mVideoList=null;

                    @Override
                    protected void onStartLoading() {
                        if (mVideoList!=null){
                            deliverResult(mVideoList);
                        }else {
                            forceLoad();
                            progressBarTrailer.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public List<MovieVideoObject> loadInBackground() {

                        try{
                            URL url = NetworkUtils.builtParticularMovieVideoURL(movieObject.getId());
                            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                            return JsonUtils.getMovieVideoJsonUtil(jsonResponse);
                        }catch (Exception e){
                            e.printStackTrace();
                            return null;
                        }

                    }

                    @Override
                    public void deliverResult(List<MovieVideoObject> data) {
                        mVideoList = data;
                        super.deliverResult(data);
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<List<MovieVideoObject>> loader, List<MovieVideoObject> data) {
                if (data!=null && data.size()!=0){
                    movieVideoAdapter.setMovieVideoList(data);
                    showTrailerData();
                }else {
                    noTrailerMessage();
                }

            }

            @Override
            public void onLoaderReset(Loader<List<MovieVideoObject>> loader) {

            }
        };


    private void noTrailerMessage(){
        progressBarTrailer.setVisibility(View.INVISIBLE);
        textViewNoTrailer.setVisibility(View.VISIBLE);
    }

    private void showTrailerData(){
        progressBarTrailer.setVisibility(View.INVISIBLE);
        textViewNoTrailer.setVisibility(View.INVISIBLE);
    }

    private final LoaderManager.LoaderCallbacks<List<MovieReviewObject>> reviewLoader = new LoaderManager.LoaderCallbacks<List<MovieReviewObject>>() {
            @Override
            public Loader<List<MovieReviewObject>> onCreateLoader(int id, final Bundle args) {

                return new AsyncTaskLoader<List<MovieReviewObject>>(getApplicationContext()) {

                    List<MovieReviewObject> mReviewList = null;

                    @Override
                    protected void onStartLoading() {
                        if (mReviewList!=null){
                            deliverResult(mReviewList);
                        }else {
                            progressBarReview.setVisibility(View.VISIBLE);
                            forceLoad();
                        }
                    }

                    @Override
                    public List<MovieReviewObject> loadInBackground() {

                        try {

                            URL url = NetworkUtils.builtParticularMovieReviewURL(movieObject.getId());
                            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                            return JsonUtils.getMovieReviewJsonUtil(jsonResponse);

                        }catch (Exception e){
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    public void deliverResult(List<MovieReviewObject> data) {
                        mReviewList = data;
                        super.deliverResult(data);
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<List<MovieReviewObject>> loader, List<MovieReviewObject> data) {
                if (data!=null && data.size()!=0){
                    movieReviewAdapter.setReviewList(data);
                    showReviewData();
                }else {
                    noReviewMessage();
                }

            }

            @Override
            public void onLoaderReset(Loader<List<MovieReviewObject>> loader) {

            }
        };


    private void noReviewMessage(){
        progressBarReview.setVisibility(View.INVISIBLE);
        textViewNoReview.setVisibility(View.VISIBLE);
    }

    private void showReviewData(){
        progressBarReview.setVisibility(View.INVISIBLE);
        textViewNoReview.setVisibility(View.INVISIBLE);
    }

    private void displayData(MovieObject movieObject){
        Picasso.with(DetailActivity.this).load(MovieAdapter.IMAGE_URL+movieObject.getMoviePoster()).fit().into(iv_movie);
        tv_title.setText(movieObject.getOriginalTitle());
        tv_release_date.setText(movieObject.getReleaseDate());
        tv_rating.setText(String.valueOf(movieObject.getRating()));
        tv_overview.setText(movieObject.getOverview());
    }

    @Override
    public void handleVideoClick(MovieVideoObject videoObject) {

        Uri uri = NetworkUtils.builtYouTubeUri(videoObject.getKey());

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        String sortType = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_sort_key),getString(R.string.pref_option_sort_value_popular));

        if (sortType.equals(getString(R.string.pref_option_sort_value_favourite))){
            getSupportLoaderManager().restartLoader(PREF_LOADER_VIDEO_ID,null,favouriteVideoLoader);
            getSupportLoaderManager().restartLoader(PREF_LOADER_REVIEW_ID,null,favouriteReviewLoader);
        }else {
            getSupportLoaderManager().restartLoader(LOADER_VIDEO_ID,null,videoLoader);
            getSupportLoaderManager().restartLoader(LOADER_REVIEW_ID,null,reviewLoader);
        }

        getSupportLoaderManager().restartLoader(CHECK_FAV_LOADER,null,checkFavLoader);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }

        return true;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id==R.id.imageViewFavourite){
            if (view.isSelected()){
                getContentResolver().delete(FavouriteContract.FavouriteMovieDetailEntry.CONTENT_URI_MOVIE, FavouriteContract.FavouriteMovieDetailEntry.COLUMN_MOVIE_ID + "=" + movieObject.getId(),null);
                getContentResolver().delete(FavouriteContract.FavouriteMovieVideoEntry.CONTENT_URI_VIDEO,FavouriteContract.FavouriteMovieVideoEntry.COLUMN_MOVIE_ID + "=" + movieObject.getId(),null);
                getContentResolver().delete(FavouriteContract.FavouriteMovieReviewEntry.CONTENT_URI_REVIEW, FavouriteContract.FavouriteMovieReviewEntry.COLUMN_MOVIE_ID + "=" + movieObject.getId(),null);
                view.setSelected(false);
            }else {
                insertMovieDetail();
                insertVideoDetail();
                insertReviewDetail();
                view.setSelected(true);
            }
        }
    }

    private void insertMovieDetail(){

        ContentValues contentValues = new ContentValues();
        contentValues.put(FavouriteContract.FavouriteMovieDetailEntry.COLUMN_MOVIE_ID,movieObject.getId());
        contentValues.put(FavouriteContract.FavouriteMovieDetailEntry.COLUMN_OVERVIEW,movieObject.getOverview());
        contentValues.put(FavouriteContract.FavouriteMovieDetailEntry.COLUMN_POSTER,movieObject.getMoviePoster());
        contentValues.put(FavouriteContract.FavouriteMovieDetailEntry.COLUMN_RATING,movieObject.getRating());
        contentValues.put(FavouriteContract.FavouriteMovieDetailEntry.COLUMN_RELEASE_DATE,movieObject.getReleaseDate());
        contentValues.put(FavouriteContract.FavouriteMovieDetailEntry.COLUMN_TITLE,movieObject.getOriginalTitle());

        getContentResolver().insert(FavouriteContract.FavouriteMovieDetailEntry.CONTENT_URI_MOVIE,contentValues);

    }

    private void insertVideoDetail(){

        int count = movieVideoAdapter.getItemCount();
        if (count>0){
            List<MovieVideoObject> list = movieVideoAdapter.movieVideoList;

            for (int i=0;i<count;i++){

                MovieVideoObject object = list.get(i);

                int id = movieObject.getId();
                String key = object.getKey();
                String name = object.getName();

                ContentValues contentValues1 = new ContentValues();
                contentValues1.put(FavouriteContract.FavouriteMovieVideoEntry.COLUMN_MOVIE_ID,id);
                contentValues1.put(FavouriteContract.FavouriteMovieVideoEntry.COLUMN_VIDEO_KEY,key);
                contentValues1.put(FavouriteContract.FavouriteMovieVideoEntry.COLUMN_VIDEO_NAME,name);

                getContentResolver().insert(FavouriteContract.FavouriteMovieVideoEntry.CONTENT_URI_VIDEO,contentValues1);
            }
        }

    }

    private void insertReviewDetail(){

        int count = movieReviewAdapter.getItemCount();
        if (count>0){
            List<MovieReviewObject> list = movieReviewAdapter.reviewList;

            for (int i=0;i<count;i++){

                MovieReviewObject object = list.get(i);

                int id = movieObject.getId();
                String author = object.getAuthor();
                String content = object.getContent();

                ContentValues contentValues1 = new ContentValues();
                contentValues1.put(FavouriteContract.FavouriteMovieReviewEntry.COLUMN_MOVIE_ID,id);
                contentValues1.put(FavouriteContract.FavouriteMovieReviewEntry.COLUMN_REVIEW_AUTHOR,author);
                contentValues1.put(FavouriteContract.FavouriteMovieReviewEntry.COLUMN_REVIEW_CONTENT,content);


                getContentResolver().insert(FavouriteContract.FavouriteMovieReviewEntry.CONTENT_URI_REVIEW,contentValues1);
            }


        }

    }

    private final LoaderManager.LoaderCallbacks<Cursor> favouriteVideoLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            return new AsyncTaskLoader<Cursor>(getApplicationContext()) {

                Cursor mData = null;

                @Override
                protected void onStartLoading() {
                    if (mData!=null){
                        deliverResult(mData);
                    }else {
                        forceLoad();
                    }
                }

                @Override
                public Cursor loadInBackground() {

                    Log.d("id",movieObject.getId()+"");

                    return  getContext().getContentResolver().query(FavouriteContract.FavouriteMovieVideoEntry.CONTENT_URI_VIDEO,null,
                            FavouriteContract.FavouriteMovieVideoEntry.COLUMN_MOVIE_ID + "=" + movieObject.getId(),null,null,null);

                }

                @Override
                public void deliverResult(Cursor data) {
                    mData = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data.getCount()!=0){

                List<MovieVideoObject> list = new ArrayList<>(data.getCount());

                while (data.moveToNext()){

                    String key = data.getString(data.getColumnIndex(FavouriteContract.FavouriteMovieVideoEntry.COLUMN_VIDEO_KEY));
                    String name = data.getString(data.getColumnIndex(FavouriteContract.FavouriteMovieVideoEntry.COLUMN_VIDEO_NAME));

                    MovieVideoObject movieObject = new MovieVideoObject();
                    movieObject.setKey(key);
                    movieObject.setName(name);

                    list.add(movieObject);

                }

                if (list.size()!=0){
                    movieVideoAdapter.setMovieVideoList(list);
                }
            data.close();
            }else {
                noTrailerMessage();
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private final LoaderManager.LoaderCallbacks<Cursor> favouriteReviewLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            return new AsyncTaskLoader<Cursor>(getApplicationContext()) {

                Cursor mData = null;

                @Override
                protected void onStartLoading() {
                    if (mData!=null){
                        deliverResult(mData);
                    }else {
                        forceLoad();
                    }
                }

                @Override
                public Cursor loadInBackground() {

                    return  getContext().getContentResolver().query(FavouriteContract.FavouriteMovieReviewEntry.CONTENT_URI_REVIEW,null,
                            FavouriteContract.FavouriteMovieReviewEntry.COLUMN_MOVIE_ID + "=" + movieObject.getId(),null,null,null);

                }

                @Override
                public void deliverResult(Cursor data) {
                    mData = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data!=null && data.getCount()!=0){

                List<MovieReviewObject> list = new ArrayList<>(data.getCount());

                while (data.moveToNext()){

                    String author = data.getString(data.getColumnIndex(FavouriteContract.FavouriteMovieReviewEntry.COLUMN_REVIEW_AUTHOR));
                    String content = data.getString(data.getColumnIndex(FavouriteContract.FavouriteMovieReviewEntry.COLUMN_REVIEW_CONTENT));

                    MovieReviewObject movieObject = new MovieReviewObject();
                    movieObject.setAuthor(author);
                    movieObject.setContent(content);

                    list.add(movieObject);

                }

                data.close();
                if (list.size()!=0){
                    movieReviewAdapter.setReviewList(list);
                }
            }else {
                noReviewMessage();
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private final LoaderManager.LoaderCallbacks<Cursor> checkFavLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<Cursor>(getApplicationContext()) {

                Cursor mCursor;

                @Override
                protected void onStartLoading() {

                    if (mCursor!=null){
                        deliverResult(mCursor);
                    }else {
                        forceLoad();
                    }
                }

                @Override
                public Cursor loadInBackground() {

                   return getContentResolver().query(FavouriteContract.FavouriteMovieDetailEntry.CONTENT_URI_MOVIE,
                            null, FavouriteContract.FavouriteMovieDetailEntry.COLUMN_MOVIE_ID+"="+movieObject.getId(),null,null,null);
                }

                @Override
                public void deliverResult(Cursor data) {
                    mCursor = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data.getCount()!=0){
                imageViewFavourite.setSelected(true);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

}
