package com.mealify.mealify.presentation.search.presenter.searchresults;

import com.mealify.mealify.data.models.filteredmeals.FilterType;

public interface MealSearchResultPresenter {
    void getFilteredMeals(FilterType filterType, String query);

    void searchMeals(String query);
}
