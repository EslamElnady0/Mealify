package com.mealify.mealify.data.meals.datasources.local;


import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mealify.mealify.data.meals.model.meal.MealEntity;
import com.mealify.mealify.db.AppDatabase;

import java.util.List;

public class MealLocalDataSource {
    private final MealDao mealDao;

    public MealLocalDataSource(Context ctx) {
        this.mealDao = AppDatabase.getInstance(ctx).mealDao();
    }

    public void insertMeal(MealEntity meal) {
        new Thread(() -> {
            mealDao.insert(meal);
        }).start();
    }

    public MealEntity getMealById(String mealId) {
        return mealDao.getMealById(mealId);
    }

    public LiveData<List<MealEntity>> getAllMeals() {
        return mealDao.getAllMeals();
    }

    public void deleteMeal(MealEntity meal) {
        new Thread(() -> {
            mealDao.delete(meal);
        }).start();
    }

    public boolean isMealCached(String mealId) {
        return mealDao.getMealById(mealId) != null;
    }
}
