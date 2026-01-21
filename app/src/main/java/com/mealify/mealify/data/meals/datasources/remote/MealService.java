package com.mealify.mealify.data.meals.datasources.remote;

import com.mealify.mealify.data.meals.model.category.CategoriesResponse;
import com.mealify.mealify.data.meals.model.category.CategoriesStrResponse;
import com.mealify.mealify.data.meals.model.country.CountriesResponse;
import com.mealify.mealify.data.meals.model.ingredient.IngredientsResponse;
import com.mealify.mealify.data.meals.model.meal.MealsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MealService {
    @GET("random.php")
    public abstract Call<MealsResponse> getRandomMeal();

    @GET("categories.php")
    public abstract Call<CategoriesResponse> getCategories();

    @GET("lookup.php")
    public abstract Call<CategoriesResponse> getMealDetails(@Query("i") String mealId);

    @GET("list.php?i=list")
    public abstract Call<IngredientsResponse> listIngredients();
    @GET("list.php?c=list")
    public abstract Call<CategoriesStrResponse> listCategories();
    @GET("list.php?a=list")
    public abstract Call<CountriesResponse> listAreas();


}
