package com.example.hoang_movie.listing;


import com.example.hoang_movie.model.Movie;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;


public interface MoviesListingInteractor {
    boolean isPaginationSupported(); //kiểm tra xem nguồn dữ liệu hiện tại có hỗ trợ phân trang không
    //Khi nào dùng?
    //
    //Nếu hiển thị danh sách phim "Popular" → thường phân trang được.
    //
    //Nếu hiển thị từ local (Realm, Room, cache…) → có thể không cần phân trang.

    Observable<List<Movie>> fetchMovies(int page);

    Observable<List<Movie>> searchMovie(String searchQuery);
}
/* View (MoviesListingFragment)
      ↓
Presenter (MoviesListingPresenterImpl)
      ↓
Interactor (MoviesListingInteractor)
      ↓
TmdbWebService / Room / Realm / Cache
*/
