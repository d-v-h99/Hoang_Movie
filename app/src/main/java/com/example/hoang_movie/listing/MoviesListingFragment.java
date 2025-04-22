package com.example.hoang_movie.listing;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.hoang_movie.BaseApplication;
import com.example.hoang_movie.R;
import com.example.hoang_movie.databinding.FragmentMoviesBinding;
import com.example.hoang_movie.model.Movie;
import com.example.hoang_movie.network.Api;
import com.example.hoang_movie.network.RequestInterceptor;
import com.example.hoang_movie.slimadapter.SlimAdapter;
import com.example.hoang_movie.sorting.SortingDialogFragment;
import com.example.hoang_movie.utils.Constants;
import com.google.android.material.snackbar.Snackbar;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.security.auth.callback.Callback;

public class MoviesListingFragment extends Fragment implements MoviesListingView {
    @Inject
    MoviesListingPresenter moviesListingPresenter;
    //> Dagger cung cấp một instance của MoviesListingPresenterImpl, nhưng biến bạn nhận được lại có kiểu MoviesListingPresenter.
    //
    //Đây là hướng tiếp cận đúng chuẩn SOLID – nguyên lý Liskov và Dependency Inversion:
    //
    //Code nên phụ thuộc vào abstraction (interface), không phải cụ thể (implementation).
    private FragmentMoviesBinding binding;
    private SlimAdapter slimAdapter;
    private List<Movie> movies = new ArrayList<>(20);
    private Callback callback;

    public MoviesListingFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (Callback) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        ((BaseApplication) requireActivity().getApplication()).createListingComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentMoviesBinding.inflate(inflater, container, false);
        initLayoutReferences();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moviesListingPresenter.setView(this);
        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList(Constants.MOVIE);
            slimAdapter.updateData(movies);
            binding.moviesListing.setVisibility(View.VISIBLE);
        } else {
            moviesListingPresenter.firstPage();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            moviesListingPresenter.firstPage();
            displaySortingOptions();
        }
        return super.onOptionsItemSelected(item);
    }

    private void displaySortingOptions() {
        DialogFragment sortingDialogFragment = SortingDialogFragment.newInstance(moviesListingPresenter);
        sortingDialogFragment.show(getParentFragmentManager(), "Select Quantity");
    }

    private void initLayoutReferences() {
        binding.moviesListing.setHasFixedSize(true);
        int columns = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? 2 : getResources().getInteger(R.integer.no_of_columns);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), columns);
        binding.moviesListing.setLayoutManager(layoutManager);
        slimAdapter = SlimAdapter.create()
                .registerDefault(R.layout.movie_grid_item, (data, injector) -> {
                    Movie movie = (Movie) data;
                    injector.text(R.id.movie_name, movie.getTitle())
                            .with(R.id.movie_poster, view -> {
                                ImageView imageView = (ImageView) view;
                                RequestOptions options = new RequestOptions()
                                        .centerCrop()
                                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                        .priority(Priority.HIGH);
                                Glide.with(view.getContext())
                                        .asBitmap()
                                        .load(Api.getPosterPath(movie.getPosterPath()))
                                        .apply(options)
                                        .into(new BitmapImageViewTarget(imageView) {
                                            @Override
                                            public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                                super.onResourceReady(bitmap, transition);
                                                View titleBackground = ((View) view.getRootView().findViewById(R.id.title_background));
                                                if (titleBackground != null) {
                                                    Palette.from(bitmap).generate(palette -> {
                                                        assert palette != null;
                                                        int color = palette.getVibrantColor(
                                                                view.getResources().getColor(R.color.black_translucent_60)
                                                        );
                                                        titleBackground.setBackgroundColor(color);
                                                    });
                                                }
                                            }
                                        });
                            })
                            .clicked(R.id.movie_container, v -> onMovieClicked(movie));
                }).attachTo(binding.moviesListing);
        slimAdapter.updateData(movies);
        binding.moviesListing.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    moviesListingPresenter.nextPage();
                }
            }
        });

    }

    @Override
    public void showMovies(List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        slimAdapter.updateData(this.movies);
        binding.moviesListing.setVisibility(View.VISIBLE);
        callback.onMoviesLoaded(movies.get(0));

    }

    @Override
    public void loadingStarted(String page) {
        String message = getString(R.string.loading_movies) +" "+ page;
        Snackbar.make(binding.moviesListing, message, Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void loadingFailed(String errorMessage) {
        Snackbar.make(binding.moviesListing, errorMessage, Snackbar.LENGTH_INDEFINITE).show();

    }

    @Override
    public void onMovieClicked(Movie movie) {
        callback.onMovieClicked(movie);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        moviesListingPresenter.destroy();
        binding = null;
    }

    @Override
    public void onDetach() {
        callback = null;
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((BaseApplication) requireActivity().getApplication()).releaseListingComponent();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.MOVIE, (ArrayList<? extends Parcelable>) movies);
    }
    public void searchViewClicked(String searchText) {
        moviesListingPresenter.searchMovie(searchText);
    }

    public void searchViewBackButtonClicked() {
        moviesListingPresenter.searchMovieBackPressed();
    }

    public interface Callback {
        void onMoviesLoaded(Movie movie);

        void onMovieClicked(Movie movie);
    }
}
/* Bạn có một @Module chứa @Provides trả về MoviesListingPresenter (đã có).

Bạn có 1 class MoviesListingPresenterImpl implements MoviesListingPresenter.

Trong lúc Dagger build graph, nó thấy MoviesListingPresenter được yêu cầu, nó sẽ dùng method @Provides để tạo.
→ ✅ Dagger cung cấp implementation của interface và bạn gán nó dưới dạng interface.*/

