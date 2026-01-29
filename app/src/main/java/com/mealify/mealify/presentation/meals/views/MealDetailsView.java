package com.mealify.mealify.presentation.meals.views;

import com.mealify.mealify.data.meals.model.meal.MealEntity;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;

public interface MealDetailsView {
    void toggleLoading(boolean isLoading);

    void onSuccess(MealEntity mealDetails);

    void onFailure(String errorMessage);

    void onIsFavoriteResult(boolean isFavorite);

    void onToggleFavoriteSuccess(boolean isFavorite);

    void onWeeklyPlanMealAdded(String message);

    void showReplaceConfirmation(WeeklyPlanMealWithMeal newMeal, WeeklyPlanMealWithMeal existingMeal);
}
