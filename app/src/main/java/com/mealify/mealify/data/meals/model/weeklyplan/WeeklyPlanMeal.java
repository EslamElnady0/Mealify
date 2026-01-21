package com.mealify.mealify.data.meals.model.weeklyplan;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.mealify.mealify.data.meals.model.meal.MealEntity;

public class WeeklyPlanMeal {
    @Embedded
    public WeeklyPlanMealEntity planEntry;

    @Relation(
            parentColumn = "mealId",
            entityColumn = "id"
    )
    public MealEntity meal;
}