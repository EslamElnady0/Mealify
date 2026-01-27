package com.mealify.mealify.data.weeklyplan.model.weeklyplan;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.mealify.mealify.data.meals.model.meal.MealEntity;

public class WeeklyPlanMealWithMeal {
    @Embedded
    public WeeklyPlanMealEntity planEntry;

    @Relation(
            parentColumn = "mealId",
            entityColumn = "id"
    )
    public MealEntity meal;
}