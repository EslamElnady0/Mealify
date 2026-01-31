package com.mealify.mealify.presentation.fav.presenter;

import android.annotation.SuppressLint;

import com.mealify.mealify.data.repos.auth.AuthRepo;
import com.mealify.mealify.data.repos.meals.MealsRepo;
import com.mealify.mealify.presentation.fav.views.FavsView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

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
    @SuppressLint("CheckResult")
    public void getFavouriteMeals() {
        if (authRepo.getCurrentUser() != null && authRepo.getCurrentUser().isAnonymous()) {
            view.setGuestMode(true);
            return;
        }
        view.setGuestMode(false);
        mealsRepo.getFavouriteMeals()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> view.onFavsSuccess(data),
                        error -> view.onFavsFailure(error.getMessage())
                );
    }

    @Override
    @SuppressLint("CheckResult")
    public void removeFavouriteMeal(String mealId) {
        mealsRepo.isMealFavorite(mealId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isFav -> {
                    if (isFav) {
                        mealsRepo.deleteMealFromFavorites(mealId)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                    }
                }, error -> view.onFavsFailure("Meal is not favorite"));
    }
}
