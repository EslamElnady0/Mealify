package com.mealify.mealify.presentation.fav.views;


import com.mealify.mealify.data.favs.model.fav.FavouriteWithMeal;

import java.util.List;

public interface FavsView {
    void onFavsSuccess(List<FavouriteWithMeal> favMeals);

    void onFavsFailure(String errorMessage);
}