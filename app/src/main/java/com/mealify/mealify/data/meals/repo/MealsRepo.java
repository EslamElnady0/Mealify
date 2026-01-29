package com.mealify.mealify.data.meals.repo;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mealify.mealify.core.response.ApiResponse;
import com.mealify.mealify.data.meals.datasources.local.MealLocalDataSource;
import com.mealify.mealify.data.meals.datasources.remote.MealRemoteDataSource;
import com.mealify.mealify.data.meals.mapper.MealMapper;
import com.mealify.mealify.data.meals.model.category.CategoryDto;
import com.mealify.mealify.data.meals.model.category.CategoryStrDto;
import com.mealify.mealify.data.meals.model.country.CountriesResponse;
import com.mealify.mealify.data.meals.model.country.CountryDto;
import com.mealify.mealify.data.meals.model.filteredmeals.FilterType;
import com.mealify.mealify.data.meals.model.filteredmeals.FilteredMeal;
import com.mealify.mealify.data.meals.model.filteredmeals.FilteredMealsResponse;
import com.mealify.mealify.data.meals.model.ingredient.IngredientDto;
import com.mealify.mealify.data.meals.model.ingredient.IngredientsResponse;
import com.mealify.mealify.data.meals.model.meal.MealDto;
import com.mealify.mealify.data.meals.model.meal.MealEntity;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsRepo {
    private MealRemoteDataSource remoteDataSource;
    private MealLocalDataSource localDataSource;

    public MealsRepo(Context ctx) {
        this.remoteDataSource = new MealRemoteDataSource(ctx);
        this.localDataSource = new MealLocalDataSource(ctx);
    }

    @SuppressLint("CheckResult")
    public void getRandomMeal(ApiResponse<List<MealDto>> apiResponse) {
        remoteDataSource.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .map(response -> response.meals)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        apiResponse::onSuccess,
                        error -> {
                            apiResponse.onError(error.getMessage());
                        }
                );
    }

    @SuppressLint("CheckResult")
    public void getCategories(ApiResponse<List<CategoryDto>> apiResponse) {
        remoteDataSource.getCategories()
                .subscribeOn(Schedulers.io())
                .map(response -> response.categories)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        apiResponse::onSuccess,
                        error -> {
                            apiResponse.onError(error.getMessage());
                        }
                );
    }

    @SuppressLint("CheckResult")
    public void getMealDetails(String mealId, ApiResponse<MealEntity> apiResponse) {
        remoteDataSource.getMealDetails(mealId)
                .subscribeOn(Schedulers.io())
                .map(response ->
                        MealMapper.toEntity(response.meals.get(0))

                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        apiResponse::onSuccess,
                        error -> {
                            apiResponse.onError(error.getMessage());
                        }
                );
    }

    @SuppressLint("CheckResult")
    public void listIngredients(ApiResponse<List<IngredientDto>> apiResponse) {
        remoteDataSource.listIngredients()
                .subscribeOn(Schedulers.io())
                .map(IngredientsResponse::getIngredients)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        apiResponse::onSuccess,
                        error -> {
                            apiResponse.onError(error.getMessage());
                        }
                );

    }

    public void listCategories(ApiResponse<List<CategoryStrDto>> apiResponse) {
        remoteDataSource.listCategories()
        ;
    }

    @SuppressLint("CheckResult")
    public void listAreas(ApiResponse<List<CountryDto>> apiResponse) {
        remoteDataSource.listAreas()
                .subscribeOn(Schedulers.io())
                .map(CountriesResponse::getMeals)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        apiResponse::onSuccess,
                        error -> {
                            apiResponse.onError(error.getMessage());
                        }
                );
    }

    @SuppressLint("CheckResult")
    public void getFilteredMeals(FilterType filterType, String query, ApiResponse<List<FilteredMeal>> apiResponse) {
        remoteDataSource.getFilteredMeals(filterType, query)
                .subscribeOn(Schedulers.io())
                .map(FilteredMealsResponse::getMeals)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        apiResponse::onSuccess,
                        error -> {
                            apiResponse.onError(error.getMessage());
                        }
                );
        ;
    }

    @SuppressLint("CheckResult")
    public void searchMealsByName(String name, ApiResponse<List<FilteredMeal>> apiResponse) {
        remoteDataSource.searchMealsByName(name)
                .subscribeOn(Schedulers.io())
                .map(response -> MealMapper.toFilteredMeals(response.meals))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        apiResponse::onSuccess,
                        error -> {
                            apiResponse.onError(error.getMessage());
                        }
                );
        ;
    }

    public void addMealToLocal(MealEntity meal) {
        localDataSource.insertMeal(meal);
    }
}
