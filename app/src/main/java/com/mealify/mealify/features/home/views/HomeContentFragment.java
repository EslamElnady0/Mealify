package com.mealify.mealify.features.home.views;

import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.mealify.mealify.R;
import com.mealify.mealify.core.datasource.remote.response.ApiResponse;
import com.mealify.mealify.core.helper.CustomToast;
import com.mealify.mealify.features.home.data.datasource.remote.HomeRemoteDataSource;
import com.mealify.mealify.features.home.data.model.category.CategoryDto;
import com.mealify.mealify.features.home.data.model.meal.MealDto;

import java.util.List;


public class HomeContentFragment extends Fragment {
    private HomeRemoteDataSource homeRemoteDataSource;

    private RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;
    private MaterialCardView mealOfTheDayCard;

    public HomeContentFragment() {

    }

    public static HomeContentFragment newInstance() {
        return new HomeContentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeRemoteDataSource = new HomeRemoteDataSource();
        View view = inflater.inflate(R.layout.fragment_home_content, container, false);

        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        mealOfTheDayCard = view.findViewById(R.id.mealOfTheDayCard);
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupMealOfTheDayCard();
        setupCategoriesRecyclerView();
    }

    private void setupMealOfTheDayCard() {
        homeRemoteDataSource.getRandomMeal(new ApiResponse<List<MealDto>>() {
            @Override
            public void onSuccess(List<MealDto> data) {
                bindMealOfTheDay(data.get(0));
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
    private void bindMealOfTheDay(MealDto meal) {
        if (!isAdded()) return;

        mealOfTheDayCard.setVisibility(VISIBLE);
        TextView title = mealOfTheDayCard.findViewById(R.id.mealName);
        Chip category = mealOfTheDayCard.findViewById(R.id.categoryChip);
        Chip area = mealOfTheDayCard.findViewById(R.id.typeChip);
        ImageView mealImage = mealOfTheDayCard.findViewById(R.id.mealImage);

        title.setText(meal.name);
        category.setText(meal.category);
        area.setText(meal.area);

        Glide.with(this)
                .load(meal.thumbnail)
                .into(mealImage);

        mealOfTheDayCard.setOnClickListener(v -> {
            if (!isAdded()) return;
            CustomToast.show(getContext(), "Clicked on Meal of the Day: " + meal.name);
        });
    }

    private void setupCategoriesRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(), 
                LinearLayoutManager.HORIZONTAL, 
                false
        );
        categoriesRecyclerView.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter( category -> {
            CustomToast.show(requireContext(), "Clicked on category: " + category.name);
        });

        categoriesRecyclerView.setAdapter(categoryAdapter);
        homeRemoteDataSource.getCategories(new ApiResponse<List<CategoryDto>>() {
            @Override
            public void onSuccess(List<CategoryDto> data) {
                categoryAdapter.setCategories(data);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });


        categoriesRecyclerView.setAdapter(categoryAdapter);
    }
}
