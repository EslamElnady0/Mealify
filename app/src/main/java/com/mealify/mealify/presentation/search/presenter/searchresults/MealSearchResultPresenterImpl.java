package com.mealify.mealify.presentation.search.presenter.searchresults;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mealify.mealify.data.models.filteredmeals.FilterType;
import com.mealify.mealify.data.models.filteredmeals.FilteredMeal;
import com.mealify.mealify.data.repos.meals.MealsRepo;
import com.mealify.mealify.presentation.search.views.searchresults.MealSearchResultView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class MealSearchResultPresenterImpl implements MealSearchResultPresenter {

    private final MealSearchResultView view;
    private final MealsRepo mealsRepo;
    private List<FilteredMeal> allMeals = new ArrayList<>();

    public MealSearchResultPresenterImpl(Context context, MealSearchResultView view) {
        this.view = view;
        this.mealsRepo = new MealsRepo(context);
    }

    @Override
    @SuppressLint("CheckResult")
    public void getFilteredMeals(FilterType filterType, String query) {
        view.toggleLoading(true);
        mealsRepo.getFilteredMeals(filterType, query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> {
                            view.toggleLoading(false);
                            allMeals = data;
                            view.showSearchResults(data);
                        },
                        error -> {
                            view.toggleLoading(false);
                            view.showError(error.getMessage());
                        }
                );
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
