package com.mealify.mealify.data.weeklyplan.datasource.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealEntity;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealType;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;

import java.util.List;

@Dao
public interface WeeklyPlanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMealToPlan(WeeklyPlanMealEntity planMeal);

    @Transaction
    @Query("SELECT * FROM weekly_plan_meals WHERE dateString BETWEEN :startDate AND :endDate ORDER BY dateString, mealType")
    LiveData<List<WeeklyPlanMealWithMeal>> getWeekMeals(String startDate, String endDate);

    @Transaction
    @Query("SELECT * FROM weekly_plan_meals WHERE dateString = :date ORDER BY mealType")
    LiveData<List<WeeklyPlanMealWithMeal>> getMealsByDate(String date);

    @Query("DELETE FROM weekly_plan_meals WHERE planId = :id")
    void deleteMealById(long id);

    @Query("DELETE FROM weekly_plan_meals WHERE dateString = :date AND mealType = :mealType")
    void deleteMealByDateAndType(String date, WeeklyPlanMealType mealType);

    @Query("DELETE FROM weekly_plan_meals")
    void clearAllPlannedMeals();

    @Query("SELECT COUNT(*) FROM weekly_plan_meals")
    int getPlannedMealsCount();
}