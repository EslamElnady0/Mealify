package com.mealify.mealify.data.favs.model.fav;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.mealify.mealify.data.meals.model.meal.MealEntity;

public class FavouriteWithMeal {
    @Embedded
    public FavouriteEntity favourite;

    @Relation(
            parentColumn = "mealId",
            entityColumn = "id"
    )
    public MealEntity meal;
}