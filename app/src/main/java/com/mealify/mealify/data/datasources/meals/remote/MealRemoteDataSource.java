package com.mealify.mealify.data.datasources.meals.remote;

import android.content.Context;

import com.mealify.mealify.data.datasources.meals.remote.service.CloudFirestoreService;
import com.mealify.mealify.data.datasources.meals.remote.service.MealService;
import com.mealify.mealify.data.models.category.CategoriesResponse;
import com.mealify.mealify.data.models.category.CategoriesStrResponse;
import com.mealify.mealify.data.models.country.CountriesResponse;
import com.mealify.mealify.data.models.fav.FavouriteEntity;
import com.mealify.mealify.data.models.filteredmeals.FilterType;
import com.mealify.mealify.data.models.filteredmeals.FilteredMealsResponse;
import com.mealify.mealify.data.models.ingredient.IngredientsResponse;
import com.mealify.mealify.data.models.meal.MealEntity;
import com.mealify.mealify.data.models.meal.MealsResponse;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealEntity;
import com.mealify.mealify.network.Network;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class MealRemoteDataSource {
    private static final String USERS_COLLECTION = "users";
    private static final String NESTED_MEALS_COLLECTION = "meals";
    private static final String NESTED_FAV_COLLECTION_NAME = "favourites";
    private static final String NESTED_WEEKLY_PLAN_COLLECTION = "weeklyPlan";

    private final MealService mealService;
    private final CloudFirestoreService cloudFirestoreService;

    public MealRemoteDataSource(Context ctx) {
        this.mealService = Network.getInstance(ctx).getHomeService();
        this.cloudFirestoreService = CloudFirestoreService.getInstance();
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

    public Completable saveMeal(String mealId, MealEntity meal) {
        return cloudFirestoreService.saveToNestedCollection(
                USERS_COLLECTION,
                NESTED_MEALS_COLLECTION,
                mealId,
                meal
        );
    }

    public Completable deleteMeal(String mealId) {
        return cloudFirestoreService.deleteFromNestedCollection(
                USERS_COLLECTION,
                NESTED_MEALS_COLLECTION,
                mealId
        );
    }

    public Single<List<MealEntity>> getMealsFromRemote() {
        return cloudFirestoreService.getFromNestedCollection(
                USERS_COLLECTION,
                NESTED_MEALS_COLLECTION,
                MealEntity.class
        );
    }

    public Completable saveToFavourites(String nestedId, Object data) {
        return cloudFirestoreService.saveToNestedCollection(
                USERS_COLLECTION,
                NESTED_FAV_COLLECTION_NAME,
                nestedId,
                data
        );
    }

    public Completable deleteFromFavourites(String nestedId) {
        return cloudFirestoreService.deleteFromNestedCollection(
                USERS_COLLECTION,
                NESTED_FAV_COLLECTION_NAME,
                nestedId
        );
    }

    public Single<List<FavouriteEntity>> getFavouritesFromRemote() {
        return cloudFirestoreService.getFromNestedCollection(
                USERS_COLLECTION,
                NESTED_FAV_COLLECTION_NAME,
                FavouriteEntity.class
        );
    }

    public Completable saveToWeeklyPlan(String nestedId, WeeklyPlanMealEntity data) {
        return cloudFirestoreService.saveToNestedCollection(
                USERS_COLLECTION,
                NESTED_WEEKLY_PLAN_COLLECTION,
                nestedId,
                data
        );
    }

    public Completable deleteFromWeeklyPlan(String nestedId) {
        return cloudFirestoreService.deleteFromNestedCollection(
                USERS_COLLECTION,
                NESTED_WEEKLY_PLAN_COLLECTION,
                nestedId
        );
    }

    public Single<List<WeeklyPlanMealEntity>> getWeeklyPlanFromRemote() {
        return cloudFirestoreService.getFromNestedCollection(
                USERS_COLLECTION,
                NESTED_WEEKLY_PLAN_COLLECTION,
                WeeklyPlanMealEntity.class
        );
    }
}
