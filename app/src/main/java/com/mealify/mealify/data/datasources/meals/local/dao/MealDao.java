package com.mealify.mealify.data.datasources.meals.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mealify.mealify.data.models.meal.MealEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMeal(MealEntity meal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMeals(List<MealEntity> meals);

    @Query("SELECT * FROM meals WHERE id = :mealId")
    Single<MealEntity> getMealById(String mealId);

    @Query("SELECT * FROM meals")
    Observable<List<MealEntity>> getAllMeals();

    @Delete
    Completable delete(MealEntity meal);

    @Query("DELETE FROM meals")
    Completable deleteAllMeals();
}
