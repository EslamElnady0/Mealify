package com.mealify.mealify.presentation.search.presenter.name;

import android.content.Context;

import com.mealify.mealify.core.response.ApiResponse;
import com.mealify.mealify.data.meals.model.filteredmeals.FilteredMeal;
import com.mealify.mealify.data.meals.repo.MealsRepo;
import com.mealify.mealify.presentation.search.views.name.SearchNameView;

import java.util.List;

public class SearchNamePresenterImpl implements SearchNamePresenter {
    private SearchNameView view;
    private MealsRepo mealsRepo;

    public SearchNamePresenterImpl(Context ctx, SearchNameView view) {
        this.view = view;
        this.mealsRepo = new MealsRepo(ctx);
    }

    @Override
    public void searchMealsByName(String name) {
        view.toggleLoading(true);
        mealsRepo.searchMealsByName(name, new ApiResponse<List<FilteredMeal>>() {
            @Override
            public void onSuccess(List<FilteredMeal> data) {
                view.toggleLoading(false);
                view.showMeals(data);
            }

            @Override
            public void onError(String message) {
                view.toggleLoading(false);
                view.showError(message);
            }
        });
    }
}
