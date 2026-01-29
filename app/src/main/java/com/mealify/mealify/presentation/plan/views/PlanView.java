package com.mealify.mealify.presentation.plan.views;

import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;

import java.util.List;

public interface PlanView {
    void showMeals(List<WeeklyPlanMealWithMeal> meals);

    void showError(String message);

    void showPlannedDates(List<String> dates);
}