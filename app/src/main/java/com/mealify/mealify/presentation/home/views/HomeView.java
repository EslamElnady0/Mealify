
package com.mealify.mealify.presentation.home.views;

import com.mealify.mealify.data.models.category.CategoryDto;
import com.mealify.mealify.data.models.meal.MealDto;

import java.util.List;

public interface HomeView {
    void showMealOfTheDay(MealDto meal);
    void showCategories(List<CategoryDto> categories);
    void showError(String message);
    void toggleMealOfTheDayLoading(boolean isLoading);
    void toggleCategoriesLoading(boolean isLoading);
}
