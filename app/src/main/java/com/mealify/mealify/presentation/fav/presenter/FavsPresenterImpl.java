package com.mealify.mealify.presentation.fav.presenter;

import com.mealify.mealify.core.response.GeneralResponse;
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
        favRepo.getFavouriteMeals(new GeneralResponse<List<FavouriteWithMeal>>() {
            @Override
            public void onSuccess(List<FavouriteWithMeal> data) {
                view.onFavsSuccess(data);
            }

            @Override
            public void onError(String errorMessage) {
                view.onFavsFailure(errorMessage);
            }
        });
    }

    @Override
    public void removeFavouriteMeal(String mealId) {
        favRepo.isMealFavorite(mealId, new GeneralResponse<Boolean>() {
            @Override
            public void onSuccess(Boolean isFav) {
                favRepo.deleteMealFromFavorites(mealId);
            }

            @Override
            public void onError(String errorMessage) {
                view.onFavsFailure("Meal is not favorite");
            }
        });
    }
}
