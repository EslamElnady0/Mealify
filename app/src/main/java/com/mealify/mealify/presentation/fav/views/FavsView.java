package com.mealify.mealify.presentation.fav.views;


import androidx.lifecycle.LiveData;

import com.mealify.mealify.data.favs.model.fav.FavouriteWithMeal;

import java.util.List;

public interface FavsView {
    void toggleLoading(boolean isLoading);
    void onFavsSuccess(LiveData<List<FavouriteWithMeal>> favMealsLiveData);
    void onFavsFailure(String errorMessage);
}