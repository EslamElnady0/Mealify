package com.mealify.mealify.presentation.fav.presenter;

import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.data.models.fav.FavouriteWithMeal;
import com.mealify.mealify.data.repos.auth.AuthRepo;
import com.mealify.mealify.data.repos.meals.MealsRepo;
import com.mealify.mealify.presentation.fav.views.FavsView;

import java.util.List;

public class FavsPresenterImpl implements FavsPresenter {

    private final MealsRepo mealsRepo;
    private final AuthRepo authRepo;
    private FavsView view;

    public FavsPresenterImpl(MealsRepo mealsRepo, FavsView view, android.content.Context context) {
        this.mealsRepo = mealsRepo;
        this.view = view;
        this.authRepo = new AuthRepo(context);
    }

    @Override
    public void getFavouriteMeals() {
        if (authRepo.getCurrentUser() != null && authRepo.getCurrentUser().isAnonymous()) {
            view.setGuestMode(true);
            return;
        }
        view.setGuestMode(false);
        mealsRepo.getFavouriteMeals(new GeneralResponse<List<FavouriteWithMeal>>() {
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
        mealsRepo.isMealFavorite(mealId, new GeneralResponse<Boolean>() {
            @Override
            public void onSuccess(Boolean isFav) {
                mealsRepo.deleteMealFromFavorites(mealId);
            }

            @Override
            public void onError(String errorMessage) {
                view.onFavsFailure("Meal is not favorite");
            }
        });
    }
}
