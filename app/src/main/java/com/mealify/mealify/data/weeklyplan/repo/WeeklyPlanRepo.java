package com.mealify.mealify.data.weeklyplan.repo;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.data.meals.datasources.local.MealLocalDataSource;
import com.mealify.mealify.data.meals.datasources.remote.MealFirestoreRemoteDataSource;
import com.mealify.mealify.data.weeklyplan.datasource.local.WeeklyPlanLocalDataSource;
import com.mealify.mealify.data.weeklyplan.datasource.remote.WeeklyPlanRemoteDataSource;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealType;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WeeklyPlanRepo {
    private final WeeklyPlanLocalDataSource localDataSource;
    private final MealLocalDataSource mealLocalDataSource;
    private final WeeklyPlanRemoteDataSource weeklyPlanRemoteDataSource;
    private final MealFirestoreRemoteDataSource mealFirestoreRemoteDataSource;


    public WeeklyPlanRepo(Context context) {
        this.localDataSource = new WeeklyPlanLocalDataSource(context);
        this.mealLocalDataSource = new MealLocalDataSource(context);
        this.weeklyPlanRemoteDataSource = new WeeklyPlanRemoteDataSource();
        this.mealFirestoreRemoteDataSource = new MealFirestoreRemoteDataSource();
    }

    @SuppressLint("CheckResult")
    public void addMealToPlan(
            WeeklyPlanMealWithMeal planMealWithMeal,
            GeneralResponse<Boolean> generalResponse
    ) {
        addMealToPlanCompletable(planMealWithMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> generalResponse.onSuccess(true),
                        error -> generalResponse.onError(error.getMessage())
                );
    }

    private Completable addMealToPlanCompletable(WeeklyPlanMealWithMeal planMealWithMeal) {
        return mealLocalDataSource.insertMeal(planMealWithMeal.meal)
                .andThen(
                        mealFirestoreRemoteDataSource.saveMeal(
                                planMealWithMeal.meal.getId(),
                                planMealWithMeal.meal
                        )
                )
                .andThen(
                        localDataSource.addMealToWeeklyPlan(
                                planMealWithMeal.planEntry
                        )
                )
                .andThen(
                        weeklyPlanRemoteDataSource.saveToWeeklyPlan(
                                planMealWithMeal.planEntry.getMealId(),
                                planMealWithMeal.planEntry
                        )
                );
    }

    @SuppressLint("CheckResult")
    public void replaceMealInPlan(
            String oldMealId,
            WeeklyPlanMealWithMeal newMeal,
            GeneralResponse<Boolean> generalResponse
    ) {
        localDataSource.deleteMealByDateAndType(newMeal.planEntry.getDateString(), newMeal.planEntry.getMealType())
                .andThen(weeklyPlanRemoteDataSource.deleteFromWeeklyPlan(oldMealId))
                .andThen(addMealToPlanCompletable(newMeal))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> generalResponse.onSuccess(true),
                        error -> generalResponse.onError(error.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    public void getMealsByDate(String date, GeneralResponse<List<WeeklyPlanMealWithMeal>> generalResponse) {
        localDataSource.getMealsByDate(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        generalResponse::onSuccess,
                        error -> {
                            generalResponse.onError(error.getMessage());
                        }
                );
    }

    @SuppressLint("CheckResult")
    public void getMealByDateAndType(String date, WeeklyPlanMealType type, GeneralResponse<WeeklyPlanMealWithMeal> generalResponse) {
        localDataSource.getMealByDateAndType(date, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        generalResponse::onSuccess,
                        error -> generalResponse.onError(error.getMessage()),
                        () -> generalResponse.onSuccess(null)
                );
    }

    @SuppressLint("CheckResult")
    public void deleteMealFromPlan(long planId) {
        localDataSource.deleteMealById(planId)
                .andThen(
                        weeklyPlanRemoteDataSource.deleteFromWeeklyPlan(
                                String.valueOf(planId)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void clearAllPlannedMeals() {
        localDataSource.clearWeeklyPlan()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @SuppressLint("CheckResult")
    public void getAllPlannedDates(GeneralResponse<List<String>> generalResponse) {
        localDataSource.getAllPlannedDates().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(generalResponse::onSuccess, error -> {
                    generalResponse.onError(error.getMessage());
                });

    }

    @SuppressLint("CheckResult")
    public void getPlannedMealsCount(GeneralResponse<Integer> generalResponse) {
        localDataSource.getPlannedMealsCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(generalResponse::onSuccess, error -> {
                    generalResponse.onError(error.getMessage());
                });
    }
}