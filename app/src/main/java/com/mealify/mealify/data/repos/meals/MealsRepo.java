package com.mealify.mealify.data.repos.meals;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mealify.mealify.core.response.GeneralResponse;
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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
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

    @SuppressLint("CheckResult")
    public void getRandomMeal(GeneralResponse<List<MealDto>> generalResponse) {
        remoteDataSource.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .map(response -> response.meals)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        generalResponse::onSuccess,
                        error -> generalResponse.onError(error.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    public void getCategories(GeneralResponse<List<CategoryDto>> generalResponse) {
        remoteDataSource.getCategories()
                .subscribeOn(Schedulers.io())
                .map(response -> response.categories)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        generalResponse::onSuccess,
                        error -> generalResponse.onError(error.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    public void getMealDetails(String mealId, GeneralResponse<MealEntity> generalResponse) {
        remoteDataSource.getMealDetails(mealId)
                .subscribeOn(Schedulers.io())
                .map(response -> MealMapper.toEntity(response.meals.get(0)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        generalResponse::onSuccess,
                        error -> generalResponse.onError(error.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    public void listIngredients(GeneralResponse<List<IngredientDto>> generalResponse) {
        remoteDataSource.listIngredients()
                .subscribeOn(Schedulers.io())
                .map(IngredientsResponse::getIngredients)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        generalResponse::onSuccess,
                        error -> generalResponse.onError(error.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    public void listAreas(GeneralResponse<List<CountryDto>> generalResponse) {
        remoteDataSource.listAreas()
                .subscribeOn(Schedulers.io())
                .map(CountriesResponse::getMeals)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        generalResponse::onSuccess,
                        error -> generalResponse.onError(error.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    public void getFilteredMeals(
            FilterType filterType,
            String query,
            GeneralResponse<List<FilteredMeal>> generalResponse
    ) {
        remoteDataSource.getFilteredMeals(filterType, query)
                .subscribeOn(Schedulers.io())
                .map(FilteredMealsResponse::getMeals)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        generalResponse::onSuccess,
                        error -> generalResponse.onError(error.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    public void searchMealsByName(
            String name,
            GeneralResponse<List<FilteredMeal>> generalResponse
    ) {
        remoteDataSource.searchMealsByName(name)
                .subscribeOn(Schedulers.io())
                .map(response -> MealMapper.toFilteredMeals(response.meals))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        generalResponse::onSuccess,
                        error -> generalResponse.onError(error.getMessage())
                );
    }


    @SuppressLint("CheckResult")
    public void insertMealInFavorites(MealEntity meal) {
        localDataSource.insertMeal(meal)
                .andThen(remoteDataSource.saveMeal(meal.getId(), meal))
                .andThen(localDataSource.addToFavourites(meal.getId()))
                .andThen(remoteDataSource.saveToFavourites(meal.getId(), meal))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @SuppressLint("CheckResult")
    public void deleteMealFromFavorites(String mealId) {
        localDataSource.removeFromFavourites(mealId)
                .andThen(remoteDataSource.deleteFromFavourites(mealId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @SuppressLint("CheckResult")
    public void isMealFavorite(String mealId, GeneralResponse<Boolean> generalResponse) {
        localDataSource.isFavourite(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(generalResponse::onSuccess, error -> generalResponse.onError(error.getMessage()));
    }

    @SuppressLint("CheckResult")
    public void getFavouriteMeals(GeneralResponse<List<FavouriteWithMeal>> generalResponse) {
        localDataSource.getAllFavourites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(generalResponse::onSuccess, error -> generalResponse.onError(error.getMessage()));
    }

    @SuppressLint("CheckResult")
    public void getFavouritesCount(GeneralResponse<Integer> generalResponse) {
        localDataSource.getFavouritesCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(generalResponse::onSuccess, error -> generalResponse.onError(error.getMessage()));
    }

    // --- Weekly Plan Methods ---

    @SuppressLint("CheckResult")
    public void addMealToPlan(WeeklyPlanMealWithMeal planMealWithMeal, GeneralResponse<Boolean> generalResponse) {
        addMealToPlanCompletable(planMealWithMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> generalResponse.onSuccess(true),
                        error -> generalResponse.onError(error.getMessage())
                );
    }

    private Completable addMealToPlanCompletable(WeeklyPlanMealWithMeal planMealWithMeal) {
        return localDataSource.insertMeal(planMealWithMeal.meal)
                .andThen(remoteDataSource.saveMeal(planMealWithMeal.meal.getId(), planMealWithMeal.meal))
                .andThen(localDataSource.addMealToWeeklyPlan(planMealWithMeal.planEntry))
                .andThen(remoteDataSource.saveToWeeklyPlan(planMealWithMeal.planEntry.getMealId(), planMealWithMeal.planEntry));
    }

    @SuppressLint("CheckResult")
    public void replaceMealInPlan(String oldMealId, WeeklyPlanMealWithMeal newMeal, GeneralResponse<Boolean> generalResponse) {
        localDataSource.deleteMealByDateAndType(newMeal.planEntry.getDateString(), newMeal.planEntry.getMealType())
                .andThen(remoteDataSource.deleteFromWeeklyPlan(oldMealId))
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
                .subscribe(generalResponse::onSuccess, error -> generalResponse.onError(error.getMessage()));
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
                .andThen(remoteDataSource.deleteFromWeeklyPlan(String.valueOf(planId)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @SuppressLint("CheckResult")
    public void getAllPlannedDates(GeneralResponse<List<String>> generalResponse) {
        localDataSource.getAllPlannedDates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(generalResponse::onSuccess, error -> generalResponse.onError(error.getMessage()));
    }

    @SuppressLint("CheckResult")
    public void getPlannedMealsCount(GeneralResponse<Integer> generalResponse) {
        localDataSource.getPlannedMealsCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(generalResponse::onSuccess, error -> generalResponse.onError(error.getMessage()));
    }

    // --- Local Storage Management ---

    public Observable<List<MealEntity>> getAllLocalMeals() {
        return localDataSource.getAllMeals();
    }

    public Observable<List<FavouriteWithMeal>> getAllLocalFavourites() {
        return localDataSource.getAllFavourites();
    }

    public Observable<List<WeeklyPlanMealWithMeal>> getAllLocalWeeklyPlans() {
        return localDataSource.getAllPlannedMeals();
    }

    public Completable removeAllLocalMeals() {
        return localDataSource.deleteAllMeals();
    }

    public Completable removeAllLocalFavourites() {
        return localDataSource.deleteAllFavourites();
    }

    public Completable removeAllLocalWeeklyPlans() {
        return localDataSource.clearWeeklyPlan();
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
