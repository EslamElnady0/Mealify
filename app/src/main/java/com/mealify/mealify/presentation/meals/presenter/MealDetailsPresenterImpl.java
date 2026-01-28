package com.mealify.mealify.presentation.meals.presenter;

import android.content.Context;

import com.mealify.mealify.core.response.ApiResponse;
import com.mealify.mealify.data.favs.repo.FavRepo;
import com.mealify.mealify.data.meals.model.meal.MealEntity;
import com.mealify.mealify.data.meals.repo.MealsRepo;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;
import com.mealify.mealify.data.weeklyplan.repo.WeeklyPlanRepo;
import com.mealify.mealify.presentation.meals.views.MealDetailsView;

public class MealDetailsPresenterImpl implements MealDetailsPresenter {
    private MealDetailsView view;
    private MealsRepo mealsRepo;
    private FavRepo favRepo;
    private WeeklyPlanRepo weeklyPlanRepo;

    public MealDetailsPresenterImpl(Context context, MealDetailsView view) {
        this.view = view;
        this.mealsRepo = new MealsRepo(context);
        this.favRepo = new FavRepo(context);
        this.weeklyPlanRepo = new WeeklyPlanRepo(context);
    }

    @Override
    public void getMealDetails(String id) {
        view.toggleLoading(true);
        mealsRepo.getMealDetails(id, new ApiResponse<MealEntity>() {
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
        new Thread(() -> {
            boolean isFav = favRepo.isMealFavorite(mealId);
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                view.onIsFavoriteResult(isFav);
            });
        }).start();
    }

    @Override
    public void toggleFavorite(MealEntity meal) {
        new Thread(() -> {
            boolean isFav = favRepo.isMealFavorite(meal.getId());
            if (isFav) {
                favRepo.deleteMealFromFavorites(meal.getId());
            } else {
                mealsRepo.addMealToLocal(meal);
                favRepo.insertMealInFavorites(meal);
            }
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                view.onToggleFavoriteSuccess(!isFav);
            });
        }).start();
    }

    @Override
    public void addToWeeklyPlan(WeeklyPlanMealWithMeal meal) {
        new Thread(() -> {
            weeklyPlanRepo.addMealToPlan(meal);
            view.onWeeklyPlanMealAdded("Meal added to weekly plan");
        }).start();
    }
}
