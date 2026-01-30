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
                onDelete = ForeignKey.NO_ACTION
        ),
        indices = {@Index("mealId"),
                @Index(value = {"dateString", "mealType"})}
)
public class WeeklyPlanMealEntity {
    @PrimaryKey(autoGenerate = true)
    private long planId;

    @NonNull
    private String mealId;

    @NonNull
    private String dateString;

    @NonNull
    private DayOfWeek dayOfWeek;

    @NonNull
    private WeeklyPlanMealType mealType;

    private long addedAt;

    public WeeklyPlanMealEntity() {
    }

    public WeeklyPlanMealEntity(@NonNull String mealId, @NonNull String dateString,
                                @NonNull DayOfWeek dayOfWeek, @NonNull WeeklyPlanMealType mealType, long addedAt) {
        this.mealId = mealId;
        this.dateString = dateString;
        this.dayOfWeek = dayOfWeek;
        this.mealType = mealType;
        this.addedAt = addedAt;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
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
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(@NonNull DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @NonNull
    public WeeklyPlanMealType getMealType() {
        return mealType;
    }

    public void setMealType(@NonNull WeeklyPlanMealType mealType) {
        this.mealType = mealType;
    }

    public long getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(long addedAt) {
        this.addedAt = addedAt;
    }
}
