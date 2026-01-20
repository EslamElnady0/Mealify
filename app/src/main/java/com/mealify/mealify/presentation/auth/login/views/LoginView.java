package com.mealify.mealify.presentation.auth.login.views;

public interface LoginView {
    void onSuccessLogin(String message);
    void onFailureLogin(String errorMessage);
    void toggleLoading(boolean isLoading);
}
