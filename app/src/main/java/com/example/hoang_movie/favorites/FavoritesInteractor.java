package com.example.hoang_movie.favorites;



import com.example.hoang_movie.model.Movie;

import java.util.List;

/**
 * @author arun
 */
public interface FavoritesInteractor {
    void setFavorite(Movie movie);

    boolean isFavorite(String id);

    List<Movie> getFavorites();

    void unFavorite(String id);
}
