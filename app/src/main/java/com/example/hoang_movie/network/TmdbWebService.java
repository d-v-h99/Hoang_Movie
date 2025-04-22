package com.example.hoang_movie.network;


import com.example.hoang_movie.wrapper_respone.MoviesWraper;
import com.example.hoang_movie.wrapper_respone.ReviewsWrapper;
import com.example.hoang_movie.wrapper_respone.VideoWrapper;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbWebService {
    @GET("3/discover/movie?language=en&sort_by=popularity.desc")
    Observable<MoviesWraper> popularMovies(@Query("page") int page);
    @GET("3/discover/movie?vote_count.gte=500&language=en&sort_by=vote_average.desc")
    Observable<MoviesWraper> highestRatedMovies(@Query("page") int page);
    @GET("3/discover/movie?language=en&sort_by=release_date.desc")
    Observable<MoviesWraper> newestMovies(@Query("release_date.lte") String maxReleaseDate, @Query("vote_count.gte") int minVoteCount);
    @GET("3/movie/{movieId}/videos")
    Observable<VideoWrapper> trailers(@Path("movieId") String movieId);
    @GET("3/movie/{movieId}/reviews")
    Observable<ReviewsWrapper> reviews(@Path("movieId") String movieId);
    @GET("3/search/movie?language=en-US&page=1")
    Observable<MoviesWraper> searchMovies(@Query("query") String searchQuery);

}
