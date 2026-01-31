package com.mealify.mealify.data.repos.meals;

import android.content.Context;

import com.mealify.mealify.data.datasources.meals.local.MealLocalDataSource;
import com.mealify.mealify.data.datasources.meals.remote.MealRemoteDataSource;
import com.mealify.mealify.data.models.category.CategoryDto;
import com.mealify.mealify.data.models.country.CountriesResponse;
import com.mealify.mealify.data.models.country.CountryDto;
import com.mealify.mealify.data.models.fav.FavouriteEntity;
import com.mealify.mealify.data.models.fav.FavouriteWithMeal;
import com.mealify.mealify.data.models.filteredmeals.FilterType;
import com.mealify.mealify.data.models.filteredmeals.FilteredMeal;
import com.mealify.mealify.data.models.filteredmeals.FilteredMealsResponse;
import com.mealify.mealify.data.models.ingredient.IngredientDto;
import com.mealify.mealify.data.models.ingredient.IngredientsResponse;
import com.mealify.mealify.data.models.meal.MealDto;
import com.mealify.mealify.data.models.meal.MealEntity;
import com.mealify.mealify.data.models.meal.MealMapper;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealEntity;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealType;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealWithMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsRepo {

    private final MealRemoteDataSource remoteDataSource;
    private final MealLocalDataSource localDataSource;

    public MealsRepo(Context ctx) {
        this.remoteDataSource = new MealRemoteDataSource(ctx);
        this.localDataSource = new MealLocalDataSource(ctx);
    }

    // --- General Meal Methods ---

    public Single<List<MealDto>> getRandomMeal() {
        return remoteDataSource.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .map(response -> response.meals);
    }

    public Single<List<CategoryDto>> getCategories() {
        return remoteDataSource.getCategories()
                .subscribeOn(Schedulers.io())
                .map(response -> response.categories);
    }

    public Single<MealEntity> getMealDetails(String mealId) {
        return remoteDataSource.getMealDetails(mealId)
                .subscribeOn(Schedulers.io())
                .map(response -> MealMapper.toEntity(response.meals.get(0)));
    }

    public Single<List<IngredientDto>> listIngredients() {
        return remoteDataSource.listIngredients()
                .subscribeOn(Schedulers.io())
                .map(IngredientsResponse::getIngredients);
    }

    public Single<List<CountryDto>> listAreas() {
        return remoteDataSource.listAreas()
                .subscribeOn(Schedulers.io())
                .map(CountriesResponse::getMeals);
    }

    public Single<List<FilteredMeal>> getFilteredMeals(FilterType filterType, String query) {
        return remoteDataSource.getFilteredMeals(filterType, query)
                .subscribeOn(Schedulers.io())
                .map(FilteredMealsResponse::getMeals);
    }

    public Single<List<FilteredMeal>> searchMealsByName(String name) {
        return remoteDataSource.searchMealsByName(name)
                .subscribeOn(Schedulers.io())
                .map(response -> MealMapper.toFilteredMeals(response.meals));
    }

    // --- Favorites Methods ---

    public Completable insertMealInFavorites(MealEntity meal) {
        return localDataSource.insertMeal(meal)
                .andThen(remoteDataSource.saveMeal(meal.getId(), meal))
                .andThen(localDataSource.addToFavourites(meal.getId()))
                .andThen(remoteDataSource.saveToFavourites(meal.getId(), meal))
                .subscribeOn(Schedulers.io());
    }

    public Completable deleteMealFromFavorites(String mealId) {
        return localDataSource.removeFromFavourites(mealId)
                .andThen(remoteDataSource.deleteFromFavourites(mealId))
                .subscribeOn(Schedulers.io());
    }

    public Single<Boolean> isMealFavorite(String mealId) {
        return localDataSource.isFavourite(mealId)
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<FavouriteWithMeal>> getFavouriteMeals() {
        return localDataSource.getAllFavourites()
                .subscribeOn(Schedulers.io());
    }

    public Single<Integer> getFavouritesCount() {
        return localDataSource.getFavouritesCount()
                .subscribeOn(Schedulers.io());
    }

    // --- Weekly Plan Methods ---

    public Completable addMealToPlan(WeeklyPlanMealWithMeal planMealWithMeal) {
        return addMealToPlanCompletable(planMealWithMeal)
                .subscribeOn(Schedulers.io());
    }

    private Completable addMealToPlanCompletable(WeeklyPlanMealWithMeal planMealWithMeal) {
        return localDataSource.insertMeal(planMealWithMeal.meal)
                .andThen(remoteDataSource.saveMeal(planMealWithMeal.meal.getId(), planMealWithMeal.meal))
                .andThen(localDataSource.addMealToWeeklyPlan(planMealWithMeal.planEntry))
                .andThen(remoteDataSource.saveToWeeklyPlan(planMealWithMeal.planEntry.getMealId(), planMealWithMeal.planEntry));
    }

    public Completable replaceMealInPlan(String oldMealId, WeeklyPlanMealWithMeal newMeal) {
        return localDataSource.deleteMealByDateAndType(newMeal.planEntry.getDateString(), newMeal.planEntry.getMealType())
                .andThen(remoteDataSource.deleteFromWeeklyPlan(oldMealId))
                .andThen(addMealToPlanCompletable(newMeal))
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<WeeklyPlanMealWithMeal>> getMealsByDate(String date) {
        return localDataSource.getMealsByDate(date)
                .subscribeOn(Schedulers.io());
    }

    public Maybe<WeeklyPlanMealWithMeal> getMealByDateAndType(String date, WeeklyPlanMealType type) {
        return localDataSource.getMealByDateAndType(date, type)
                .subscribeOn(Schedulers.io());
    }

    public Completable deleteMealFromPlan(long planId) {
        return localDataSource.deleteMealById(planId)
                .andThen(remoteDataSource.deleteFromWeeklyPlan(String.valueOf(planId)))
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<String>> getAllPlannedDates() {
        return localDataSource.getAllPlannedDates()
                .subscribeOn(Schedulers.io());
    }

    public Single<Integer> getPlannedMealsCount() {
        return localDataSource.getPlannedMealsCount()
                .subscribeOn(Schedulers.io());
    }

    // --- Local Storage Management ---

    public Observable<List<MealEntity>> getAllLocalMeals() {
        return localDataSource.getAllMeals().subscribeOn(Schedulers.io());
    }

    public Observable<List<FavouriteWithMeal>> getAllLocalFavourites() {
        return localDataSource.getAllFavourites().subscribeOn(Schedulers.io());
    }

    public Observable<List<WeeklyPlanMealWithMeal>> getAllLocalWeeklyPlans() {
        return localDataSource.getAllPlannedMeals().subscribeOn(Schedulers.io());
    }

    public Completable removeAllLocalMeals() {
        return localDataSource.deleteAllMeals().subscribeOn(Schedulers.io());
    }

    public Completable removeAllLocalFavourites() {
        return localDataSource.deleteAllFavourites().subscribeOn(Schedulers.io());
    }

    public Completable removeAllLocalWeeklyPlans() {
        return localDataSource.clearWeeklyPlan().subscribeOn(Schedulers.io());
    }

    public Completable uploadMealsToFirebase(List<MealEntity> meals) {
        if (meals == null || meals.isEmpty()) return Completable.complete();
        return Observable.fromIterable(meals)
                .flatMapCompletable(meal -> remoteDataSource.saveMeal(meal.getId(), meal))
                .subscribeOn(Schedulers.io());
    }

    public Completable uploadFavouritesToFirebase(List<FavouriteEntity> favourites) {
        if (favourites == null || favourites.isEmpty()) return Completable.complete();
        return Observable.fromIterable(favourites)
                .flatMapCompletable(fav -> remoteDataSource.saveToFavourites(fav.getMealId(), fav))
                .subscribeOn(Schedulers.io());
    }

    public Completable uploadWeeklyPlanToFirebase(List<WeeklyPlanMealEntity> plans) {
        if (plans == null || plans.isEmpty()) return Completable.complete();
        return Observable.fromIterable(plans)
                .flatMapCompletable(plan -> remoteDataSource.saveToWeeklyPlan(String.valueOf(plan.getMealId()), plan))
                .subscribeOn(Schedulers.io());
    }

    public Single<List<MealEntity>> getMealsFromFirebase() {
        return remoteDataSource.getMealsFromRemote().subscribeOn(Schedulers.io());
    }

    public Single<List<FavouriteEntity>> getFavouritesFromFirebase() {
        return remoteDataSource.getFavouritesFromRemote().subscribeOn(Schedulers.io());
    }

    public Single<List<WeeklyPlanMealEntity>> getWeeklyPlanFromFirebase() {
        return remoteDataSource.getWeeklyPlanFromRemote().subscribeOn(Schedulers.io());
    }

    public Completable saveMealsToLocal(List<MealEntity> meals) {
        if (meals == null || meals.isEmpty()) return Completable.complete();
        return localDataSource.insertMeals(meals).subscribeOn(Schedulers.io());
    }

    public Completable saveFavouritesToLocal(List<FavouriteEntity> favourites) {
        if (favourites == null || favourites.isEmpty()) return Completable.complete();
        return localDataSource.insertFavourites(favourites).subscribeOn(Schedulers.io());
    }

    public Completable saveWeeklyPlanToLocal(List<WeeklyPlanMealEntity> plans) {
        if (plans == null || plans.isEmpty()) return Completable.complete();
        return localDataSource.insertWeeklyPlan(plans).subscribeOn(Schedulers.io());
    }

    public Completable syncDataFromFirebase() {
        return Completable.concatArray(
                getMealsFromFirebase().flatMapCompletable(this::saveMealsToLocal),
                getFavouritesFromFirebase().flatMapCompletable(this::saveFavouritesToLocal),
                getWeeklyPlanFromFirebase().flatMapCompletable(this::saveWeeklyPlanToLocal)
        ).subscribeOn(Schedulers.io());
    }
}
