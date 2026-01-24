package com.mealify.mealify.presentation.search.views.ingredient;

import com.mealify.mealify.data.meals.model.ingredient.IngredientDto;

import java.util.List;

public interface SearchIngredientView {
    void showIngredients(List<IngredientDto> ingredients);

    void showError(String message);

    void toggleLoading(boolean isLoading);
}
