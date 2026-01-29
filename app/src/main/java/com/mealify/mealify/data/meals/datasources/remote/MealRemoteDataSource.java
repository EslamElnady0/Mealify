package com.mealify.mealify.data.meals.datasources.remote;

import android.content.Context;

import com.mealify.mealify.data.meals.model.category.CategoriesResponse;
import com.mealify.mealify.data.meals.model.category.CategoriesStrResponse;
import com.mealify.mealify.data.meals.model.country.CountriesResponse;
import com.mealify.mealify.data.meals.model.filteredmeals.FilterType;
import com.mealify.mealify.data.meals.model.filteredmeals.FilteredMealsResponse;
import com.mealify.mealify.data.meals.model.ingredient.IngredientsResponse;
import com.mealify.mealify.data.meals.model.meal.MealsResponse;
import com.mealify.mealify.network.Network;

import io.reactivex.rxjava3.core.Single;

public class MealRemoteDataSource {
    private final Network network;
    private final MealService mealService;

    public MealRemoteDataSource(Context ctx) {
        network = Network.getInstance(ctx);
        mealService = network.getHomeService();
    }

    public Single<MealsResponse> getRandomMeal() {
        return mealService.getRandomMeal();
    }

    public Single<CategoriesResponse> getCategories() {
        return mealService.getCategories();
    }

    public Single<MealsResponse> getMealDetails(String mealId) {
        return mealService.getMealDetails(mealId);
    }

    public Single<IngredientsResponse> listIngredients() {
        return mealService.listIngredients();
    }

    public Single<CategoriesStrResponse> listCategories() {
        return mealService.listCategories();
    }

    public Single<CountriesResponse> listAreas() {
        return mealService.listAreas();
    }

    public Single<FilteredMealsResponse> getFilteredMeals(FilterType filterType, String query) {
        Single<FilteredMealsResponse> call;

        switch (filterType) {
            case AREA:
                call = mealService.filterMealsByArea(query);
                break;
            case CATEGORY:
                call = mealService.filterMealsByCategory(query);
                break;
            case INGREDIENT:
                call = mealService.filterMealsByIngredient(query);
                break;
            default:
                call = mealService.filterMealsByCategory(query);
        }
        return call;

    }

    public Single<MealsResponse> searchMealsByName(String name) {
        return mealService.searchMealsByName(name);
    }
}

