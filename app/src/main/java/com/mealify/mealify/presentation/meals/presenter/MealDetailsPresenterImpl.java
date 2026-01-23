package com.mealify.mealify.presentation.meals.presenter;

import android.content.Context;

import com.mealify.mealify.core.response.ApiResponse;
import com.mealify.mealify.data.meals.model.meal.MealEntity;
import com.mealify.mealify.data.meals.repo.MealsRepo;
import com.mealify.mealify.presentation.meals.views.MealDetailsView;

public class MealDetailsPresenterImpl implements MealDetailsPresenter{
    private MealDetailsView view;
    private MealsRepo mealsRepo;

    public MealDetailsPresenterImpl(Context context, MealDetailsView view){
        this.view = view;
        this.mealsRepo = new MealsRepo(context);
    }

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
}
