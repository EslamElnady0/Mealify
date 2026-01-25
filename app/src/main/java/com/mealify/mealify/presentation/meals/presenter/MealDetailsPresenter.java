package com.mealify.mealify.presentation.meals.presenter;

import com.mealify.mealify.data.meals.model.meal.MealEntity;

public interface MealDetailsPresenter {
    void getMealDetails(String id);
    void isMealFavorite(String mealId);
    void toggleFavorite(MealEntity meal);
}
