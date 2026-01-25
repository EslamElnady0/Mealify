package com.mealify.mealify.data.favs.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mealify.mealify.data.favs.datasource.FavouriteLocalDataSource;
import com.mealify.mealify.data.favs.model.fav.FavouriteEntity;
import com.mealify.mealify.data.favs.model.fav.FavouriteWithMeal;
import com.mealify.mealify.data.meals.datasources.local.MealLocalDataSource;
import com.mealify.mealify.data.meals.model.meal.MealEntity;

import java.util.List;

public class FavRepo {
    private FavouriteLocalDataSource favoritesDataSource;
    private MealLocalDataSource mealLocalDataSource;
    public FavRepo(Context ctx) {
        this.favoritesDataSource = new FavouriteLocalDataSource(ctx);
        this.mealLocalDataSource = new MealLocalDataSource(ctx);
    }
    public void insertMealInFavorites(MealEntity meal) {
        mealLocalDataSource.insertMeal(meal);
        favoritesDataSource.addToFavourites(meal.getId());
    }

    public void deleteMealFromFavorites(String mealId) {
        favoritesDataSource.removeFromFavourites(mealId);
    }

    public boolean isMealFavorite(String mealId) {
        return favoritesDataSource.isFavourite(mealId);
    }

    public LiveData<List<FavouriteWithMeal>> getFavouriteMeals() {
        return favoritesDataSource.getAllFavourites();
    }

}
