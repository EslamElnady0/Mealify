package com.mealify.mealify.data.datasources.meals.local;

import android.content.Context;

import com.mealify.mealify.data.datasources.meals.local.dao.FavouriteDao;
import com.mealify.mealify.data.datasources.meals.local.dao.MealDao;
import com.mealify.mealify.data.datasources.meals.local.dao.WeeklyPlanDao;
import com.mealify.mealify.data.models.fav.FavouriteEntity;
import com.mealify.mealify.data.models.fav.FavouriteWithMeal;
import com.mealify.mealify.data.models.meal.MealEntity;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealEntity;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealType;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealWithMeal;
import com.mealify.mealify.db.AppDatabase;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class MealLocalDataSource {
    private final MealDao mealDao;
    private final FavouriteDao favouriteDao;
    private final WeeklyPlanDao weeklyPlanDao;

    public MealLocalDataSource(Context ctx) {
        AppDatabase db = AppDatabase.getInstance(ctx);
        this.mealDao = db.mealDao();
        this.favouriteDao = db.favouriteDao();
        this.weeklyPlanDao = db.weeklyPlanDao();
    }

    public Completable insertMeal(MealEntity meal) {
        return mealDao.insertMeal(meal);
    }

    public Completable insertMeals(List<MealEntity> meals) {
        return mealDao.insertMeals(meals);
    }

    public Single<MealEntity> getMealById(String mealId) {
        return mealDao.getMealById(mealId);
    }

    public Observable<List<MealEntity>> getAllMeals() {
        return mealDao.getAllMeals();
    }

    public Completable deleteMeal(MealEntity meal) {
        return mealDao.delete(meal);
    }

    public Completable deleteAllMeals() {
        return mealDao.deleteAllMeals();
    }

    public Completable addToFavourites(String mealId) {
        FavouriteEntity entity = new FavouriteEntity(mealId, System.currentTimeMillis());
        return favouriteDao.insert(entity);
    }

    public Completable insertFavourites(List<FavouriteEntity> favourites) {
        return favouriteDao.insertFavourites(favourites);
    }

    public Completable removeFromFavourites(String mealId) {
        return favouriteDao.deleteByMealId(mealId);
    }

    public Single<Boolean> isFavourite(String mealId) {
        return favouriteDao.isFavourite(mealId);
    }

    public Observable<List<FavouriteWithMeal>> getAllFavourites() {
        return favouriteDao.getAllFavouritesWithMeals();
    }

    public Single<Integer> getFavouritesCount() {
        return favouriteDao.getFavouritesCount();
    }

    public Completable deleteAllFavourites() {
        return favouriteDao.deleteAllFavourites();
    }

    public Completable addMealToWeeklyPlan(WeeklyPlanMealEntity mealEntity) {
        return weeklyPlanDao.addMealToPlan(mealEntity);
    }

    public Completable insertWeeklyPlan(List<WeeklyPlanMealEntity> plans) {
        return weeklyPlanDao.insertWeeklyPlan(plans);
    }

    public Observable<List<WeeklyPlanMealWithMeal>> getWeekMeals(String startDate, String endDate) {
        return weeklyPlanDao.getWeekMeals(startDate, endDate);
    }

    public Observable<List<WeeklyPlanMealWithMeal>> getMealsByDate(String date) {
        return weeklyPlanDao.getMealsByDate(date);
    }

    public Maybe<WeeklyPlanMealWithMeal> getMealByDateAndType(String date, WeeklyPlanMealType mealType) {
        return weeklyPlanDao.getMealByDateAndType(date, mealType);
    }

    public Completable deleteMealById(long id) {
        return weeklyPlanDao.deleteMealById(id);
    }

    public Completable deleteMealByDateAndType(String date, WeeklyPlanMealType mealType) {
        return weeklyPlanDao.deleteMealByDateAndType(date, mealType);
    }

    public Completable clearWeeklyPlan() {
        return weeklyPlanDao.clearAllPlannedMeals();
    }

    public Single<Integer> getPlannedMealsCount() {
        return weeklyPlanDao.getPlannedMealsCount();
    }

    public Observable<List<String>> getAllPlannedDates() {
        return weeklyPlanDao.getAllPlannedDates();
    }

    public Observable<List<WeeklyPlanMealWithMeal>> getAllPlannedMeals() {
        return weeklyPlanDao.getAllPlannedMeals();
    }
}