package com.mealify.mealify.presentation.auth.login.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.data.auth.repo.AuthRepo;
import com.mealify.mealify.data.meals.repo.MealsRepo;
import com.mealify.mealify.presentation.auth.login.views.LoginView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class LoginPresenterImpl implements LoginPresenter {
    private AuthRepo authRepo;
    private MealsRepo mealsRepo;
    private LoginView view;
    private CompositeDisposable disposables = new CompositeDisposable();

    public LoginPresenterImpl(Context ctx, LoginView view) {
        this.view = view;
        this.authRepo = new AuthRepo(ctx);
        this.mealsRepo = new MealsRepo(ctx);
    }

    @Override
    public void login(String email, String password) {
        view.toggleLoading(true);
        authRepo.login(email, password, new GeneralResponse<String>() {
            @SuppressLint("CheckResult")
            @Override
            public void onSuccess(String response) {
                mealsRepo.syncDataFromFirebase()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            view.toggleLoading(false);
                            view.onSuccessLogin(response);
                        }, error -> {
                            view.toggleLoading(false);
                            view.onFailureLogin("Login success, but failed to sync data: " + error.getMessage());
                        });
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
        authRepo.googleSignIn(new GeneralResponse<String>() {
            @SuppressLint("CheckResult")
            @Override
            public void onSuccess(String response) {
                mealsRepo.syncDataFromFirebase()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            view.toggleLoading(false);
                            view.onSuccessLogin(response);
                        }, error -> {
                            view.toggleLoading(false);
                            view.onFailureLogin("Login success, but failed to sync data: " + error.getMessage());
                        });
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
        authRepo.signInAnonymously(new GeneralResponse<String>() {
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
