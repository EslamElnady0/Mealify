package com.mealify.mealify.data.favs.datasource.local;

import android.content.Context;

import com.mealify.mealify.data.favs.model.fav.FavouriteEntity;
import com.mealify.mealify.data.favs.model.fav.FavouriteWithMeal;
import com.mealify.mealify.db.AppDatabase;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class FavouriteLocalDataSource {
    private final FavouriteDao favouriteDao;

    public FavouriteLocalDataSource(Context ctx) {
        this.favouriteDao = AppDatabase.getInstance(ctx).favouriteDao();
    }

    public Completable addToFavourites(String mealId) {
        FavouriteEntity entity = new FavouriteEntity(mealId, System.currentTimeMillis());
        return favouriteDao.insert(entity);
    }

    public Completable removeFromFavourites(String mealId) {
        return favouriteDao.deleteByMealId(mealId);
    }

    public Single<Boolean> isFavourite(String mealId) {
        return favouriteDao.isFavourite(mealId);
    }

    public Observable<List<FavouriteWithMeal>> getAllFavourites() {
        return favouriteDao.getAllFavouritesWithMeals();
    }

    public Single<Integer> getFavouritesCount() {
        return favouriteDao.getFavouritesCount();
    }

    public Completable deleteAllFavourites() {
        return favouriteDao.deleteAllFavourites();
    }
}

