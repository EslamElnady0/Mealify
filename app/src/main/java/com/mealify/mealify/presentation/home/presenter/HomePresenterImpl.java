package com.mealify.mealify.presentation.home.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mealify.mealify.data.repos.meals.MealsRepo;
import com.mealify.mealify.presentation.home.views.HomeView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class HomePresenterImpl implements HomePresenter {

    private final HomeView view;
    private final MealsRepo mealsRepo;

    public HomePresenterImpl(Context context, HomeView view) {
        this.view = view;
        this.mealsRepo = new MealsRepo(context);
    }

    @Override
    @SuppressLint("CheckResult")
    public void getMealOfTheDay() {
        view.toggleMealOfTheDayLoading(true);
        mealsRepo.getRandomMeal()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> {
                            view.toggleMealOfTheDayLoading(false);
                            if (data != null && !data.isEmpty()) {
                                view.showMealOfTheDay(data.get(0));
                            }
                        },
                        error -> {
                            view.toggleMealOfTheDayLoading(false);
                            view.showError(error.getMessage());
                        }
                );
    }

    @Override
    @SuppressLint("CheckResult")
    public void getCategories() {
        view.toggleCategoriesLoading(true);
        mealsRepo.getCategories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> {
                            view.toggleCategoriesLoading(false);
                            view.showCategories(data);
                        },
                        error -> {
                            view.toggleCategoriesLoading(false);
                            view.showError(error.getMessage());
                        }
                );
    }
}
