package com.mealify.mealify.presentation.search.presenter.searchresults;

import com.mealify.mealify.data.meals.model.filteredmeals.FilterType;

public interface MealSearchResultPresenter {
    void getFilteredMeals(FilterType filterType, String query);

    void searchMeals(String query);
}
