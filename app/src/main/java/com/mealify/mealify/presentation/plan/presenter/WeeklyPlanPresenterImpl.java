package com.mealify.mealify.presentation.plan.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.mealify.mealify.data.repos.auth.AuthRepo;
import com.mealify.mealify.data.repos.meals.MealsRepo;
import com.mealify.mealify.presentation.plan.views.PlanView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class WeeklyPlanPresenterImpl implements WeeklyPlanPresenter {
    private PlanView view;
    private final MealsRepo mealsRepo;
    private final AuthRepo authRepo;

    public WeeklyPlanPresenterImpl(Context ctx, PlanView view) {
        this.mealsRepo = new MealsRepo(ctx);
        this.view = view;
        this.authRepo = new AuthRepo(ctx);
    }

    @Override
    @SuppressLint("CheckResult")
    public void loadMealsForDate(String dateString) {
        if (authRepo.getCurrentUser() != null && authRepo.getCurrentUser().isAnonymous()) {
            view.setGuestMode(true);
            return;
        }
        view.setGuestMode(false);
        mealsRepo.getMealsByDate(dateString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> view.showMeals(data),
                        error -> {}
                );
    }

    @Override
    @SuppressLint("CheckResult")
    public void deleteMealFromPlan(long planId) {
        Log.i("TAG", "deleteMealFromPlan: " + planId);
        mealsRepo.deleteMealFromPlan(planId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    @SuppressLint("CheckResult")
    public void getAllPlannedDates() {
        mealsRepo.getAllPlannedDates()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> view.showPlannedDates(data),
                        error -> {}
                );
    }
}
