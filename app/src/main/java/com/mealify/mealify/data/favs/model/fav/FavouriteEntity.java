package com.mealify.mealify.data.favs.model.fav;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import com.mealify.mealify.data.meals.model.meal.MealEntity;

@Entity(
        tableName = "favourites",
        foreignKeys = @ForeignKey(
                entity = MealEntity.class,
                parentColumns = "id",
                childColumns = "mealId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("mealId")}
)
public class FavouriteEntity {
    @PrimaryKey
    @NonNull
    private String mealId;

    private long timestamp;
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