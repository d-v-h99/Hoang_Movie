package com.example.hoang_movie.favorites;

import com.example.hoang_movie.model.Movie;

import java.util.Collections;
import java.util.List;

public class FavoritesInteractorImpl implements FavoritesInteractor {
    private FavoritesStore favoritesStore;

    public FavoritesInteractorImpl(FavoritesStore favoritesStore) {
        this.favoritesStore = favoritesStore;
    }

    @Override
    public void setFavorite(Movie movie) {
        favoritesStore.setFavorite(movie);
    }

    @Override
    public boolean isFavorite(String id) {
        return favoritesStore.isFavorite(id);
    }

    @Override
    public List<Movie> getFavorites() {
        return favoritesStore.getFavorites();
    }

    @Override
    public void unFavorite(String id) {
        favoritesStore.unfavorite(id);
    }
}
