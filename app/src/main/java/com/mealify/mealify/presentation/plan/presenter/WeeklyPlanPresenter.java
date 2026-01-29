package com.mealify.mealify.presentation.plan.presenter;

public interface WeeklyPlanPresenter {

    public void loadMealsForDate(String dateString);

    void deleteMealFromPlan(long planId);

    void getAllPlannedDates();
}
