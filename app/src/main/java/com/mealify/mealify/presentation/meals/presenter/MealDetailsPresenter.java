package com.mealify.mealify.presentation.meals.presenter;

import com.mealify.mealify.data.meals.model.meal.MealEntity;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;

public interface MealDetailsPresenter {
    void getMealDetails(String id);

    void isMealFavorite(String mealId);

    void toggleFavorite(MealEntity meal);

    void addToWeeklyPlan(WeeklyPlanMealWithMeal meal);

    void forceAddToWeeklyPlan(WeeklyPlanMealWithMeal meal, String oldMealId);
    
    void startNetworkMonitoring(String mealId);
    
    void onDestroy();
}
