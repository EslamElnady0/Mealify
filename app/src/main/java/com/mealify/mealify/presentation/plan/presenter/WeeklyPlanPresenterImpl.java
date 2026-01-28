package com.mealify.mealify.presentation.plan.presenter;

import android.content.Context;

import androidx.lifecycle.LiveData;

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
    public void loadMealsForToday() {

    }

    @Override
    public void loadMealsForDate(String dateString) {
        LiveData<List<WeeklyPlanMealWithMeal>> meals = weeklyPlanRepo.getMealsByDate(dateString);
        view.showMeals(meals);
    }
}
