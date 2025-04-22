package com.example.hoang_movie.listing;

import com.example.hoang_movie.favorites.FavoritesInteractor;
import com.example.hoang_movie.network.TmdbWebService;
import com.example.hoang_movie.sorting.SortingOptionStore;

import dagger.Module;
import dagger.Provides;

@Module
public class ListingModule {
    @Provides
    MoviesListingInteractor provideMovieListingInteractor(FavoritesInteractor favoritesInteractor,
                                                          TmdbWebService tmdbWebService,
                                                          SortingOptionStore sortingOptionStore){
        return new MoviesListingInteractorImpl(favoritesInteractor, tmdbWebService, sortingOptionStore);
    }
    @Provides
    MoviesListingPresenter provideMovieListingPresenter(MoviesListingInteractor interactor) {
        return new MoviesListingPresenterImpl(interactor);
        //Bạn đang nói với Dagger:
        //
        //"Khi có ai đó cần MoviesListingPresenter, hãy tạo ra MoviesListingPresenterImpl và trả về dưới dạng interface."
    }
}
