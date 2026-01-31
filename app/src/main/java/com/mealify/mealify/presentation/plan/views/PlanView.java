package com.mealify.mealify.presentation.plan.views;

import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealWithMeal;

import java.util.List;

public interface PlanView {
    void showMeals(List<WeeklyPlanMealWithMeal> meals);

    void showError(String message);
    void onDeleteSuccess(String message);

    void showPlannedDates(List<String> dates);

    void setGuestMode(boolean isGuest);
}