package com.mealify.mealify.data.favs.repo;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.data.favs.datasource.local.FavouriteLocalDataSource;
import com.mealify.mealify.data.favs.datasource.remote.FavouriteRemoteDataSource;
import com.mealify.mealify.data.favs.model.fav.FavouriteWithMeal;
import com.mealify.mealify.data.meals.datasources.local.MealLocalDataSource;
import com.mealify.mealify.data.meals.datasources.remote.MealFirestoreRemoteDataSource;
import com.mealify.mealify.data.meals.model.meal.MealEntity;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavRepo {
    private FavouriteLocalDataSource favoritesDataSource;
    private FavouriteRemoteDataSource favRemoteDataSource;

    private MealFirestoreRemoteDataSource mealFirestoreRemoteDataSource;
    private MealLocalDataSource mealLocalDataSource;

    public FavRepo(Context ctx) {
        this.favoritesDataSource = new FavouriteLocalDataSource(ctx);
        this.mealLocalDataSource = new MealLocalDataSource(ctx);
        this.favRemoteDataSource = new FavouriteRemoteDataSource();
        this.mealFirestoreRemoteDataSource = new MealFirestoreRemoteDataSource();
    }

    @SuppressLint("CheckResult")
    public void insertMealInFavorites(MealEntity meal) {
        mealLocalDataSource.insertMeal(meal)
                .andThen(
                        mealFirestoreRemoteDataSource.saveMeal(
                                meal.getId(),
                                meal
                        )
                )
                .andThen(
                        favoritesDataSource.addToFavourites(
                                meal.getId()
                        )
                )
                .andThen(
                        favRemoteDataSource.saveToFavourites(
                                meal.getId(),
                                meal
                        )
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


    @SuppressLint("CheckResult")
    public void deleteMealFromFavorites(String mealId) {
        favoritesDataSource.removeFromFavourites(mealId)
                .andThen(favRemoteDataSource.deleteFromFavourites(mealId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


    @SuppressLint("CheckResult")
    public void isMealFavorite(String mealId, GeneralResponse<Boolean> generalResponse) {
        favoritesDataSource.isFavourite(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(generalResponse::onSuccess, error -> {
                    generalResponse.onError(error.getMessage());
                });
    }

    @SuppressLint("CheckResult")
    public void getFavouriteMeals(GeneralResponse<List<FavouriteWithMeal>> generalResponse) {
        favoritesDataSource.getAllFavourites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(generalResponse::onSuccess, error -> {
                    generalResponse.onError(error.getMessage());
                });
    }

}
