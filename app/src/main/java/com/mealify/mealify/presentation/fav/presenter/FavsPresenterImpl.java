package com.mealify.mealify.presentation.fav.presenter;

import androidx.lifecycle.LiveData;


import com.mealify.mealify.data.meals.model.meal.MealEntity;
import com.mealify.mealify.data.favs.model.fav.FavouriteWithMeal;
import com.mealify.mealify.data.favs.repo.FavRepo;
import com.mealify.mealify.presentation.fav.views.FavsView;

import java.util.List;

public class FavsPresenterImpl implements FavsPresenter {

    private final FavRepo favRepo;
    private FavsView view;

    public FavsPresenterImpl(FavRepo favRepo, FavsView view) {
        this.favRepo = favRepo;
        this.view = view;
    }

    @Override
    public void getFavouriteMeals() {

        try {
            LiveData<List<FavouriteWithMeal>> favMealsLiveData = favRepo.getFavouriteMeals();
            view.onFavsSuccess(favMealsLiveData);

        } catch (Exception e) {
            view.onFavsFailure(e.getMessage());
        }
    }

    @Override
    public void removeFavouriteMeal(String mealId) {
        new Thread(() -> {
            if (favRepo.isMealFavorite(mealId)) {
                favRepo.deleteMealFromFavorites(mealId);
            } else {
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                    view.onFavsFailure("Meal is not favorite");
                });
            }
        }).start();
    }
}
