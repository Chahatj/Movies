package com.example.chahat.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.recyclerview.R.styleable.RecyclerView;

/**
 * Created by chahat on 18/7/17.
 */

public class MovieVideoAdapter extends RecyclerView.Adapter<MovieVideoAdapter.MovieVideoViewHolder> {

    public List<MovieVideoObject> movieVideoList;
    private final OnListItemClick mOnItemClick;

    public MovieVideoAdapter(OnListItemClick itemClick){
        movieVideoList = new ArrayList<>();
        mOnItemClick = itemClick;
    }

    @Override
    public MovieVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movie_video_item,parent,false);
        return new MovieVideoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MovieVideoViewHolder holder, int position) {

        MovieVideoObject videoObject = movieVideoList.get(position);

        holder.textViewMovieTrailerName.setText(videoObject.getName());

    }

    public void setMovieVideoList(List<MovieVideoObject> list){
        this.movieVideoList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (movieVideoList!=null){
            return movieVideoList.size();
        }else {
            return 0;
        }
    }

    public class MovieVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView textViewMovieTrailerName;

        public MovieVideoViewHolder(View itemView){
            super(itemView);

            textViewMovieTrailerName = (TextView) itemView.findViewById(R.id.tv_movie_trailer_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnItemClick.handleVideoClick(movieVideoList.get(getAdapterPosition()));
        }
    }

    interface OnListItemClick{
        void handleVideoClick(MovieVideoObject videoObject);
    }
}
