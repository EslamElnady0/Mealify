package com.mealify.mealify.data.models.weeklyplan;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.mealify.mealify.data.models.meal.MealEntity;

public class WeeklyPlanMealWithMeal {
    @Embedded
    public WeeklyPlanMealEntity planEntry;

    @Relation(
            parentColumn = "mealId",
            entityColumn = "id"
    )
    public MealEntity meal;

    public WeeklyPlanMealWithMeal(MealEntity meal, WeeklyPlanMealEntity planEntry) {
        this.meal = meal;
        this.planEntry = planEntry;
    }
}