package com.mealify.mealify.data.weeklyplan.datasource.local;

import android.content.Context;

import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealEntity;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealType;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;
import com.mealify.mealify.db.AppDatabase;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class WeeklyPlanLocalDataSource {
    private final WeeklyPlanDao weeklyPlanDao;

    public WeeklyPlanLocalDataSource(Context context) {
        this.weeklyPlanDao = AppDatabase.getInstance(context).weeklyPlanDao();
    }

    public Completable addMealToWeeklyPlan(WeeklyPlanMealEntity mealEntity) {
        return weeklyPlanDao.addMealToPlan(mealEntity);
    }

    public Observable<List<WeeklyPlanMealWithMeal>> getWeekMeals(String startDate, String endDate) {
        return weeklyPlanDao.getWeekMeals(startDate, endDate);
    }

    public Observable<List<WeeklyPlanMealWithMeal>> getMealsByDate(String date) {
        return weeklyPlanDao.getMealsByDate(date);
    }

    public Maybe<WeeklyPlanMealWithMeal> getMealByDateAndType(String date, WeeklyPlanMealType mealType) {
        return weeklyPlanDao.getMealByDateAndType(date, mealType);
    }

    public Completable deleteMealById(long id) {
        return weeklyPlanDao.deleteMealById(id);
    }

    public Completable deleteMealByDateAndType(String date, WeeklyPlanMealType mealType) {
        return weeklyPlanDao.deleteMealByDateAndType(date, mealType);
    }

    public Completable clearWeeklyPlan() {
        return weeklyPlanDao.clearAllPlannedMeals();
    }

    public Single<Integer> getPlannedMealsCount() {
        return weeklyPlanDao.getPlannedMealsCount();
    }

    public Observable<List<String>> getAllPlannedDates() {
        return weeklyPlanDao.getAllPlannedDates();
    }
}