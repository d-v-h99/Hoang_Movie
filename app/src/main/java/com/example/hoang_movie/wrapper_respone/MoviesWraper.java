package com.example.hoang_movie.wrapper_respone;

import com.example.hoang_movie.model.Movie;
import com.squareup.moshi.Json;

import java.util.List;

public class MoviesWraper {

    @Json(name = "results") // khi JSON có key "results", thì gán nó vào trường movies.
    private List<Movie> movies;//ánh xạ mỗi object trong results thành một object Movie.

    public List<Movie> getMovieList() {
        return movies;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movies = movieList;
    }
}
//ớp "bọc" (wrapper) để ánh xạ dữ liệu JSON từ response