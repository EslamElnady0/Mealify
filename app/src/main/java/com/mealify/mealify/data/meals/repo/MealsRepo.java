package com.mealify.mealify.data.meals.repo;

import android.content.Context;

import com.mealify.mealify.core.response.ApiResponse;
import com.mealify.mealify.data.meals.datasources.remote.MealRemoteDataSource;
import com.mealify.mealify.data.meals.model.category.CategoryDto;
import com.mealify.mealify.data.meals.model.category.CategoryStrDto;
import com.mealify.mealify.data.meals.model.country.CountryDto;
import com.mealify.mealify.data.meals.model.filteredmeals.FilterType;
import com.mealify.mealify.data.meals.model.filteredmeals.FilteredMeal;
import com.mealify.mealify.data.meals.model.ingredient.IngredientDto;
import com.mealify.mealify.data.meals.model.meal.MealDto;
import com.mealify.mealify.data.meals.model.meal.MealEntity;

import java.util.List;

public class MealsRepo {
    private MealRemoteDataSource remoteDataSource;

    public MealsRepo(Context ctx) {
        this.remoteDataSource = new MealRemoteDataSource(ctx);
    }

    public void getRandomMeal(ApiResponse<List<MealDto>> apiResponse) {
        remoteDataSource.getRandomMeal(apiResponse);
    }

    public void getCategories(ApiResponse<List<CategoryDto>> apiResponse) {
        remoteDataSource.getCategories(apiResponse);
    }

    public void getMealDetails(String mealId, ApiResponse<MealEntity> apiResponse) {
        remoteDataSource.getMealDetails(mealId, apiResponse);
    }

    public void listIngredients(ApiResponse<List<IngredientDto>> apiResponse) {
        remoteDataSource.listIngredients(apiResponse);
    }

    public void listCategories(ApiResponse<List<CategoryStrDto>> apiResponse) {
        remoteDataSource.listCategories(apiResponse);
    }

    public void listAreas(ApiResponse<List<CountryDto>> apiResponse) {
        remoteDataSource.listAreas(apiResponse);
    }

    public void getFilteredMeals(FilterType filterType, String query, ApiResponse<List<FilteredMeal>> apiResponse) {
        remoteDataSource.getFilteredMeals(filterType, query, apiResponse);
    }
}
