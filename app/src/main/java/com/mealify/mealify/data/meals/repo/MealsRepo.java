package com.mealify.mealify.data.meals.repo;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.data.meals.datasources.local.MealLocalDataSource;
import com.mealify.mealify.data.meals.datasources.remote.MealRemoteDataSource;
import com.mealify.mealify.data.meals.mapper.MealMapper;
import com.mealify.mealify.data.meals.model.category.CategoryDto;
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
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import com.mealify.mealify.data.favs.datasource.local.FavouriteLocalDataSource;
import com.mealify.mealify.data.favs.model.fav.FavouriteWithMeal;
import com.mealify.mealify.data.weeklyplan.datasource.local.WeeklyPlanLocalDataSource;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;

public class MealsRepo {

    private final MealRemoteDataSource remoteDataSource;
    private final MealLocalDataSource localDataSource;
    private final FavouriteLocalDataSource favouriteLocalDataSource;
    private final WeeklyPlanLocalDataSource weeklyPlanLocalDataSource;

    public MealsRepo(Context ctx) {
        this.remoteDataSource = new MealRemoteDataSource(ctx);
        this.localDataSource = new MealLocalDataSource(ctx);
        this.favouriteLocalDataSource = new FavouriteLocalDataSource(ctx);
        this.weeklyPlanLocalDataSource = new WeeklyPlanLocalDataSource(ctx);
    }

    @SuppressLint("CheckResult")
    public void getRandomMeal(GeneralResponse<List<MealDto>> generalResponse) {
        remoteDataSource.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .map(response -> response.meals)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        generalResponse::onSuccess,
                        error -> generalResponse.onError(error.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    public void getCategories(GeneralResponse<List<CategoryDto>> generalResponse) {
        remoteDataSource.getCategories()
                .subscribeOn(Schedulers.io())
                .map(response -> response.categories)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        generalResponse::onSuccess,
                        error -> generalResponse.onError(error.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    public void getMealDetails(String mealId, GeneralResponse<MealEntity> generalResponse) {
        remoteDataSource.getMealDetails(mealId)
                .subscribeOn(Schedulers.io())
                .map(response -> MealMapper.toEntity(response.meals.get(0)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        generalResponse::onSuccess,
                        error -> generalResponse.onError(error.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    public void listIngredients(GeneralResponse<List<IngredientDto>> generalResponse) {
        remoteDataSource.listIngredients()
                .subscribeOn(Schedulers.io())
                .map(IngredientsResponse::getIngredients)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        generalResponse::onSuccess,
                        error -> generalResponse.onError(error.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    public void listAreas(GeneralResponse<List<CountryDto>> generalResponse) {
        remoteDataSource.listAreas()
                .subscribeOn(Schedulers.io())
                .map(CountriesResponse::getMeals)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        generalResponse::onSuccess,
                        error -> generalResponse.onError(error.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    public void getFilteredMeals(
            FilterType filterType,
            String query,
            GeneralResponse<List<FilteredMeal>> generalResponse
    ) {
        remoteDataSource.getFilteredMeals(filterType, query)
                .subscribeOn(Schedulers.io())
                .map(FilteredMealsResponse::getMeals)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        generalResponse::onSuccess,
                        error -> generalResponse.onError(error.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    public void searchMealsByName(
            String name,
            GeneralResponse<List<FilteredMeal>> generalResponse
    ) {
        remoteDataSource.searchMealsByName(name)
                .subscribeOn(Schedulers.io())
                .map(response -> MealMapper.toFilteredMeals(response.meals))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        generalResponse::onSuccess,
                        error -> generalResponse.onError(error.getMessage())
                );
    }

    public Observable<List<MealEntity>> getAllLocalMeals() {
        return localDataSource.getAllMeals();
    }

    public Observable<List<FavouriteWithMeal>> getAllLocalFavourites() {
        return favouriteLocalDataSource.getAllFavourites();
    }

    public Observable<List<WeeklyPlanMealWithMeal>> getAllLocalWeeklyPlans() {
        return weeklyPlanLocalDataSource.getAllPlannedMeals();
    }

    public Completable removeAllLocalMeals() {
        return localDataSource.deleteAllMeals();
    }

    public Completable removeAllLocalFavourites() {
        return favouriteLocalDataSource.deleteAllFavourites();
    }

    public Completable removeAllLocalWeeklyPlans() {
        return weeklyPlanLocalDataSource.clearWeeklyPlan();
    }
}
