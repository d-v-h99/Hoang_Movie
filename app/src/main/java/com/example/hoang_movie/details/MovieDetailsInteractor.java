package com.example.hoang_movie.details;

import com.example.hoang_movie.model.Review;
import com.example.hoang_movie.model.Video;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

public interface MovieDetailsInteractor {
    Observable<List<Video>> getTrailers(String id);
    Observable<List<Review>> getReviews(String id);
}
