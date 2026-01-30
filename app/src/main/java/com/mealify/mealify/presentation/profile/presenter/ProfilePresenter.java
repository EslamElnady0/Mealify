package com.mealify.mealify.presentation.profile.presenter;

public interface ProfilePresenter {
    void loadUserData();
    void loadStats();
    void logout();
    void onDestroy();
}
