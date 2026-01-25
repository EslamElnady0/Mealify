package com.mealify.mealify.data.favs.datasource;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mealify.mealify.data.favs.model.fav.FavouriteEntity;
import com.mealify.mealify.data.favs.model.fav.FavouriteWithMeal;
import com.mealify.mealify.db.AppDatabase;
import com.mealify.mealify.db.AppDatabase_Impl;

import java.util.List;

public class FavouriteLocalDataSource {
    private final FavouriteDao favouriteDao;

    public FavouriteLocalDataSource(Context ctx) {
        this.favouriteDao = AppDatabase.getInstance(ctx).favouriteDao();
    }

    public void addToFavourites(String mealId) {
        FavouriteEntity entity =
                new FavouriteEntity(mealId, System.currentTimeMillis());
        favouriteDao.insert(entity);
    }

    public void removeFromFavourites(String mealId) {
        favouriteDao.deleteByMealId(mealId);
    }

    public boolean isFavourite(String mealId) {
        return favouriteDao.isFavourite(mealId);
    }

    public LiveData<List<FavouriteWithMeal>> getAllFavourites() {
        return favouriteDao.getAllFavouritesWithMeals();
    }
}
