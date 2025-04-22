package com.example.hoang_movie.details;

import com.example.hoang_movie.favorites.FavoritesInteractor;
import com.example.hoang_movie.model.Movie;
import com.example.hoang_movie.model.Review;
import com.example.hoang_movie.model.Video;
import com.example.hoang_movie.utils.RxUtils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailsPresenterImpl implements MovieDetailsPresenter{
    private MovieDetailsView view;
    private MovieDetailsInteractor movieDetailsInteractor;
    private FavoritesInteractor favoritesInteractor;
    private Disposable trailersSubscription;
    private Disposable reviewSubscription;

    public MovieDetailsPresenterImpl(MovieDetailsInteractor movieDetailsInteractor, FavoritesInteractor favoritesInteractor) {
        this.movieDetailsInteractor = movieDetailsInteractor;
        this.favoritesInteractor = favoritesInteractor;
    }

    @Override
    public void showDetails(Movie movie) {
        if(isViewAttached()){
            view.showDetails(movie);
        }
    }

    @Override
    public void showTrailers(Movie movie) {
        trailersSubscription = movieDetailsInteractor.getTrailers(movie.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetTrailersSuccess, t->onGetTrailersFailure());
    }
    private void onGetTrailersSuccess(List<Video> videos) {
        if (isViewAttached()) {
            view.showTrailers(videos);
        }
    }
    private void onGetTrailersFailure() {
        // Do nothing
    }

    @Override
    public void showReviews(Movie movie) {
        reviewSubscription = movieDetailsInteractor.getReviews(movie.getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetReviewsSuccess, t->onGetTrailersFailure());
    }
    private void onGetReviewsSuccess(List<Review> reviews) {
        if (isViewAttached()) {
            view.showReviews(reviews);
        }
    }

    @Override
    public void showFavoriteButton(Movie movie) {
        boolean isFavorite = favoritesInteractor.isFavorite(movie.getId());
        if (isViewAttached()) {
            if (isFavorite) {
                view.showFavorited();
            } else {
                view.showUnFavorited();
            }
        }

    }

    @Override
    public void onFavoriteClick(Movie movie) {
        if (isViewAttached()) {
            boolean isFavorite = favoritesInteractor.isFavorite(movie.getId());
            if (isFavorite) {
                favoritesInteractor.unFavorite(movie.getId());
                view.showUnFavorited();
            } else {
                favoritesInteractor.setFavorite(movie);
                view.showFavorited();
            }
        }

    }

    @Override
    public void setView(MovieDetailsView view) {
        this.view = view;
    }
    private boolean isViewAttached() {
        return view != null;
    }

    @Override
    public void destroy() {
        view = null;
        RxUtils.unsubscribe(trailersSubscription, reviewSubscription);

    }
}
