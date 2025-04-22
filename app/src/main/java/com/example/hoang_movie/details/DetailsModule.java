package com.example.hoang_movie.details;

import com.example.hoang_movie.favorites.FavoritesInteractor;
import com.example.hoang_movie.network.TmdbWebService;

import dagger.Module;
import dagger.Provides;

@Module
public class DetailsModule {
    @Provides
    @DetailsScope
    MovieDetailsInteractor provideInteractor(TmdbWebService tmdbWebService){
        return new MovieDetailsInteractorImpl(tmdbWebService);
    }
    @Provides
    @DetailsScope
    MovieDetailsPresenter providePresenter(MovieDetailsInteractor detailsInteractor,
                                           FavoritesInteractor favoritesInteractor){
        return new MovieDetailsPresenterImpl(detailsInteractor, favoritesInteractor);
    }
}
