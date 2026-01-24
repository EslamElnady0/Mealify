package com.mealify.mealify.presentation.search.views.searchresults;

import com.mealify.mealify.data.meals.model.filteredmeals.FilteredMeal;

import java.util.List;

public interface MealSearchResultView {
    void showSearchResults(List<FilteredMeal> meals);

    void showError(String errorMessage);

    void toggleLoading(boolean isLoading);
}
