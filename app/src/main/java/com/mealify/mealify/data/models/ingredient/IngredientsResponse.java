package com.mealify.mealify.data.models.ingredient;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IngredientsResponse {

    @SerializedName("meals")  private List<IngredientDto> ingredients;

    public List<IngredientDto> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientDto> meals) {
        this.ingredients = meals;
    }
}
