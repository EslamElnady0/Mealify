package com.mealify.mealify.data.meals.datasources.remote;

import com.mealify.mealify.data.favs.datasource.remote.CloudFirestoreService;
import com.mealify.mealify.data.meals.model.meal.MealEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class MealFirestoreRemoteDataSource {

    private static final String USERS_COLLECTION = "users";
    private static final String NESTED_MEALS_COLLECTION = "meals";

    private final CloudFirestoreService cloudFirestoreService;

    public MealFirestoreRemoteDataSource() {
        cloudFirestoreService = CloudFirestoreService.getInstance();
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
}
