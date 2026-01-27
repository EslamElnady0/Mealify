package com.mealify.mealify.data.weeklyplan.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mealify.mealify.data.meals.datasources.local.MealLocalDataSource;
import com.mealify.mealify.data.weeklyplan.datasource.local.WeeklyPlanLocalDataSource;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;

import java.util.List;

public class WeeklyPlanRepo {

    private final WeeklyPlanLocalDataSource weeklyPlanLocalDataSource;
    private final MealLocalDataSource mealLocalDataSource;

    public WeeklyPlanRepo(
            Context ctx
    ) {
        this.weeklyPlanLocalDataSource = new WeeklyPlanLocalDataSource(ctx);
        this.mealLocalDataSource = new MealLocalDataSource(ctx);
    }

    public void addMealToWeeklyPlan(
            WeeklyPlanMealWithMeal weeklyPlanMealWithMeal
    ) {
        mealLocalDataSource.insertMeal(weeklyPlanMealWithMeal.meal);
        weeklyPlanLocalDataSource.addMealToWeeklyPlan(weeklyPlanMealWithMeal.planEntry);
    }

    public LiveData<List<WeeklyPlanMealWithMeal>> getWeekMeals(
            String startDate,
            String endDate
    ) {
        return weeklyPlanLocalDataSource.getWeekMeals(startDate, endDate);
    }

    public LiveData<List<WeeklyPlanMealWithMeal>> getMealsByDate(String date) {
        return weeklyPlanLocalDataSource.getMealsByDate(date);
    }

    public void deleteMealById(long id) {
        weeklyPlanLocalDataSource.deleteMealById(id);
    }

    public void clearWeeklyPlan() {
        weeklyPlanLocalDataSource.clearWeeklyPlan();
    }

    public int getPlannedMealsCount() {
        return weeklyPlanLocalDataSource.getPlannedMealsCount();
    }
}
