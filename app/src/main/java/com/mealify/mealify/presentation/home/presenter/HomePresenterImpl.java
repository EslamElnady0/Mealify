
package com.mealify.mealify.presentation.home.presenter;

import android.content.Context;

import com.mealify.mealify.core.response.ApiResponse;
import com.mealify.mealify.data.meals.model.category.CategoryDto;
import com.mealify.mealify.data.meals.model.meal.MealDto;
import com.mealify.mealify.data.meals.repo.MealsRepo;
import com.mealify.mealify.presentation.home.views.HomeView;

import java.util.List;

public class HomePresenterImpl implements HomePresenter {

    private final HomeView view;
    private final MealsRepo mealsRepo;

    public HomePresenterImpl(Context context, HomeView view) {
        this.view = view;
        this.mealsRepo = new MealsRepo(context);
    }

    @Override
    public void getMealOfTheDay() {
        view.toggleMealOfTheDayLoading(true);
        mealsRepo.getRandomMeal(new ApiResponse<List<MealDto>>() {
            @Override
            public void onSuccess(List<MealDto> data) {
                view.toggleMealOfTheDayLoading(false);
                view.showMealOfTheDay(data.get(0));
            }

            @Override
            public void onError(String errorMessage) {
                view.toggleMealOfTheDayLoading(false);
                view.showError(errorMessage);
            }
        });
    }

    @Override
    public void getCategories() {
        view.toggleCategoriesLoading(true);
        mealsRepo.getCategories(new ApiResponse<List<CategoryDto>>() {
            @Override
            public void onSuccess(List<CategoryDto> data) {
                view.toggleCategoriesLoading(false);
                view.showCategories(data);
            }

            @Override
            public void onError(String errorMessage) {
                view.toggleCategoriesLoading(false);
                view.showError(errorMessage);
            }
        });
    }
}
