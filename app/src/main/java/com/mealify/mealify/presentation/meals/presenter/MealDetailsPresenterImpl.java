package com.mealify.mealify.presentation.meals.presenter;

import android.content.Context;

import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.data.favs.repo.FavRepo;
import com.mealify.mealify.data.meals.model.meal.MealEntity;
import com.mealify.mealify.data.meals.repo.MealsRepo;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealType;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;
import com.mealify.mealify.data.weeklyplan.repo.WeeklyPlanRepo;
import com.mealify.mealify.presentation.meals.views.MealDetailsView;

import com.mealify.mealify.network.NetworkObservation;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MealDetailsPresenterImpl implements MealDetailsPresenter {
    private MealDetailsView view;
    private MealsRepo mealsRepo;
    private FavRepo favRepo;
    private WeeklyPlanRepo weeklyPlanRepo;
    private Context context;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public MealDetailsPresenterImpl(Context context, MealDetailsView view) {
        this.context = context;
        this.view = view;
        this.mealsRepo = new MealsRepo(context);
        this.favRepo = new FavRepo(context);
        this.weeklyPlanRepo = new WeeklyPlanRepo(context);
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

        favRepo.isMealFavorite(mealId, new GeneralResponse<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                view.onIsFavoriteResult(data);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void toggleFavorite(MealEntity meal) {

        favRepo.isMealFavorite(meal.getId(), new GeneralResponse<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                boolean isFav = data;
                if (isFav) {
                    favRepo.deleteMealFromFavorites(meal.getId());
                } else {
                    favRepo.insertMealInFavorites(meal);
                }
                view.onToggleFavoriteSuccess(!isFav);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void addToWeeklyPlan(WeeklyPlanMealWithMeal meal) {
        if (meal.planEntry.getMealType() == WeeklyPlanMealType.SNACK) {
            forceAddToWeeklyPlan(meal, null);
        } else {
            weeklyPlanRepo.getMealByDateAndType(
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
            weeklyPlanRepo.replaceMealInPlan(oldMealId, meal, callback);
        } else {
            weeklyPlanRepo.addMealToPlan(meal, callback);
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
