package com.mealify.mealify.data.models.meal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealsResponse {
    @SerializedName("meals")
    public List<MealDto> meals;

}
