package com.example.hoang_movie.details;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.hoang_movie.databinding.ActivityMovieDetailsBinding;
import com.example.hoang_movie.model.Movie;
import com.example.hoang_movie.utils.Constants;

public class MovieDetailsActivity extends AppCompatActivity {
    private ActivityMovieDetailsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null && extras.containsKey(Constants.MOVIE)) {
                Movie movie = extras.getParcelable(Constants.MOVIE);
                if (movie != null) {
                    MovieDetailsFragment movieDetailsFragment = MovieDetailsFragment.getInstance(movie);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(binding.movieDetailsContainer.getId(), movieDetailsFragment)
                            .commit();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
