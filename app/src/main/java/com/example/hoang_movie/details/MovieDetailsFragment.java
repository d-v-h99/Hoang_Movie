package com.example.hoang_movie.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hoang_movie.BaseApplication;
import com.example.hoang_movie.R;
import com.example.hoang_movie.databinding.FragmentMovieDetailsBinding;
import com.example.hoang_movie.model.Movie;
import com.example.hoang_movie.model.Review;
import com.example.hoang_movie.model.Video;
import com.example.hoang_movie.network.Api;
import com.example.hoang_movie.slimadapter.SlimAdapter;
import com.example.hoang_movie.utils.Constants;

import java.util.List;

import javax.inject.Inject;

public class MovieDetailsFragment extends Fragment implements MovieDetailsView, View.OnClickListener {
    @Inject
    MovieDetailsPresenter movieDetailsPresenter;
    private FragmentMovieDetailsBinding binding;
    private SlimAdapter trailerAdapter;
    private SlimAdapter reviewAdapter;

    private Movie movie;

    public static MovieDetailsFragment getInstance(@NonNull Movie movie) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.MOVIE, movie);
        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        movieDetailsFragment.setArguments(args);
        return movieDetailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((BaseApplication) getActivity().getApplication()).createDetailsComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false);
        setToolbar();
        return binding.getRoot();
    }

    private void setToolbar() {
        binding.collapsingToolbar.setContentScrimColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
        binding.collapsingToolbar.setTitle(getString(R.string.movie_details));
        binding.collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedToolbar);
        binding.collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedToolbar);

        if (binding.toolbar != null) {
            ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
            ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            movie = getArguments().getParcelable(Constants.MOVIE);
            if (movie != null) {
                movieDetailsPresenter.setView(this);
                movieDetailsPresenter.showDetails(movie);
                movieDetailsPresenter.showFavoriteButton(movie);
            }
        }
        binding.favorite.setOnClickListener(this);
        setupAdapters();
    }

    private void setupAdapters() {
        trailerAdapter = SlimAdapter.create()
                .registerDefault(R.layout.video, (data, injector) -> {
                    Video trailer = (Video) data;
                    ImageView thumb = (ImageView) injector.findViewById(R.id.video_thumb);
                    thumb.setTag(R.id.glide_tag, Video.getUrl(trailer));
                    thumb.setOnClickListener(this);

                    Glide.with(requireContext())
                            .load(Video.getThumbnailUrl(trailer))
                            .apply(new RequestOptions().placeholder(R.color.colorPrimary).centerCrop().override(150, 150))
                            .into(thumb);
                })
                .attachTo(binding.trailersAndReviews.trailers);

        reviewAdapter = SlimAdapter.create()
                .registerDefault(R.layout.review, (data, injector) -> {
                    Review review = (Review) data;
                    injector.text(R.id.review_author, review.getAuthor());
                    TextView content = (TextView) injector.findViewById(R.id.review_content);
                    content.setText(review.getContent());
                    content.setOnClickListener(this);
                })
                .attachTo(binding.trailersAndReviews.reviews);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.video_thumb){
            onThumbnailClick(view);
        }else if(view.getId() == R.id.review_content){
            onReviewClick((TextView) view);
        }else if(view.getId() == R.id.favorite) {
            onFavoriteClick();
        }

    }
    private void onReviewClick(TextView view) {
        view.setMaxLines(view.getMaxLines() == 5 ? 500 : 5);
    }

    private void onThumbnailClick(View view) {
        String videoUrl = (String) view.getTag(R.id.glide_tag);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(intent);
    }
    private void onFavoriteClick() {
        movieDetailsPresenter.onFavoriteClick(movie);
    }



    @Override
    public void showDetails(Movie movie) {
        Glide.with(requireContext()).load(Api.getBackdropPath(movie.getBackdropPath())).into(binding.moviePoster);
        binding.movieName.setText(movie.getTitle());
        binding.movieYear.setText(getString(R.string.release_date, movie.getReleaseDate()));
        binding.movieRating.setText(getString(R.string.rating, String.valueOf(movie.getVoteAverage())));
        binding.movieDescription.setText(movie.getOverview());
        movieDetailsPresenter.showTrailers(movie);
        movieDetailsPresenter.showReviews(movie);

    }

    @Override
    public void showTrailers(List<Video> trailers) {
        boolean isEmpty = trailers == null || trailers.isEmpty();
        assert binding.trailersAndReviews != null;
        binding.trailersAndReviews.trailersContainer.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        binding.trailersAndReviews.trailersLabel.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        binding.trailersAndReviews.trailers.setVisibility(isEmpty ? View.GONE : View.VISIBLE);

        if (!isEmpty) {
            trailerAdapter.updateData(trailers);
        }
    }

    @Override
    public void showReviews(List<Review> reviews) {
        boolean isEmpty = reviews == null || reviews.isEmpty();
        binding.trailersAndReviews.reviewsLabel.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        binding.trailersAndReviews.reviews.setVisibility(isEmpty ? View.GONE : View.VISIBLE);

        if (!isEmpty) {
            reviewAdapter.updateData(reviews);
        }

    }

    @Override
    public void showFavorited() {
        binding.favorite.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_white_24dp));
    }

    @Override
    public void showUnFavorited() {
        binding.favorite.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite_border_white_24dp));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        movieDetailsPresenter.destroy();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((BaseApplication) requireActivity().getApplication()).releaseDetailsComponent();
    }
}

