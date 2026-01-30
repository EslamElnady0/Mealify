package com.mealify.mealify.presentation.plan.presenter;

import android.content.Context;
import android.util.Log;

import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;
import com.mealify.mealify.data.weeklyplan.repo.WeeklyPlanRepo;
import com.mealify.mealify.presentation.plan.views.PlanView;

import java.util.List;

public class WeeklyPlanPresenterImpl implements WeeklyPlanPresenter {
    private PlanView view;
    private WeeklyPlanRepo weeklyPlanRepo;

    public WeeklyPlanPresenterImpl(Context ctx, PlanView view) {
        this.weeklyPlanRepo = new WeeklyPlanRepo(ctx);
        this.view = view;
    }


    @Override
    public void loadMealsForDate(String dateString) {
        weeklyPlanRepo.getMealsByDate(dateString,
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
        weeklyPlanRepo.deleteMealFromPlan(planId);
    }

    @Override
    public void getAllPlannedDates() {
        weeklyPlanRepo.getAllPlannedDates(new GeneralResponse<List<String>>() {
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
