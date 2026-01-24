package com.mealify.mealify.presentation.meals.views;

import com.mealify.mealify.data.meals.model.meal.MealEntity;

public interface MealDetailsView {
    void toggleLoading(boolean isLoading);
    void onSuccess(MealEntity mealDetails);
    void onFailure(String errorMessage);
}
