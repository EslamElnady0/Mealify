package com.mealify.mealify.presentation.splash.presenter;

import com.mealify.mealify.data.auth.repo.AuthRepo;
import com.mealify.mealify.presentation.splash.views.SplashView;

public class SplashPresenterImpl implements SplashPresenter {
    private final SplashView view;
    private final AuthRepo authRepo;

    public SplashPresenterImpl(SplashView view, AuthRepo authRepo) {
        this.view = view;
        this.authRepo = authRepo;
    }

    @Override
    public void checkLoginStatus() {
        String userId = authRepo.getCurrentUserId();
        if (userId != null && !userId.isEmpty()) {
            view.onUserAuthenticated(userId);
        } else {
            view.onUserNotAuthenticated();
        }
    }
}
