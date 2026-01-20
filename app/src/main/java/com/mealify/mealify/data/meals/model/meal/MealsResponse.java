package com.mealify.mealify.data.meals.model.meal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealsResponse {
    @SerializedName("meals")
    public List<MealDto> meals;

}
