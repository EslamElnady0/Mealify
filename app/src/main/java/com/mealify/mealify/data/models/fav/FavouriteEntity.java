package com.mealify.mealify.data.models.fav;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.mealify.mealify.data.models.meal.MealEntity;

@Entity(
        tableName = "favourites",
        foreignKeys = @ForeignKey(
                entity = MealEntity.class,
                parentColumns = "id",
                childColumns = "mealId",
                onDelete = ForeignKey.NO_ACTION
        ),
        indices = {@Index("mealId")}
)
public class FavouriteEntity {
    @PrimaryKey
    @NonNull
    private String mealId;

    private long timestamp;

    public FavouriteEntity() {
    }

    public FavouriteEntity(@NonNull String mealId, long timestamp) {
        this.mealId = mealId;
        this.timestamp = timestamp;
    }

    @NonNull
    public String getMealId() {
        return mealId;
    }

    public void setMealId(@NonNull String mealId) {
        this.mealId = mealId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}