package com.example.hoang_movie.details;



import com.example.hoang_movie.model.Movie;
import com.example.hoang_movie.model.Review;
import com.example.hoang_movie.model.Video;

import java.util.List;

/**
 * @author arun
 */
interface MovieDetailsView {
    void showDetails(Movie movie);

    void showTrailers(List<Video> trailers);

    void showReviews(List<Review> reviews);

    void showFavorited();

    void showUnFavorited();
}
