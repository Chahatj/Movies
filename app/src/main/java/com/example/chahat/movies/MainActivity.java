package com.example.chahat.movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chahat.movies.data.FavouriteContract;
import com.example.chahat.movies.utilities.JsonUtils;
import com.example.chahat.movies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnListItemClick,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView rv_movies;
    private TextView tv_error_message,textViewNoCollection;
    private ProgressBar pb_loading_indicator;
    private MovieAdapter movieAdapter;

    private static final int LOADER_ID = 1;
    private static final int FAVORITE_MOVIE_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv_movies = (RecyclerView) findViewById(R.id.rv_movies);
        tv_error_message = (TextView) findViewById(R.id.tv_error_message);
        textViewNoCollection = (TextView) findViewById(R.id.textViewNoCollection);
        pb_loading_indicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,calculateNoOfColumns(this));
        rv_movies.setLayoutManager(gridLayoutManager);

        movieAdapter = new MovieAdapter(this,this);
        rv_movies.setAdapter(movieAdapter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortType = sharedPreferences.getString(getString(R.string.pref_sort_key),getString(R.string.pref_option_sort_value_popular));

        if (sortType.equals(getString(R.string.pref_option_sort_value_favourite))){
            getSupportLoaderManager().initLoader(FAVORITE_MOVIE_LOADER_ID,null,favouriteMovieLoader);
        }else {
            getSupportLoaderManager().initLoader(LOADER_ID,null,moviesLoader);
        }
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }

    private final LoaderManager.LoaderCallbacks<List<MovieObject>> moviesLoader = new LoaderManager.LoaderCallbacks<List<MovieObject>>() {
        @Override
        public Loader<List<MovieObject>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<List<MovieObject>>(getApplicationContext()) {

                List<MovieObject> list = null;

                @Override
                protected void onStartLoading() {
                    if (list!=null){
                        deliverResult(list);
                    }else {
                        pb_loading_indicator.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }

                @Override
                public List<MovieObject> loadInBackground() {
                    try {

                        String sortType = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.pref_sort_key),getString(R.string.pref_option_sort_value_popular));

                        URL url = NetworkUtils.buildURL(sortType);

                        String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);

                        return JsonUtils.getMovieJsonUtils(jsonResponse);

                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void deliverResult(List<MovieObject> data) {
                    list = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<MovieObject>> loader, List<MovieObject> data) {
            if (data==null){
                showErrorMessage();
            }else {
                showMovieData();
            }
            movieAdapter.setMovieData(data);
        }

        @Override
        public void onLoaderReset(Loader<List<MovieObject>> loader) {

        }
    };

    private void showErrorMessage(){
        tv_error_message.setVisibility(View.VISIBLE);
        textViewNoCollection.setVisibility(View.INVISIBLE);
        rv_movies.setVisibility(View.INVISIBLE);
        pb_loading_indicator.setVisibility(View.INVISIBLE);
    }

    private void showMovieData(){
        rv_movies.setVisibility(View.VISIBLE);
        textViewNoCollection.setVisibility(View.INVISIBLE);
        tv_error_message.setVisibility(View.INVISIBLE);
        pb_loading_indicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void handleClick(MovieObject movieObject) {

        Intent intent = new Intent(MainActivity.this,DetailActivity.class);
        intent.putExtra("MovieObject",movieObject);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==R.id.action_setting){
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }

        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    @Override
    protected void onStart() {
        super.onStart();

            String sortType = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_sort_key),getString(R.string.pref_option_sort_value_popular));
            if (!(sortType.equals(getString(R.string.pref_option_sort_value_favourite)))){
                getSupportLoaderManager().restartLoader(LOADER_ID,null,moviesLoader);
            }else {
                getSupportLoaderManager().restartLoader(FAVORITE_MOVIE_LOADER_ID,null,favouriteMovieLoader);
            }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    private final LoaderManager.LoaderCallbacks<Cursor> favouriteMovieLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
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

                    return  getContext().getContentResolver().query(FavouriteContract.FavouriteMovieDetailEntry.CONTENT_URI_MOVIE,null,
                            null,null,null);

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

                List<MovieObject> list = new ArrayList<>(data.getCount());

                while (data.moveToNext()){

                    int id = data.getInt(data.getColumnIndex(FavouriteContract.FavouriteMovieDetailEntry.COLUMN_MOVIE_ID));
                    String poster = data.getString(data.getColumnIndex(FavouriteContract.FavouriteMovieDetailEntry.COLUMN_POSTER));
                    String overview = data.getString(data.getColumnIndex(FavouriteContract.FavouriteMovieDetailEntry.COLUMN_OVERVIEW));
                    int rating = data.getInt(data.getColumnIndex(FavouriteContract.FavouriteMovieDetailEntry.COLUMN_RATING));
                    String title = data.getString(data.getColumnIndex(FavouriteContract.FavouriteMovieDetailEntry.COLUMN_TITLE));
                    String releaseDate = data.getString(data.getColumnIndex(FavouriteContract.FavouriteMovieDetailEntry.COLUMN_RELEASE_DATE));

                    MovieObject movieObject = new MovieObject();
                    movieObject.setId(id);
                    movieObject.setReleaseDate(releaseDate);
                    movieObject.setRating(rating);
                    movieObject.setOriginalTitle(title);
                    movieObject.setMoviePoster(poster);
                    movieObject.setOverview(overview);

                    list.add(movieObject);

                }

                data.close();
                if (list.size() != 0){
                    showMovieData();
                    movieAdapter.setMovieData(list);
                }

            }else {
                showNoCollectionMessage();
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private void showNoCollectionMessage(){
        textViewNoCollection.setVisibility(View.VISIBLE);
        rv_movies.setVisibility(View.INVISIBLE);
        tv_error_message.setVisibility(View.INVISIBLE);
        pb_loading_indicator.setVisibility(View.INVISIBLE);
    }
}
