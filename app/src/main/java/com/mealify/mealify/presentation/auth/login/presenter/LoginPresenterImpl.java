package com.mealify.mealify.presentation.auth.login.presenter;

import android.content.Context;

import com.mealify.mealify.core.response.ApiResponse;
import com.mealify.mealify.data.auth.repo.AuthRepo;
import com.mealify.mealify.presentation.auth.login.views.LoginView;

public class LoginPresenterImpl implements LoginPresenter{
    private AuthRepo authRepo;
    private LoginView view;
    public LoginPresenterImpl(Context ctx, LoginView view) {
        this.view = view;
        this.authRepo = new AuthRepo(ctx);
    }

    @Override
    public void login(String email, String password) {
        view.toggleLoading(true);
        authRepo.login(email, password, new ApiResponse<String>() {
            @Override
            public void onSuccess(String response) {
                view.toggleLoading(false);
                view.onSuccessLogin(response);
            }

            @Override
            public void onError(String errorMessage) {
                view.toggleLoading(false);
                view.onFailureLogin(errorMessage);
            }
        });
    }


    @Override
    public void googleSignIn() {
        view.toggleLoading(true);
        authRepo.googleSignIn(new ApiResponse<String>() {
            @Override
            public void onSuccess(String response) {
                view.toggleLoading(false);
                view.onSuccessLogin(response);
            }

            @Override
            public void onError(String errorMessage) {
                view.toggleLoading(false);
                view.onFailureLogin(errorMessage);
            }
        });
    }

    @Override
    public void signInAnonymously() {
        view.toggleLoading(true);
        authRepo.signInAnonymously(new ApiResponse<String>() {
            @Override
            public void onSuccess(String response) {
                view.toggleLoading(false);
                view.onSuccessLogin(response);
            }

            @Override
            public void onError(String errorMessage) {
                view.toggleLoading(false);
                view.onFailureLogin(errorMessage);
            }
        });
    }
}
