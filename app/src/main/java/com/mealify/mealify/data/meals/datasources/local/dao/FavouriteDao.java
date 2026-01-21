package com.mealify.mealify.data.meals.datasources.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Transaction;
import com.mealify.mealify.data.meals.model.fav.FavouriteEntity;
import com.mealify.mealify.data.meals.model.fav.FavouriteWithMeal;
import java.util.List;

@Dao
public interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavouriteEntity favourite);

    @Transaction
    @Query("SELECT * FROM favourites ORDER BY timestamp DESC")
    List<FavouriteWithMeal> getAllFavouritesWithMeals();

    @Query("SELECT EXISTS(SELECT 1 FROM favourites WHERE mealId = :mealId)")
    boolean isFavourite(String mealId);

    @Delete
    void delete(FavouriteEntity favourite);

    @Query("DELETE FROM favourites WHERE mealId = :mealId")
    void deleteByMealId(String mealId);
}