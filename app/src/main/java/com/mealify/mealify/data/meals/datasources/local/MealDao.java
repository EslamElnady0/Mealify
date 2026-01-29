package com.mealify.mealify.data.meals.datasources.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mealify.mealify.data.meals.model.meal.MealEntity;

import java.util.List;

@Dao
public interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMeal(MealEntity meal);

    @Query("SELECT * FROM meals WHERE id = :mealId")
    MealEntity getMealById(String mealId);

    @Query("SELECT * FROM meals")
    LiveData<List<MealEntity>> getAllMeals();

    @Delete
    void delete(MealEntity meal);
}
