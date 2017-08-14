package com.example.chahat.movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chahat on 19/7/17.
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder> {

    List<MovieReviewObject> reviewList;

    public MovieReviewAdapter(){
        reviewList = new ArrayList<>();
    }

    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movie_review_item,parent,false);
        return new MovieReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
        MovieReviewObject movieReviewObject = reviewList.get(position);
        holder.textViewAuthor.setText(movieReviewObject.getAuthor());
        holder.textViewContent.setText(movieReviewObject.getContent());
    }

    @Override
    public int getItemCount() {
        if (reviewList!=null){
            return reviewList.size();
        }else {
            return 0;
        }
    }

    public void setReviewList(List<MovieReviewObject> list){
        this.reviewList = list;
        notifyDataSetChanged();
    }

    public class MovieReviewViewHolder extends RecyclerView.ViewHolder{

        private final TextView textViewAuthor,textViewContent;

        public MovieReviewViewHolder(View itemView){
            super(itemView);

            textViewAuthor = (TextView) itemView.findViewById(R.id.textViewAuthor);
            textViewContent = (TextView) itemView.findViewById(R.id.textViewReviewContent);
        }
    }
}
