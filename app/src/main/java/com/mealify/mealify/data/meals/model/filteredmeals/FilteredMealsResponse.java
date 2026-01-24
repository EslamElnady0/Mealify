package com.mealify.mealify.data.meals.model.filteredmeals;

import java.util.List;

public class FilteredMealsResponse {

    private List<FilteredMeal> meals;

    public List<FilteredMeal> getMeals() {
        return meals;
    }

    public void setMeals(List<FilteredMeal> meals) {
        this.meals = meals;
    }
}