package com.mealify.mealify.presentation.search.presenter.searchresults;

import android.content.Context;

import com.mealify.mealify.core.response.ApiResponse;
import com.mealify.mealify.data.meals.model.filteredmeals.FilterType;
import com.mealify.mealify.data.meals.model.filteredmeals.FilteredMeal;
import com.mealify.mealify.data.meals.repo.MealsRepo;
import com.mealify.mealify.presentation.search.views.searchresults.MealSearchResultView;

import java.util.ArrayList;
import java.util.List;

public class MealSearchResultPresenterImpl implements MealSearchResultPresenter {

    private final MealSearchResultView view;
    private final MealsRepo mealsRepo;
    private List<FilteredMeal> allMeals = new ArrayList<>();

    public MealSearchResultPresenterImpl(Context context, MealSearchResultView view) {
        this.view = view;
        this.mealsRepo = new MealsRepo(context);
    }

    @Override
    public void getFilteredMeals(FilterType filterType, String query) {
        view.toggleLoading(true);
        mealsRepo.getFilteredMeals(filterType, query, new ApiResponse<List<FilteredMeal>>() {
            @Override
            public void onSuccess(List<FilteredMeal> data) {
                view.toggleLoading(false);
                allMeals = data;
                view.showSearchResults(data);
            }

            @Override
            public void onError(String errorMessage) {
                view.toggleLoading(false);
                view.showError(errorMessage);
            }
        });
    }

    @Override
    public void searchMeals(String query) {
        if (query == null || query.trim().isEmpty()) {
            view.showSearchResults(allMeals);
            return;
        }

        List<FilteredMeal> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase().trim();

        for (FilteredMeal meal : allMeals) {
            if (meal.getStrMeal() != null && meal.getStrMeal().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(meal);
            }
        }

        view.showSearchResults(filteredList);
    }
}
