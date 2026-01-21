package com.mealify.mealify.presentation.auth.register.presenter;

public interface RegisterPresenter {
    void register(String email, String password,String name);
    void googleSignIn();
    void signInAnonymously();
}
