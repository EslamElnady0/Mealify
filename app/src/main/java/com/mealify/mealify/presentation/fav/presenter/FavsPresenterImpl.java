package com.mealify.mealify.presentation.fav.presenter;

import androidx.lifecycle.LiveData;


import com.mealify.mealify.data.favs.model.fav.FavouriteWithMeal;
import com.mealify.mealify.data.favs.repo.FavRepo;
import com.mealify.mealify.presentation.fav.views.FavsView;

import java.util.List;

public class FavsPresenterImpl implements FavsPresenter {

    private final FavRepo favRepo;
    private FavsView view;

    public FavsPresenterImpl(FavRepo favRepo) {
        this.favRepo = favRepo;
    }

    @Override
    public void getFavouriteMeals() {
        view.toggleLoading(true);

        try {
            LiveData<List<FavouriteWithMeal>> favMealsLiveData = favRepo.getFavouriteMeals();
            view.toggleLoading(false);
            view.onFavsSuccess(favMealsLiveData);

        } catch (Exception e) {
            view.toggleLoading(false);
            view.onFavsFailure(e.getMessage());
        }
    }

    @Override
    public void removeFavouriteMeal(String mealId) {

        if(favRepo.isMealFavorite(mealId)){
            favRepo.deleteMealFromFavorites(mealId);
        }else{
            view.onFavsFailure("Meal is not favorite");
        }

    }
}
