package com.mealify.mealify.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.mealify.mealify.data.meals.datasources.local.dao.FavouriteDao;
import com.mealify.mealify.data.meals.datasources.local.dao.MealDao;
import com.mealify.mealify.data.meals.datasources.local.dao.WeeklyPlanDao;
import com.mealify.mealify.data.meals.model.meal.IngredientListConverter;
import com.mealify.mealify.data.meals.model.meal.MealEntity;
import com.mealify.mealify.data.meals.model.fav.FavouriteEntity;
import com.mealify.mealify.data.meals.model.weeklyplan.WeeklyPlanMealEntity;

@Database(
        entities = {
                MealEntity.class,
                WeeklyPlanMealEntity.class,
                FavouriteEntity.class
        },
        version = 1
)
@TypeConverters({IngredientListConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MealDao mealDao();
    public abstract WeeklyPlanDao weeklyPlanDao();
    public abstract FavouriteDao favouriteDao();
}