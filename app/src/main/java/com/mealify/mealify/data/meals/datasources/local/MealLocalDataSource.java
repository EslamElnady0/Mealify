package com.mealify.mealify.data.meals.datasources.local;

import android.content.Context;

import com.mealify.mealify.data.meals.model.meal.MealEntity;
import com.mealify.mealify.db.AppDatabase;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class MealLocalDataSource {
    private final MealDao mealDao;

    public MealLocalDataSource(Context ctx) {
        this.mealDao = AppDatabase.getInstance(ctx).mealDao();
    }

    public Completable insertMeal(MealEntity meal) {
        return mealDao.insertMeal(meal);
    }

    public Completable insertMeals(List<MealEntity> meals) {
        return mealDao.insertMeals(meals);
    }

    public Single<MealEntity> getMealById(String mealId) {
        return mealDao.getMealById(mealId);
    }

    public Observable<List<MealEntity>> getAllMeals() {
        return mealDao.getAllMeals();
    }

    public Completable deleteMeal(MealEntity meal) {
        return mealDao.delete(meal);
    }

    public Completable deleteAllMeals() {
        return mealDao.deleteAllMeals();
    }
}