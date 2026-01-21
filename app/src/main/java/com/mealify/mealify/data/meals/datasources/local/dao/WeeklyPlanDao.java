package com.mealify.mealify.data.meals.datasources.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import com.mealify.mealify.data.meals.model.weeklyplan.WeeklyPlanMealEntity;
import com.mealify.mealify.data.meals.model.weeklyplan.WeeklyPlanMeal;
import java.util.List;

@Dao
public interface WeeklyPlanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMealToPlan(WeeklyPlanMealEntity planMeal);

    @Transaction
    @Query("SELECT * FROM weekly_plan_meals WHERE dateString BETWEEN :startDate AND :endDate ORDER BY dateString, mealType")
    List<WeeklyPlanMeal> getWeekMeals(String startDate, String endDate);

    @Transaction
    @Query("SELECT * FROM weekly_plan_meals WHERE dateString = :date ORDER BY mealType")
    List<WeeklyPlanMeal> getMealsByDate(String date);

    @Query("DELETE FROM weekly_plan_meals WHERE id = :id")
    void deleteMealById(long id);

    @Query("DELETE FROM weekly_plan_meals")
    void clearAllPlannedMeals();

    @Query("SELECT COUNT(*) FROM weekly_plan_meals")
    int getPlannedMealsCount();

}