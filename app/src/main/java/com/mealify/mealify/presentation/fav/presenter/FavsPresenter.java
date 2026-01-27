package com.mealify.mealify.presentation.fav.presenter;

import com.mealify.mealify.data.meals.model.meal.MealEntity;

public interface FavsPresenter {
    void getFavouriteMeals();
    void removeFavouriteMeal(String mealId);
}

