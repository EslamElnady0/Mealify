
package com.mealify.mealify.presentation.home.views;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.mealify.mealify.R;
import com.mealify.mealify.core.helper.CustomToast;
import com.mealify.mealify.data.meals.model.category.CategoryDto;
import com.mealify.mealify.data.meals.model.meal.MealDto;
import com.mealify.mealify.presentation.home.presenter.HomePresenter;
import com.mealify.mealify.presentation.home.presenter.HomePresenterImpl;

import java.util.List;


public class HomeContentFragment extends Fragment implements HomeView {
    private HomePresenter presenter;

    private RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;
    private MaterialCardView mealOfTheDayCard;
    private FrameLayout mealOfTheDayLoading;
    private FrameLayout categoriesLoading;
    private TextView mealOfTheDayText;
    private TextView browseCats;
    private TextView seeAll;


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
        presenter = new HomePresenterImpl(requireContext(), this);
        View view = inflater.inflate(R.layout.fragment_home_content, container, false);

        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        mealOfTheDayCard = view.findViewById(R.id.mealOfTheDayCard);
        mealOfTheDayLoading = view.findViewById(R.id.mealOfTheDayLoading);
        categoriesLoading = view.findViewById(R.id.categoriesLoading);
        mealOfTheDayText = view.findViewById(R.id.mealOfTheDayTitle);
        browseCats = view.findViewById(R.id.browseCategoriesTitle);
        seeAll = view.findViewById(R.id.seeAllButton);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getMealOfTheDay();
        presenter.getCategories();
        setupCategoriesRecyclerView();
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
    }

    @Override
    public void showMealOfTheDay(MealDto meal) {
        bindMealOfTheDay(meal);
    }

    @Override
    public void showCategories(List<CategoryDto> categories) {
        categoryAdapter.setCategories(categories);
    }

    @Override
    public void showError(String message) {
        CustomToast.show(requireContext(), message);
    }

    @Override
    public void toggleMealOfTheDayLoading(boolean isLoading) {
        mealOfTheDayLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        mealOfTheDayCard.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mealOfTheDayText.setVisibility(isLoading ? View.GONE : VISIBLE);
    }

    @Override
    public void toggleCategoriesLoading(boolean isLoading) {
        categoriesLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        categoriesRecyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        browseCats.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        seeAll.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }
}
