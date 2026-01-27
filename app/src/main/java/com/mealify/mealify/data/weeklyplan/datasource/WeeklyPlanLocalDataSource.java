package com.mealify.mealify.data.weeklyplan.datasource;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealEntity;
import com.mealify.mealify.db.AppDatabase;

import java.util.List;

public class WeeklyPlanLocalDataSource {

    private final WeeklyPlanDao weeklyPlanDao;

    public WeeklyPlanLocalDataSource(Context context) {
        this.weeklyPlanDao = AppDatabase.getInstance(context).weeklyPlanDao();
    }
    public void addMealToWeeklyPlan(WeeklyPlanMealEntity mealEntity) {
        weeklyPlanDao.addMealToPlan(mealEntity);
    }

    public LiveData<List<WeeklyPlanMealWithMeal>> getWeekMeals(String startDate, String endDate) {
        return weeklyPlanDao.getWeekMeals(startDate, endDate);
    }

    public LiveData<List<WeeklyPlanMealWithMeal>> getMealsByDate(String date) {
        return weeklyPlanDao.getMealsByDate(date);
    }

    public void deleteMealById(long id) {
        weeklyPlanDao.deleteMealById(id);
    }

    public void clearWeeklyPlan() {
        weeklyPlanDao.clearAllPlannedMeals();
    }

    public int getPlannedMealsCount() {
        return weeklyPlanDao.getPlannedMealsCount();
    }
}
