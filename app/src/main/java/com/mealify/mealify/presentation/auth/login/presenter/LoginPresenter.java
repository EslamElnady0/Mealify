package com.mealify.mealify.presentation.auth.login.presenter;

public interface LoginPresenter {
    void login(String email, String password);
    void googleSignIn();
    void signInAnonymously();
}
