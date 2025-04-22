package com.example.hoang_movie.listing;

import com.example.hoang_movie.model.Movie;

import java.util.List;

interface MoviesListingView {
    void showMovies(List<Movie> movies);

    void loadingStarted(String page);

    void loadingFailed(String errorMessage);

    void onMovieClicked(Movie movie);
}
