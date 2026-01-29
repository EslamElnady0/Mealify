package com.mealify.mealify.data.weeklyplan.repo;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.data.meals.datasources.local.MealLocalDataSource;
import com.mealify.mealify.data.weeklyplan.datasource.local.WeeklyPlanLocalDataSource;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealType;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WeeklyPlanRepo {
    private final WeeklyPlanLocalDataSource localDataSource;
    private final MealLocalDataSource mealLocalDataSource;

    public WeeklyPlanRepo(Context context) {
        this.localDataSource = new WeeklyPlanLocalDataSource(context);
        this.mealLocalDataSource = new MealLocalDataSource(context);
    }

    @SuppressLint("CheckResult")
    public void addMealToPlan(WeeklyPlanMealWithMeal planMealWithMeal, GeneralResponse<Boolean> generalResponse) {
        mealLocalDataSource.insertMeal(planMealWithMeal.meal)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        () -> {
                            localDataSource.addMealToWeeklyPlan(planMealWithMeal.planEntry)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            () -> {
                                                generalResponse.onSuccess(true);
                                            },
                                            error -> {
                                                generalResponse.onError(error.getMessage());
                                            }
                                    );

                        }

                );
    }

    public void deleteMealByDateAndType(String date, WeeklyPlanMealType mealType) {
        localDataSource.deleteMealByDateAndType(date, mealType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
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
    public void getWeekMeals(String startDate, String endDate, GeneralResponse<List<WeeklyPlanMealWithMeal>> generalResponse) {
        localDataSource.getWeekMeals(startDate, endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(generalResponse::onSuccess, error -> {
                    generalResponse.onError(error.getMessage());
                });
    }

    public void deleteMealFromPlan(long planId) {
        localDataSource.deleteMealById(planId)
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
}