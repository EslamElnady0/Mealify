package com.mealify.mealify.presentation.profile.presenter;

import android.content.Context;
import com.google.firebase.auth.FirebaseUser;
import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.data.auth.datasources.FirebaseAuthService;
import android.annotation.SuppressLint;
import com.mealify.mealify.data.favs.repo.FavRepo;
import com.mealify.mealify.data.weeklyplan.repo.WeeklyPlanRepo;
import com.mealify.mealify.data.meals.repo.MealsRepo;
import com.mealify.mealify.data.meals.model.meal.MealEntity;
import com.mealify.mealify.data.favs.model.fav.FavouriteWithMeal;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;
import com.mealify.mealify.presentation.profile.views.ProfileView;
import java.util.List;
import java.util.stream.Collectors;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class ProfilePresenterImpl implements ProfilePresenter {

    private final Context context;
    private final ProfileView view;
    private final FirebaseAuthService authService;
    private final FavRepo favRepo;
    private final WeeklyPlanRepo planRepo;
    private final MealsRepo mealsRepo;

    private int favoritesCount = 0;
    private int plansCount = 0;

    public ProfilePresenterImpl(Context context, ProfileView view) {
        this.context = context;
        this.view = view;
        this.authService = FirebaseAuthService.getInstance(context);
        this.favRepo = new FavRepo(context);
        this.planRepo = new WeeklyPlanRepo(context);
        this.mealsRepo = new MealsRepo(context);
    }

    @Override
    public void loadUserData() {
        FirebaseUser user = authService.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            String name = user.getDisplayName();

            if (name == null || name.isEmpty()) {
                if (email != null && email.contains("@")) {
                    name = email.split("@")[0];
                } else {
                    name = "User";
                }
            }
            view.displayUserData(name, email);
        }
    }

    @Override
    public void loadStats() {
        view.toggleLoading(true);
        favRepo.getFavouritesCount(new GeneralResponse<Integer>() {
            @Override
            public void onSuccess(Integer count) {
                favoritesCount = count;
                loadPlansCount();
            }

            @Override
            public void onError(String errorMessage) {
                view.toggleLoading(false);
            }
        });
    }

    private void loadPlansCount() {
        planRepo.getPlannedMealsCount(new GeneralResponse<Integer>() {
            @Override
            public void onSuccess(Integer count) {
                plansCount = count;
                view.displayStats(favoritesCount, plansCount);
                view.toggleLoading(false);
            }

            @Override
            public void onError(String errorMessage) {
                view.toggleLoading(false);
            
            }
        });
    }

    @Override
    @SuppressLint("CheckResult")
    public void logout() {
        view.toggleLoading(true);

        Observable.combineLatest(
                mealsRepo.getAllLocalMeals().firstOrError().toObservable(),
                mealsRepo.getAllLocalFavourites().firstOrError().toObservable(),
                mealsRepo.getAllLocalWeeklyPlans().firstOrError().toObservable(),
                (meals, favs, plans) -> new DataBundle(meals, favs, plans)
        )
        .flatMapCompletable(bundle -> 
            mealsRepo.uploadMealsToFirebase(bundle.meals)
                .andThen(mealsRepo.uploadFavouritesToFirebase(
                        bundle.favs.stream().map(f -> f.favourite).collect(Collectors.toList())
                ))
                .andThen(mealsRepo.uploadWeeklyPlanToFirebase(
                        bundle.plans.stream().map(p -> p.planEntry).collect(Collectors.toList())
                ))
        )
        .andThen(mealsRepo.removeAllLocalFavourites())
        .andThen(mealsRepo.removeAllLocalWeeklyPlans())
        .andThen(mealsRepo.removeAllLocalMeals())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(() -> {
            authService.signOut(new GeneralResponse<String>() {
                @Override
                public void onSuccess(String result) {
                    view.toggleLoading(false);
                    view.onLogoutSuccess();
                }

                @Override
                public void onError(String errorMessage) {
                    view.toggleLoading(false);
                    view.onLogoutError(errorMessage);
                }
            });
        }, error -> {
            view.toggleLoading(false);
            view.onLogoutError(error.getMessage());
        });
    }

    private static class DataBundle {
        final List<MealEntity> meals;
        final List<FavouriteWithMeal> favs;
        final List<WeeklyPlanMealWithMeal> plans;

        DataBundle(List<MealEntity> meals, List<FavouriteWithMeal> favs, List<WeeklyPlanMealWithMeal> plans) {
            this.meals = meals;
            this.favs = favs;
            this.plans = plans;
        }
    }

    @Override
    public void onDestroy() {
    
    }
}
