package com.mealify.mealify.data.weeklyplan.datasource.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealEntity;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealType;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface WeeklyPlanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addMealToPlan(WeeklyPlanMealEntity planMeal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertWeeklyPlan(List<WeeklyPlanMealEntity> plans);

    @Transaction
    @Query("SELECT * FROM weekly_plan_meals WHERE dateString BETWEEN :startDate AND :endDate ORDER BY dateString, mealType")
    Observable<List<WeeklyPlanMealWithMeal>> getWeekMeals(String startDate, String endDate);

    @Transaction
    @Query("SELECT * FROM weekly_plan_meals WHERE dateString = :date ORDER BY mealType")
    Observable<List<WeeklyPlanMealWithMeal>> getMealsByDate(String date);

    @Transaction
    @Query("SELECT * FROM weekly_plan_meals WHERE dateString = :date AND mealType = :mealType Order By addedAt DESC LIMIT 1")
    Maybe<WeeklyPlanMealWithMeal> getMealByDateAndType(String date, WeeklyPlanMealType mealType);

    @Query("DELETE FROM weekly_plan_meals WHERE mealId = :id")
    Completable deleteMealById(long id);

    @Query("DELETE FROM weekly_plan_meals WHERE dateString = :date AND mealType = :mealType")
    Completable deleteMealByDateAndType(String date, WeeklyPlanMealType mealType);

    @Query("DELETE FROM weekly_plan_meals")
    Completable clearAllPlannedMeals();

    @Query("SELECT COUNT(*) FROM weekly_plan_meals")
    Single<Integer> getPlannedMealsCount();

    @Query("SELECT DISTINCT dateString FROM weekly_plan_meals")
    Observable<List<String>> getAllPlannedDates();

    @Transaction
    @Query("SELECT * FROM weekly_plan_meals")
    Observable<List<WeeklyPlanMealWithMeal>> getAllPlannedMeals();
}