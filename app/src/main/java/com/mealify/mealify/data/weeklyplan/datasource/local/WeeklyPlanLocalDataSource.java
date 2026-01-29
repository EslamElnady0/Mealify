package com.mealify.mealify.data.weeklyplan.datasource.local;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealEntity;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealType;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;
import com.mealify.mealify.db.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeeklyPlanLocalDataSource {

    private final WeeklyPlanDao weeklyPlanDao;
    private final ExecutorService executorService;

    public WeeklyPlanLocalDataSource(Context context) {
        this.weeklyPlanDao = AppDatabase.getInstance(context).weeklyPlanDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void addMealToWeeklyPlan(WeeklyPlanMealEntity mealEntity) {
        executorService.execute(() -> weeklyPlanDao.addMealToPlan(mealEntity));
    }

    public LiveData<List<WeeklyPlanMealWithMeal>> getWeekMeals(String startDate, String endDate) {
        return weeklyPlanDao.getWeekMeals(startDate, endDate);
    }

    public LiveData<List<WeeklyPlanMealWithMeal>> getMealsByDate(String date) {
        return weeklyPlanDao.getMealsByDate(date);
    }

    public void deleteMealById(long id) {
        executorService.execute(() -> weeklyPlanDao.deleteMealById(id));
    }

    public void deleteMealByDateAndType(String date, WeeklyPlanMealType mealType) {
        executorService.execute(() -> weeklyPlanDao.deleteMealByDateAndType(date, mealType));
    }

    public void clearWeeklyPlan() {
        executorService.execute(() -> weeklyPlanDao.clearAllPlannedMeals());
    }

    public int getPlannedMealsCount() {
        return weeklyPlanDao.getPlannedMealsCount();
    }

    public LiveData<List<String>> getAllPlannedDates() {
        return weeklyPlanDao.getAllPlannedDates();
    }
}