package com.mealify.mealify.presentation.fav.presenter;

import com.mealify.mealify.data.models.meal.MealEntity;

public interface FavsPresenter {
    void getFavouriteMeals();
    void removeFavouriteMeal(String mealId);
}

