package com.mealify.mealify.presentation.profile.presenter;

import android.content.Context;
import com.google.firebase.auth.FirebaseUser;
import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.data.auth.datasources.FirebaseAuthService;
import com.mealify.mealify.data.favs.repo.FavRepo;
import com.mealify.mealify.data.weeklyplan.repo.WeeklyPlanRepo;
import com.mealify.mealify.presentation.profile.views.ProfileView;

public class ProfilePresenterImpl implements ProfilePresenter {

    private final Context context;
    private final ProfileView view;
    private final FirebaseAuthService authService;
    private final FavRepo favRepo;
    private final WeeklyPlanRepo planRepo;

    private int favoritesCount = 0;
    private int plansCount = 0;

    public ProfilePresenterImpl(Context context, ProfileView view) {
        this.context = context;
        this.view = view;
        this.authService = FirebaseAuthService.getInstance(context);
        this.favRepo = new FavRepo(context);
        this.planRepo = new WeeklyPlanRepo(context);
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
    public void logout() {
        view.toggleLoading(true);
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
    }

    @Override
    public void onDestroy() {
        // Cleanup if needed
    }
}
