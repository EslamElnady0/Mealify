package com.mealify.mealify.data.meals.datasources.remote;

import com.mealify.mealify.data.meals.model.category.CategoriesResponse;
import com.mealify.mealify.data.meals.model.category.CategoriesStrResponse;
import com.mealify.mealify.data.meals.model.country.CountriesResponse;
import com.mealify.mealify.data.meals.model.filteredmeals.FilteredMealsResponse;
import com.mealify.mealify.data.meals.model.ingredient.IngredientsResponse;
import com.mealify.mealify.data.meals.model.meal.MealsResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealService {
    @GET("random.php")
    public abstract Single<MealsResponse> getRandomMeal();

    @GET("categories.php")
    public abstract Single<CategoriesResponse> getCategories();

    @GET("lookup.php")
    public abstract Single<MealsResponse> getMealDetails(@Query("i") String mealId);

    @GET("list.php?i=list")
    public abstract Single<IngredientsResponse> listIngredients();

    @GET("list.php?c=list")
    public abstract Single<CategoriesStrResponse> listCategories();

    @GET("list.php?a=list")
    public abstract Single<CountriesResponse> listAreas();

    @GET("filter.php")
    public abstract Single<FilteredMealsResponse> filterMealsByIngredient(@Query("i") String ingredient);

    @GET("filter.php")
    public abstract Single<FilteredMealsResponse> filterMealsByCategory(@Query("c") String category);

    @GET("filter.php")
    public abstract Single<FilteredMealsResponse> filterMealsByArea(@Query("a") String area);

    @GET("search.php")
    public abstract Single<MealsResponse> searchMealsByName(@Query("s") String name);

}
