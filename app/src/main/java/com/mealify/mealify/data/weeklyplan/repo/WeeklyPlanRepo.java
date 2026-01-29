package com.mealify.mealify.data.weeklyplan.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mealify.mealify.data.meals.datasources.local.MealLocalDataSource;
import com.mealify.mealify.data.weeklyplan.datasource.local.WeeklyPlanLocalDataSource;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealType;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeeklyPlanRepo {
    private final WeeklyPlanLocalDataSource localDataSource;
    private final MealLocalDataSource mealLocalDataSource;
    private final ExecutorService executorService;

    public WeeklyPlanRepo(Context context) {
        this.localDataSource = new WeeklyPlanLocalDataSource(context);
        this.mealLocalDataSource = new MealLocalDataSource(context);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void addMealToPlan(WeeklyPlanMealWithMeal planMealWithMeal) {
        executorService.execute(() -> {
            mealLocalDataSource.insertMeal(planMealWithMeal.meal);
            localDataSource.addMealToWeeklyPlan(planMealWithMeal.planEntry);
        });
    }

    public void deleteMealByDateAndType(String date, WeeklyPlanMealType mealType) {
        executorService.execute(() -> {
            localDataSource.deleteMealByDateAndType(date, mealType);
        });
    }

    public LiveData<List<WeeklyPlanMealWithMeal>> getMealsByDate(String date) {
        return localDataSource.getMealsByDate(date);
    }

    public WeeklyPlanMealWithMeal getMealByDateAndType(String date, WeeklyPlanMealType type) {
        return localDataSource.getMealByDateAndType(date, type);
    }

    public LiveData<List<WeeklyPlanMealWithMeal>> getWeekMeals(String startDate, String endDate) {
        return localDataSource.getWeekMeals(startDate, endDate);
    }

    public void deleteMealFromPlan(long planId) {
        localDataSource.deleteMealById(planId);
    }

    public void clearAllPlannedMeals() {
        localDataSource.clearWeeklyPlan();
    }

    public LiveData<List<String>> getAllPlannedDates() {
        return localDataSource.getAllPlannedDates();
    }
}