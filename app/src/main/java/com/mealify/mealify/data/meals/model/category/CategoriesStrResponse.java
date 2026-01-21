package com.mealify.mealify.data.meals.model.category;

import java.util.List;

public class CategoriesStrResponse {
    private List<CategoryStrDto> meals;



    public CategoriesStrResponse(List<CategoryStrDto> meals) {
        this.meals = meals;
    }

    public List<CategoryDto> getMeals() {
        return meals;
    }

    public void setMeals(List<CategoryStrDto> meals) {
        this.meals = meals;
    }
}
