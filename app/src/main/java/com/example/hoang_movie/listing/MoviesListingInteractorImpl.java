package com.example.hoang_movie.listing;

import com.example.hoang_movie.favorites.FavoritesInteractor;
import com.example.hoang_movie.model.Movie;
import com.example.hoang_movie.network.TmdbWebService;
import com.example.hoang_movie.sorting.SortType;
import com.example.hoang_movie.sorting.SortingOptionStore;
import com.example.hoang_movie.wrapper_respone.MoviesWraper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class MoviesListingInteractorImpl implements MoviesListingInteractor {
    private FavoritesInteractor favoritesInteractor;//Dùng để lấy danh sách phim yêu thích từ local (Realm)
    private TmdbWebService tmdbWebService;//Interface Retrofit để gọi TMDB API
    private SortingOptionStore sortingOptionStore;//Lưu lựa chọn kiểu sắp xếp hiện tại (Popular, Rated, Newest, Favorite
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final int NEWEST_MIN_VOTE_COUNT = 50;

    MoviesListingInteractorImpl(FavoritesInteractor favoritesInteractor,
                                TmdbWebService tmdbWebService, SortingOptionStore store) {
        this.favoritesInteractor = favoritesInteractor;
        this.tmdbWebService = tmdbWebService;
        sortingOptionStore = store;
    }

    @Override
    public boolean isPaginationSupported() {
      int selectedOption = sortingOptionStore.getSelectedOption();
      return selectedOption != SortType.FAVORITES.getValue();
    }

    @Override
    public Observable<List<Movie>> fetchMovies(int page) {
        int selectedOption = sortingOptionStore.getSelectedOption();
        if (selectedOption == SortType.MOST_POPULAR.getValue()) {
            return tmdbWebService.popularMovies(page).map(MoviesWraper::getMovieList);
        } else if (selectedOption == SortType.HIGHEST_RATED.getValue()) {
            return tmdbWebService.highestRatedMovies(page).map(MoviesWraper::getMovieList);
        } else if (selectedOption == SortType.NEWEST.getValue()) {
            Calendar cal = Calendar.getInstance();
            String maxReleaseDate = dateFormat.format(cal.getTime());
            return tmdbWebService.newestMovies(maxReleaseDate, NEWEST_MIN_VOTE_COUNT).map(MoviesWraper::getMovieList);
        } else {
            return Observable.just(favoritesInteractor.getFavorites());
        }

    }

    @Override
    public Observable<List<Movie>> searchMovie(String searchQuery) {
        return tmdbWebService.searchMovies(searchQuery).map(MoviesWraper::getMovieList);
    }
}
