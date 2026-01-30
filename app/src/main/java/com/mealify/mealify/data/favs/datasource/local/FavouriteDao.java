package com.mealify.mealify.data.favs.datasource.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.mealify.mealify.data.favs.model.fav.FavouriteEntity;
import com.mealify.mealify.data.favs.model.fav.FavouriteWithMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(FavouriteEntity favourite);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertFavourites(List<FavouriteEntity> favourites);

    @Transaction
    @Query("SELECT * FROM favourites ORDER BY timestamp DESC")
    Observable<List<FavouriteWithMeal>> getAllFavouritesWithMeals();

    @Query("SELECT EXISTS(SELECT 1 FROM favourites WHERE mealId = :mealId)")
    Single<Boolean> isFavourite(String mealId);

    @Delete
    Completable delete(FavouriteEntity favourite);

    @Query("DELETE FROM favourites WHERE mealId = :mealId")
    Completable deleteByMealId(String mealId);

    @Query("SELECT COUNT(*) FROM favourites")
    Single<Integer> getFavouritesCount();

    @Query("DELETE FROM favourites")
    Completable deleteAllFavourites();
}