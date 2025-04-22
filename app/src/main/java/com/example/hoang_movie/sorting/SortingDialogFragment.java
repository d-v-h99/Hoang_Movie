package com.example.hoang_movie.sorting;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.hoang_movie.BaseApplication;
import com.example.hoang_movie.R;
import com.example.hoang_movie.databinding.SortingOptionsBinding;
import com.example.hoang_movie.listing.MoviesListingPresenter;

import javax.inject.Inject;

public class SortingDialogFragment extends DialogFragment implements SortingDialogView, RadioGroup.OnCheckedChangeListener {
    @Inject
    SortingDialogPresenter sortingDialogPresenter;
    private static MoviesListingPresenter moviesListingPresenter;
    private SortingOptionsBinding binding;

    public static SortingDialogFragment newInstance(MoviesListingPresenter moviesListingPresenter) {
        SortingDialogFragment.moviesListingPresenter = moviesListingPresenter;
        return new SortingDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((BaseApplication) getActivity().getApplication()).getListingComponent().inject(this);
        sortingDialogPresenter.setView(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = SortingOptionsBinding.inflate(inflater);
        initViews();
        Dialog dialog = new Dialog(requireActivity());
        dialog.setContentView(binding.getRoot());
        dialog.setTitle(R.string.sort_by);
        dialog.show();
        return dialog;


    }

    private void initViews() {
        sortingDialogPresenter.setLastSavedOption();
        binding.sortingGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.most_popular) {
            sortingDialogPresenter.onPopularMoviesSelected();
            moviesListingPresenter.firstPage();
        } else if (checkedId == R.id.highest_rated) {
            sortingDialogPresenter.onHighestRatedMoviesSelected();
            moviesListingPresenter.firstPage();
        } else if (checkedId == R.id.favorites) {
            sortingDialogPresenter.onFavoritesSelected();
            moviesListingPresenter.firstPage();
        } else {
            sortingDialogPresenter.onNewestMoviesSelected();
            moviesListingPresenter.firstPage();
        }
    }

    @Override
    public void setPopularChecked() {
        binding.mostPopular.setChecked(true);

    }

    @Override
    public void setNewestChecked() {
        binding.newest.setChecked(true);

    }

    @Override
    public void setHighestRatedChecked() {
        binding.highestRated.setChecked(true);
    }

    @Override
    public void setFavoritesChecked() {
        binding.favorites.setChecked(true);
    }

    @Override
    public void dismissDialog() {
        dismiss();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sortingDialogPresenter.destroy();
        binding = null;
    }
}
