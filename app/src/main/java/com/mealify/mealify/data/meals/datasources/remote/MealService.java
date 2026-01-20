package com.mealify.mealify.data.meals.datasources.remote;

import com.mealify.mealify.data.meals.model.category.CategoriesResponse;
import com.mealify.mealify.data.meals.model.meal.MealsResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MealService {
    @GET("random.php")
    public abstract Call<MealsResponse> getRandomMeal();

    @GET("categories.php")
    public abstract Call<CategoriesResponse> getCategories();
}
