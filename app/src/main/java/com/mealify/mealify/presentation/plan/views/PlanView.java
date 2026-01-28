package com.mealify.mealify.presentation.plan.views;

import androidx.lifecycle.LiveData;

import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;

import java.util.List;

public interface PlanView {
    void showMeals(LiveData<List<WeeklyPlanMealWithMeal>> meals);

    void showError(String message);
}