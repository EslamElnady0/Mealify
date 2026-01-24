package com.mealify.mealify.presentation.home.views;

import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.mealify.mealify.InnerAppFragmentDirections;
import com.mealify.mealify.R;
import com.mealify.mealify.core.helper.CustomToast;
import com.mealify.mealify.data.meals.model.category.CategoryDto;
import com.mealify.mealify.data.meals.model.filteredmeals.FilterType;
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
    private NavController navController;


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
        mealOfTheDayCard.setVisibility(VISIBLE);
        mealOfTheDayCard.setOnClickListener(v -> {
            if (!isAdded()) return;
            int mealId = Integer.parseInt(meal.id);
            InnerAppFragmentDirections.ActionInnerAppFragmentToMealDetailsFragment action =
                    InnerAppFragmentDirections.actionInnerAppFragmentToMealDetailsFragment(mealId);
            NavHostFragment navHostFragment = (NavHostFragment)
                    getActivity().getSupportFragmentManager().findFragmentById(R.id.inner_home_container);
            navController = navHostFragment.getNavController();
            navController.navigate(action);
        });
    }

    private void setupCategoriesRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        categoriesRecyclerView.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(category -> {
            InnerAppFragmentDirections.ActionInnerAppFragmentToMealSearchResultFragment action =
                    InnerAppFragmentDirections.actionInnerAppFragmentToMealSearchResultFragment(FilterType.CATEGORY, category.name);
            NavHostFragment navHostFragment = (NavHostFragment)
                    getActivity().getSupportFragmentManager().findFragmentById(R.id.inner_home_container);
            NavController navController = navHostFragment.getNavController();
            navController.navigate(action);
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
