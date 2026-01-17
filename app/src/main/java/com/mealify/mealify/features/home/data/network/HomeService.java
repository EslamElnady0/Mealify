package com.mealify.mealify.features.home.data.network;

import com.mealify.mealify.features.home.data.model.category.CategoriesResponse;
import com.mealify.mealify.features.home.data.model.meal.MealsResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface HomeService {
    @GET("random.php")
    public abstract Call<MealsResponse> getRandomMeal();

    @GET("categories.php")
    public abstract Call<CategoriesResponse> getCategories();
}
