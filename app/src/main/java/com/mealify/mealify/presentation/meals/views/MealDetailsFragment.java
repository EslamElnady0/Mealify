package com.mealify.mealify.presentation.meals.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mealify.mealify.R;
import com.mealify.mealify.core.helper.CustomToast;
import com.mealify.mealify.core.utils.MealDetailsUtils;
import com.mealify.mealify.data.meals.model.meal.MealEntity;
import com.mealify.mealify.presentation.meals.presenter.MealDetailsPresenter;
import com.mealify.mealify.presentation.meals.presenter.MealDetailsPresenterImpl;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class MealDetailsFragment extends Fragment implements MealDetailsView {

    private ProgressBar progressBar;
    private NestedScrollView contentScrollView;
    private TextView errorText;
    private ImageView mealImageView;
    private TextView areaText;
    private TextView mealNameText;
    private TextView categoryText;
    private TextView ingCountText;
    private RecyclerView ingredientsRecycler;
    private RecyclerView stepsRecycler;
    private YouTubePlayerView youtubePlayerView;
    private ImageButton backButton;
    private ImageButton favButton;

    private IngredientAdapter ingredientAdapter;
    private StepAdapter stepAdapter;
    private MealDetailsPresenter presenter;

    private int mealId;
    private MealEntity currentMeal;
    private boolean isFavorite = false;

    public MealDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mealId = MealDetailsFragmentArgs.fromBundle(getArguments()).getMealId();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupRecyclerView();

        presenter = new MealDetailsPresenterImpl(getContext(), this);
        
        presenter.getMealDetails(String.valueOf(mealId));
        presenter.isMealFavorite(String.valueOf(mealId));

        backButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });

        favButton.setOnClickListener(v -> {
            if (currentMeal != null) {
                presenter.toggleFavorite(currentMeal);
            }
        });
    }

    private void initViews(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        errorText = view.findViewById(R.id.errorText);
        contentScrollView = view.findViewById(R.id.contentScrollView);

        // Header views
        View header = view.findViewById(R.id.mealHeader);
        mealImageView = header.findViewById(R.id.mealImageView);
        areaText = header.findViewById(R.id.area_text);
        mealNameText = header.findViewById(R.id.meal_name);
        categoryText = header.findViewById(R.id.category_text);
        backButton = view.findViewById(R.id.back_button);
        favButton = view.findViewById(R.id.fav_btn);

        ingCountText = view.findViewById(R.id.ing_count);
        ingredientsRecycler = view.findViewById(R.id.ingredientsRecycler);
        stepsRecycler = view.findViewById(R.id.stepsRecycler);
        youtubePlayerView = view.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youtubePlayerView);
    }

    private void setupRecyclerView() {
        ingredientAdapter = new IngredientAdapter();
        ingredientsRecycler.setAdapter(ingredientAdapter);

        stepAdapter = new StepAdapter();
        stepsRecycler.setAdapter(stepAdapter);
    }

    @Override
    public void toggleLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            contentScrollView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            contentScrollView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(MealEntity mealDetails) {
        if (mealDetails != null && getContext() != null) {
            Glide.with(this).load(mealDetails.getThumbnail()).into(mealImageView);
            areaText.setText(mealDetails.getArea());
            mealNameText.setText(mealDetails.getName());
            categoryText.setText(mealDetails.getCategory());

            if (mealDetails.getInstructions() != null) {
                stepAdapter.setSteps(MealDetailsUtils.parseInstructions(mealDetails.getInstructions()));
            }

            if (mealDetails.getYoutubeUrl() != null && !mealDetails.getYoutubeUrl().isEmpty()) {
                loadYoutubeVideo(mealDetails.getYoutubeUrl());
            } else {
                youtubePlayerView.setVisibility(View.GONE);
                getView().findViewById(R.id.watch_tut_text).setVisibility(View.GONE);
            }

            if (mealDetails.getIngredients() != null) {
                ingCountText.setText(mealDetails.getIngredients().size() + " items");
                ingredientAdapter.setIngredients(mealDetails.getIngredients());
            }
            this.currentMeal = mealDetails;
        }
    }

    @Override
    public void onFailure(String errorMessage) {
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(errorMessage);
            contentScrollView.setVisibility(View.GONE);
    }

    private void loadYoutubeVideo(String videoUrl) {
        String videoId = MealDetailsUtils.extractVideoId(videoUrl);
        if (videoId != null) {
            youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youtubePlayer) {
                    youtubePlayer.cueVideo(videoId, 0);
                }
            });
        } else {
            youtubePlayerView.setVisibility(View.GONE);
            if (getView() != null) {
                getView().findViewById(R.id.watch_tut_text).setVisibility(View.GONE);
            }
        }
    }
    private void updateFavButton() {
        if (isFavorite) {
            favButton.setImageResource(R.drawable.favourite);
        } else {
            favButton.setImageResource(R.drawable.fav_24);
        }
    }

    @Override
    public void onIsFavoriteResult(boolean isFavorite) {
        this.isFavorite = isFavorite;
        updateFavButton();
    }

    @Override
    public void onToggleFavoriteSuccess(boolean isFavorite) {
        this.isFavorite = isFavorite;
        updateFavButton();
        String message = isFavorite ? "Added to favorites" : "Removed from favorites";
        CustomToast.show(getContext(), message);
    }
}