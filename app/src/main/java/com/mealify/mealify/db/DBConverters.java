package com.mealify.mealify.db;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mealify.mealify.data.models.meal.Ingredient;
import com.mealify.mealify.data.models.weeklyplan.DayOfWeek;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealType;

import java.lang.reflect.Type;
import java.util.List;

public class DBConverters {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static DayOfWeek fromString(String value) {
        return value == null ? null : DayOfWeek.valueOf(value);
    }

    @TypeConverter
    public static String dayOfWeekToString(DayOfWeek dayOfWeek) {
        return dayOfWeek == null ? null : dayOfWeek.name();
    }

    @TypeConverter
    public static WeeklyPlanMealType fromStringToMealType(String value) {
        return value == null ? null : WeeklyPlanMealType.valueOf(value);
    }

    @TypeConverter
    public static String mealTypeToString(WeeklyPlanMealType mealType) {
        return mealType == null ? null : mealType.name();
    }

    @TypeConverter
    public static String fromIngredientList(List<Ingredient> ingredients) {
        if (ingredients == null) {
            return null;
        }
        return gson.toJson(ingredients);
    }

    @TypeConverter
    public static List<Ingredient> toIngredientList(String ingredientsString) {
        if (ingredientsString == null) {
            return null;
        }
        Type listType = new TypeToken<List<Ingredient>>() {
        }.getType();
        return gson.fromJson(ingredientsString, listType);
    }
}
