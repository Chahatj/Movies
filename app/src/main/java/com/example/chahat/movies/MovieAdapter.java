package com.example.chahat.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chahat on 11/7/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private List<MovieObject> movieList;
    private final Context context;
    private final OnListItemClick mOnItemClick;

    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/w185/";

    public MovieAdapter(Context cont,OnListItemClick itemClick){
        context = cont;
        mOnItemClick = itemClick;
        movieList = new ArrayList<>();
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item,parent,false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Picasso.with(context).load(IMAGE_URL+movieList.get(position).getMoviePoster()).fit().into(holder.iv_movie);
        Log.d("MovieAdapter",movieList.get(position).getMoviePoster());
    }

    @Override
    public int getItemCount() {
        if (movieList!=null){
            return movieList.size();
        }else {
            return 0;
        }
    }

    public void setMovieData(List<MovieObject> movieData){
        this.movieList = movieData;
        notifyDataSetChanged();
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView iv_movie;

        public MovieAdapterViewHolder(View itemView){
            super(itemView);

            iv_movie = (ImageView) itemView.findViewById(R.id.iv_movie);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnItemClick.handleClick(movieList.get(getAdapterPosition()));
        }
    }

    interface OnListItemClick{
        void handleClick(MovieObject movieObject);
    }
}
