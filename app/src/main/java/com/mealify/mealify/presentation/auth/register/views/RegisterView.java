package com.mealify.mealify.presentation.auth.register.views;

public interface RegisterView {

    void onSuccessRegister(String message);

    void onFailureRegister(String errorMessage);

    void toggleLoading(boolean isLoading);
}