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
import com.mealify.mealify.data.meals.model.meal.MealEntity;
import com.mealify.mealify.presentation.meals.presenter.MealDetailsPresenter;
import com.mealify.mealify.presentation.meals.presenter.MealDetailsPresenterImpl;

public class MealDetailsFragment extends Fragment implements MealDetailsView {

    private ProgressBar progressBar;
    private NestedScrollView contentScrollView;
    private ImageView mealImageView;
    private TextView areaText;
    private TextView mealNameText;
    private TextView categoryText;
    private TextView ingCountText;
    private RecyclerView ingredientsRecycler;
    private TextView instructionsText;
    private ImageButton backButton;

    private IngredientAdapter ingredientAdapter;
    private MealDetailsPresenter presenter;

    private int mealId;

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

        backButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });
    }

    private void initViews(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        contentScrollView = view.findViewById(R.id.contentScrollView);

        // Header views
        View header = view.findViewById(R.id.mealHeader);
        mealImageView = header.findViewById(R.id.mealImageView);
        areaText = header.findViewById(R.id.area_text);
        mealNameText = header.findViewById(R.id.meal_name);
        categoryText = header.findViewById(R.id.category_text);
        backButton = view.findViewById(R.id.back_button);

        ingCountText = view.findViewById(R.id.ing_count);
        ingredientsRecycler = view.findViewById(R.id.ingredientsRecycler);
        instructionsText = view.findViewById(R.id.instructions);
    }

    private void setupRecyclerView() {
        ingredientAdapter = new IngredientAdapter();
        ingredientsRecycler.setAdapter(ingredientAdapter);
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
            instructionsText.setText(mealDetails.getInstructions());

            if (mealDetails.getIngredients() != null) {
                ingCountText.setText(mealDetails.getIngredients().size() + " items");
                ingredientAdapter.setIngredients(mealDetails.getIngredients());
            }
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        if (getContext() != null) {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        }
    }
}