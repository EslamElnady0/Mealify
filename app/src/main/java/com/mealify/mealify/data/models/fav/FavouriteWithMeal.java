package com.mealify.mealify.data.models.fav;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.mealify.mealify.data.models.meal.MealEntity;

public class FavouriteWithMeal {
    @Embedded
    public FavouriteEntity favourite;

    @Relation(
            parentColumn = "mealId",
            entityColumn = "id"
    )
    public MealEntity meal;
}