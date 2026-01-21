package com.mealify.mealify.data.meals.model.meal;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class IngredientListConverter {
    private static final Gson gson = new Gson();

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
        Type listType = new TypeToken<List<Ingredient>>() {}.getType();
        return gson.fromJson(ingredientsString, listType);
    }
}