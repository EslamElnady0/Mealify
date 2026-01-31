package com.mealify.mealify.presentation.plan.presenter;

import android.content.Context;
import android.util.Log;

import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealWithMeal;
import com.mealify.mealify.data.repos.auth.AuthRepo;
import com.mealify.mealify.data.repos.meals.MealsRepo;
import com.mealify.mealify.presentation.plan.views.PlanView;

import java.util.List;

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
    public void loadMealsForDate(String dateString) {
        if (authRepo.getCurrentUser() != null && authRepo.getCurrentUser().isAnonymous()) {
            view.setGuestMode(true);
            return;
        }
        view.setGuestMode(false);
        mealsRepo.getMealsByDate(dateString,
                new GeneralResponse<List<WeeklyPlanMealWithMeal>>() {
                    @Override
                    public void onSuccess(List<WeeklyPlanMealWithMeal> data) {
                        view.showMeals(data);
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
    }

    @Override
    public void deleteMealFromPlan(long planId) {
        Log.i("TAG", "deleteMealFromPlan: " + planId);
        mealsRepo.deleteMealFromPlan(planId);
    }

    @Override
    public void getAllPlannedDates() {
        mealsRepo.getAllPlannedDates(new GeneralResponse<List<String>>() {
            @Override
            public void onSuccess(List<String> data) {
                view.showPlannedDates(data);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
}
