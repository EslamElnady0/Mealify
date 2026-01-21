package com.mealify.mealify.data.meals.model.ingredient;

import java.util.List;

public class IngredientsResponse {

    private List<IngredientDto> meals;

    public List<IngredientDto> getIngredients() {
        return meals;
    }

    public void setIngredients(List<IngredientDto> meals) {
        this.meals = meals;
    }
}
