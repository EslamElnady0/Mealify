package com.mealify.mealify.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.mealify.mealify.data.datasources.meals.local.dao.FavouriteDao;
import com.mealify.mealify.data.models.fav.FavouriteEntity;
import com.mealify.mealify.data.datasources.meals.local.dao.MealDao;
import com.mealify.mealify.data.models.meal.MealEntity;
import com.mealify.mealify.data.datasources.meals.local.dao.WeeklyPlanDao;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealEntity;

@Database(
        entities = {
                MealEntity.class,
                WeeklyPlanMealEntity.class,
                FavouriteEntity.class
        },
        version = 7
)
@TypeConverters({DBConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "mealify_db";

    private static AppDatabase db = null;

    public static AppDatabase getInstance(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                    .build();
        }
        return db;
    }

    public abstract MealDao mealDao();

    public abstract WeeklyPlanDao weeklyPlanDao();

    public abstract FavouriteDao favouriteDao();
}