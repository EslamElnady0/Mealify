package com.mealify.mealify.presentation.search.views.name;

import com.mealify.mealify.data.models.filteredmeals.FilteredMeal;

import java.util.List;

public interface SearchNameView {
    void toggleLoading(boolean isLoading);

    void showMeals(List<FilteredMeal> meals);

    void showError(String message);
}
