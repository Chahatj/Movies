package com.example.chahat.movies;

import java.io.Serializable;

/**
 * Created by chahat on 18/7/17.
 */
public class MovieObject implements Serializable {

    private int id,rating;
    private String moviePoster,originalTitle,overview,releaseDate;


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
