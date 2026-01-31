package com.mealify.mealify.presentation.search.presenter.name;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mealify.mealify.data.repos.meals.MealsRepo;
import com.mealify.mealify.presentation.search.views.name.SearchNameView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class SearchNamePresenterImpl implements SearchNamePresenter {
    private SearchNameView view;
    private MealsRepo mealsRepo;

    public SearchNamePresenterImpl(Context ctx, SearchNameView view) {
        this.view = view;
        this.mealsRepo = new MealsRepo(ctx);
    }

    @Override
    @SuppressLint("CheckResult")
    public void searchMealsByName(String name) {
        view.toggleLoading(true);
        mealsRepo.searchMealsByName(name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> {
                            view.toggleLoading(false);
                            view.showMeals(data);
                        },
                        error -> {
                            view.toggleLoading(false);
                            view.showError(error.getMessage());
                        }
                );
    }
}
