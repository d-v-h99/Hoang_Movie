package com.example.hoang_movie.details;

import com.example.hoang_movie.model.Review;
import com.example.hoang_movie.model.Video;
import com.example.hoang_movie.network.TmdbWebService;
import com.example.hoang_movie.wrapper_respone.ReviewsWrapper;
import com.example.hoang_movie.wrapper_respone.VideoWrapper;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class MovieDetailsInteractorImpl implements MovieDetailsInteractor{
    private TmdbWebService tmdbWebService;

    public MovieDetailsInteractorImpl(TmdbWebService tmdbWebService) {
        this.tmdbWebService = tmdbWebService;
    }

    @Override
    public Observable<List<Video>> getTrailers(String id) {
        return tmdbWebService.trailers(id).map(VideoWrapper::getVideos);
    }

    @Override
    public Observable<List<Review>> getReviews(String id) {
        return tmdbWebService.reviews(id).map(ReviewsWrapper::getReviews);
    }
}
