package com.mealify.mealify.presentation.meals.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mealify.mealify.core.utils.NetworkObservation;
import com.mealify.mealify.data.models.meal.MealEntity;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealType;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealWithMeal;
import com.mealify.mealify.data.repos.meals.MealsRepo;
import com.mealify.mealify.presentation.meals.views.MealDetailsView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MealDetailsPresenterImpl implements MealDetailsPresenter {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MealDetailsView view;
    private final MealsRepo mealsRepo;
    private final Context context;

    public MealDetailsPresenterImpl(Context context, MealDetailsView view) {
        this.context = context;
        this.view = view;
        this.mealsRepo = new MealsRepo(context);
    }

    @Override
    @SuppressLint("CheckResult")
    public void getMealDetails(String id) {
        view.toggleLoading(true);
        mealsRepo.getMealDetails(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> {
                            view.toggleLoading(false);
                            view.onSuccess(data);
                        },
                        error -> {
                            view.toggleLoading(false);
                            view.onFailure(error.getMessage());
                        }
                );
    }

    @Override
    @SuppressLint("CheckResult")
    public void isMealFavorite(String mealId) {
        mealsRepo.isMealFavorite(mealId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> view.onIsFavoriteResult(data),
                        error -> {}
                );
    }

    @Override
    @SuppressLint("CheckResult")
    public void toggleFavorite(MealEntity meal) {
        mealsRepo.isMealFavorite(meal.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isFav -> {
                    if (isFav) {
                        mealsRepo.deleteMealFromFavorites(meal.getId())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> view.onToggleFavoriteSuccess(false));
                    } else {
                        mealsRepo.insertMealInFavorites(meal)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> view.onToggleFavoriteSuccess(true));
                    }
                }, error -> {});
    }

    @Override
    @SuppressLint("CheckResult")
    public void addToWeeklyPlan(WeeklyPlanMealWithMeal meal) {
        if (meal.planEntry.getMealType() == WeeklyPlanMealType.SNACK) {
            forceAddToWeeklyPlan(meal, null);
        } else {
            mealsRepo.getMealByDateAndType(meal.planEntry.getDateString(), meal.planEntry.getMealType())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            existingMeal -> view.showReplaceConfirmation(meal, existingMeal),
                            error -> view.onFailure(error.getMessage() + " fromGET"),
                            () -> forceAddToWeeklyPlan(meal, null)
                    );
        }
    }

    @Override
    @SuppressLint("CheckResult")
    public void forceAddToWeeklyPlan(WeeklyPlanMealWithMeal meal, String oldMealId) {
        if (oldMealId != null) {
            mealsRepo.replaceMealInPlan(oldMealId, meal)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> view.onWeeklyPlanMealAdded("Meal added to weekly plan"),
                            error -> view.onFailure(error.getMessage())
                    );
        } else {
            mealsRepo.addMealToPlan(meal)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> view.onWeeklyPlanMealAdded("Meal added to weekly plan"),
                            error -> view.onFailure(error.getMessage())
                    );
        }
    }

    @Override
    public void startNetworkMonitoring(String mealId) {
        disposables.add(
                NetworkObservation.getInstance(context)
                        .observeConnection()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isConnected -> {
                            view.toggleOffline(!isConnected);
                            if (isConnected) {
                                getMealDetails(mealId);
                                isMealFavorite(mealId);
                            }
                        })
        );
    }

    @Override
    public void onDestroy() {
        disposables.clear();
        view = null;
    }
}
