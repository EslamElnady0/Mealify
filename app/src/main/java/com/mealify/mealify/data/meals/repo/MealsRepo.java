package com.mealify.mealify.data.meals.repo;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.data.favs.datasource.local.FavouriteLocalDataSource;
import com.mealify.mealify.data.favs.datasource.remote.FavouriteRemoteDataSource;
import com.mealify.mealify.data.favs.model.fav.FavouriteEntity;
import com.mealify.mealify.data.favs.model.fav.FavouriteWithMeal;
import com.mealify.mealify.data.meals.datasources.local.MealLocalDataSource;
import com.mealify.mealify.data.meals.datasources.remote.MealFirestoreRemoteDataSource;
import com.mealify.mealify.data.meals.datasources.remote.MealRemoteDataSource;
import com.mealify.mealify.data.meals.mapper.MealMapper;
import com.mealify.mealify.data.meals.model.category.CategoryDto;
import com.mealify.mealify.data.meals.model.country.CountriesResponse;
import com.mealify.mealify.data.meals.model.country.CountryDto;
import com.mealify.mealify.data.meals.model.filteredmeals.FilterType;
import com.mealify.mealify.data.meals.model.filteredmeals.FilteredMeal;
import com.mealify.mealify.data.meals.model.filteredmeals.FilteredMealsResponse;
import com.mealify.mealify.data.meals.model.ingredient.IngredientDto;
import com.mealify.mealify.data.meals.model.ingredient.IngredientsResponse;
import com.mealify.mealify.data.meals.model.meal.MealDto;
import com.mealify.mealify.data.meals.model.meal.MealEntity;
import com.mealify.mealify.data.weeklyplan.datasource.local.WeeklyPlanLocalDataSource;
import com.mealify.mealify.data.weeklyplan.datasource.remote.WeeklyPlanRemoteDataSource;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealEntity;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsRepo {

    private final MealRemoteDataSource remoteDataSource;
    private final MealLocalDataSource localDataSource;
    private final FavouriteLocalDataSource favouriteLocalDataSource;
    private final WeeklyPlanLocalDataSource weeklyPlanLocalDataSource;
    private final MealFirestoreRemoteDataSource mealFirestoreRemoteDataSource;
    private final FavouriteRemoteDataSource favouriteRemoteDataSource;
    private final WeeklyPlanRemoteDataSource weeklyPlanRemoteDataSource;

    public MealsRepo(Context ctx) {
        this.remoteDataSource = new MealRemoteDataSource(ctx);
        this.localDataSource = new MealLocalDataSource(ctx);
        this.favouriteLocalDataSource = new FavouriteLocalDataSource(ctx);
        this.weeklyPlanLocalDataSource = new WeeklyPlanLocalDataSource(ctx);
        this.mealFirestoreRemoteDataSource = new MealFirestoreRemoteDataSource();
        this.favouriteRemoteDataSource = new FavouriteRemoteDataSource();
        this.weeklyPlanRemoteDataSource = new WeeklyPlanRemoteDataSource();
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

    public Observable<List<MealEntity>> getAllLocalMeals() {
        return localDataSource.getAllMeals();
    }

    public Observable<List<FavouriteWithMeal>> getAllLocalFavourites() {
        return favouriteLocalDataSource.getAllFavourites();
    }

    public Observable<List<WeeklyPlanMealWithMeal>> getAllLocalWeeklyPlans() {
        return weeklyPlanLocalDataSource.getAllPlannedMeals();
    }

    public Completable removeAllLocalMeals() {
        return localDataSource.deleteAllMeals();
    }

    public Completable removeAllLocalFavourites() {
        return favouriteLocalDataSource.deleteAllFavourites();
    }

    public Completable removeAllLocalWeeklyPlans() {
        return weeklyPlanLocalDataSource.clearWeeklyPlan();
    }

    public Completable uploadMealsToFirebase(List<MealEntity> meals) {
        if (meals == null || meals.isEmpty()) return Completable.complete();
        return Observable.fromIterable(meals)
                .flatMapCompletable(meal -> mealFirestoreRemoteDataSource.saveMeal(meal.getId(), meal))
                .subscribeOn(Schedulers.io());
    }

    public Completable uploadFavouritesToFirebase(List<FavouriteEntity> favourites) {
        if (favourites == null || favourites.isEmpty()) return Completable.complete();
        return Observable.fromIterable(favourites)
                .flatMapCompletable(fav -> favouriteRemoteDataSource.saveToFavourites(fav.getMealId(), fav))
                .subscribeOn(Schedulers.io());
    }

    public Completable uploadWeeklyPlanToFirebase(List<WeeklyPlanMealEntity> plans) {
        if (plans == null || plans.isEmpty()) return Completable.complete();
        return Observable.fromIterable(plans)
                .flatMapCompletable(plan -> weeklyPlanRemoteDataSource
                        .saveToWeeklyPlan(String.valueOf(plan.getMealId()), plan))
                .subscribeOn(Schedulers.io());
    }

    public Single<List<MealEntity>> getMealsFromFirebase() {
        return mealFirestoreRemoteDataSource.getMealsFromRemote()
                .subscribeOn(Schedulers.io());
    }

    public Single<List<FavouriteEntity>> getFavouritesFromFirebase() {
        return favouriteRemoteDataSource.getFavouritesFromRemote()
                .subscribeOn(Schedulers.io());
    }

    public Single<List<WeeklyPlanMealEntity>> getWeeklyPlanFromFirebase() {
        return weeklyPlanRemoteDataSource.getWeeklyPlanFromRemote()
                .subscribeOn(Schedulers.io());
    }

    public Completable saveMealsToLocal(List<MealEntity> meals) {
        if (meals == null || meals.isEmpty()) return Completable.complete();
        return localDataSource.insertMeals(meals).subscribeOn(Schedulers.io());
    }

    public Completable saveFavouritesToLocal(List<FavouriteEntity> favourites) {
        if (favourites == null || favourites.isEmpty()) return Completable.complete();
        return favouriteLocalDataSource.insertFavourites(favourites).subscribeOn(Schedulers.io());
    }

    public Completable saveWeeklyPlanToLocal(List<WeeklyPlanMealEntity> plans) {
        if (plans == null || plans.isEmpty()) return Completable.complete();
        return weeklyPlanLocalDataSource.insertWeeklyPlan(plans).subscribeOn(Schedulers.io());
    }

    public Completable syncDataFromFirebase() {
        return Completable.concatArray(
                getMealsFromFirebase()
                        .flatMapCompletable(this::saveMealsToLocal),
                getFavouritesFromFirebase()
                        .flatMapCompletable(this::saveFavouritesToLocal),
                getWeeklyPlanFromFirebase()
                        .flatMapCompletable(this::saveWeeklyPlanToLocal)
        ).subscribeOn(Schedulers.io());
    }
}
