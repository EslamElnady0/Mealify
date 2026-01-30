package com.mealify.mealify.presentation.profile.views;

public interface ProfileView {
    void displayUserData(String name, String email);
    void displayStats(int favCount, int planCount);
    void onLogoutSuccess();
    void onLogoutError(String errorMessage);
    void toggleLoading(boolean isLoading);
}
