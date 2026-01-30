package com.mealify.mealify.presentation.auth.login.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mealify.mealify.core.helper.CustomLogger;
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
        CustomLogger.log("Presenter: Starting email login for " + email, "LOGIN_PRESENTER");
        authRepo.login(email, password, new GeneralResponse<String>() {
            @SuppressLint("CheckResult")
            @Override
            public void onSuccess(String response) {
                CustomLogger.log("Presenter: Login success, syncing data...", "LOGIN_PRESENTER");
                mealsRepo.syncDataFromFirebase()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            CustomLogger.log("Presenter: Data sync complete", "LOGIN_PRESENTER");
                            view.toggleLoading(false);
                            view.onSuccessLogin(response);
                        }, error -> {
                            CustomLogger.log("Presenter: Data sync failed: " + error.getMessage(), "LOGIN_PRESENTER");
                            view.toggleLoading(false);
                            view.onFailureLogin("Login success, but failed to sync data: " + error.getMessage());
                        });
            }

            @Override
            public void onError(String errorMessage) {
                CustomLogger.log("Presenter: Email login failed: " + errorMessage, "LOGIN_PRESENTER");
                view.toggleLoading(false);
                view.onFailureLogin(errorMessage);
            }
        });
    }


    @Override
    public void googleSignIn() {
        view.toggleLoading(true);
        CustomLogger.log("Presenter: Starting Google Sign-In", "LOGIN_PRESENTER");
        authRepo.googleSignIn(new GeneralResponse<String>() {
            @SuppressLint("CheckResult")
            @Override
            public void onSuccess(String response) {
                CustomLogger.log("Presenter: Google Sign-In success, syncing data...", "LOGIN_PRESENTER");
                mealsRepo.syncDataFromFirebase()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            CustomLogger.log("Presenter: Data sync complete after Google Sign-In", "LOGIN_PRESENTER");
                            view.toggleLoading(false);
                            view.onSuccessLogin(response);
                        }, error -> {
                            CustomLogger.log("Presenter: Data sync failed after Google Sign-In: " + error.getMessage(), "LOGIN_PRESENTER");
                            view.toggleLoading(false);
                            view.onFailureLogin("Login success, but failed to sync data: " + error.getMessage());
                        });
            }

            @Override
            public void onError(String errorMessage) {
                CustomLogger.log("Presenter: Google Sign-In failed: " + errorMessage, "LOGIN_PRESENTER");
                view.toggleLoading(false);
                view.onFailureLogin(errorMessage);
            }
        });
    }

    @Override
    public void signInAnonymously() {
        view.toggleLoading(true);
        CustomLogger.log("Presenter: Starting anonymous sign-in", "LOGIN_PRESENTER");
        authRepo.signInAnonymously(new GeneralResponse<String>() {
            @Override
            public void onSuccess(String response) {
                CustomLogger.log("Presenter: Anonymous sign-in success", "LOGIN_PRESENTER");
                view.toggleLoading(false);
                view.onSuccessLogin(response);
            }

            @Override
            public void onError(String errorMessage) {
                CustomLogger.log("Presenter: Anonymous sign-in failed: " + errorMessage, "LOGIN_PRESENTER");
                view.toggleLoading(false);
                view.onFailureLogin(errorMessage);
            }
        });
    }
}
