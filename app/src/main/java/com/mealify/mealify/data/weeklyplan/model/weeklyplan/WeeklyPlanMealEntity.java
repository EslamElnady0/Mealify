package com.mealify.mealify.data.weeklyplan.model.weeklyplan;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.mealify.mealify.data.meals.model.meal.MealEntity;

@Entity(
        tableName = "weekly_plan_meals",
        foreignKeys = @ForeignKey(
                entity = MealEntity.class,
                parentColumns = "id",
                childColumns = "mealId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("mealId"), @Index(value = {"dateString", "mealType"}, unique = true)}
)
public class WeeklyPlanMealEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String mealId;

    @NonNull
    private String dateString;

    @NonNull
    private String dayOfWeek;

    @NonNull
    private String mealType;

    private long addedAt;

    public WeeklyPlanMealEntity(@NonNull String mealId, @NonNull String dateString,
                                @NonNull String dayOfWeek, @NonNull String mealType, long addedAt) {
        this.mealId = mealId;
        this.dateString = dateString;
        this.dayOfWeek = dayOfWeek;
        this.mealType = mealType;
        this.addedAt = addedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getMealId() {
        return mealId;
    }

    public void setMealId(@NonNull String mealId) {
        this.mealId = mealId;
    }

    @NonNull
    public String getDateString() {
        return dateString;
    }

    public void setDateString(@NonNull String dateString) {
        this.dateString = dateString;
    }

    @NonNull
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(@NonNull String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @NonNull
    public String getMealType() {
        return mealType;
    }

    public void setMealType(@NonNull String mealType) {
        this.mealType = mealType;
    }

    public long getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(long addedAt) {
        this.addedAt = addedAt;
    }
}
