package com.mealify.mealify.presentation.meals.presenter;

import android.content.Context;

import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.core.utils.NetworkObservation;
import com.mealify.mealify.data.models.meal.MealEntity;
import com.mealify.mealify.data.repos.meals.MealsRepo;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealType;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealWithMeal;
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
    public void getMealDetails(String id) {
        view.toggleLoading(true);
        mealsRepo.getMealDetails(id, new GeneralResponse<MealEntity>() {
            @Override
            public void onSuccess(MealEntity data) {
                view.toggleLoading(false);
                view.onSuccess(data);
            }

            @Override
            public void onError(String errorMessage) {
                view.toggleLoading(false);
                view.onFailure(errorMessage);
            }
        });
    }

    @Override
    public void isMealFavorite(String mealId) {
        mealsRepo.isMealFavorite(mealId, new GeneralResponse<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                view.onIsFavoriteResult(data);
            }

            @Override
            public void onError(String errorMessage) {
                // Ignore error for favorite check
            }
        });
    }

    @Override
    public void toggleFavorite(MealEntity meal) {
        mealsRepo.isMealFavorite(meal.getId(), new GeneralResponse<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                boolean isFav = data;
                if (isFav) {
                    mealsRepo.deleteMealFromFavorites(meal.getId());
                } else {
                    mealsRepo.insertMealInFavorites(meal);
                }
                view.onToggleFavoriteSuccess(!isFav);
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
            }
        });
    }

    @Override
    public void addToWeeklyPlan(WeeklyPlanMealWithMeal meal) {
        if (meal.planEntry.getMealType() == WeeklyPlanMealType.SNACK) {
            forceAddToWeeklyPlan(meal, null);
        } else {
            mealsRepo.getMealByDateAndType(
                    meal.planEntry.getDateString(),
                    meal.planEntry.getMealType(), new GeneralResponse<WeeklyPlanMealWithMeal>() {
                        @Override
                        public void onSuccess(WeeklyPlanMealWithMeal existingMeal) {
                            if (existingMeal != null) {
                                view.showReplaceConfirmation(meal, existingMeal);
                            } else {
                                forceAddToWeeklyPlan(meal, null);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            view.onFailure(errorMessage + " fromGET");
                        }
                    }
            );
        }
    }

    @Override
    public void forceAddToWeeklyPlan(WeeklyPlanMealWithMeal meal, String oldMealId) {
        GeneralResponse<Boolean> callback = new GeneralResponse<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                view.onWeeklyPlanMealAdded("Meal added to weekly plan");
            }

            @Override
            public void onError(String errorMessage) {
                view.onFailure(errorMessage);
            }
        };

        if (oldMealId != null) {
            mealsRepo.replaceMealInPlan(oldMealId, meal, callback);
        } else {
            mealsRepo.addMealToPlan(meal, callback);
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
