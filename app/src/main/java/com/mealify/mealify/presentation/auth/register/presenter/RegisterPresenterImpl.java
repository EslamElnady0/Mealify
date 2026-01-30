package com.mealify.mealify.presentation.auth.register.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mealify.mealify.core.helper.CustomLogger;
import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.data.auth.repo.AuthRepo;
import com.mealify.mealify.data.meals.repo.MealsRepo;
import com.mealify.mealify.presentation.auth.register.views.RegisterView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class RegisterPresenterImpl implements RegisterPresenter {

    private final AuthRepo authRepo;
    private final MealsRepo mealsRepo;
    private RegisterView view;

    public RegisterPresenterImpl(Context context, RegisterView view) {
        this.view = view;
        this.authRepo = new AuthRepo(context);
        this.mealsRepo = new MealsRepo(context);
    }

    @Override
    public void register(String email, String password, String name) {
        if (view == null) return;

        view.toggleLoading(true);

        authRepo.register(email, password, name, new GeneralResponse<String>() {
            @Override
            public void onSuccess(String response) {
                if (view == null) return;
                view.toggleLoading(false);
                view.onSuccessRegister(response);
            }

            @Override
            public void onError(String errorMessage) {
                if (view == null) return;
                view.toggleLoading(false);
                view.onFailureRegister(errorMessage);
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
                            view.onSuccessRegister(response);
                        }, error -> {
                            CustomLogger.log("Presenter: Data sync failed after Google Sign-In: " + error.getMessage(), "LOGIN_PRESENTER");
                            view.toggleLoading(false);
                            view.onFailureRegister("Login success, but failed to sync data: " + error.getMessage());
                        });
            }

            @Override
            public void onError(String errorMessage) {
                CustomLogger.log("Presenter: Google Sign-In failed: " + errorMessage, "LOGIN_PRESENTER");
                view.toggleLoading(false);
                view.onFailureRegister(errorMessage);
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
                view.onSuccessRegister(response);
            }

            @Override
            public void onError(String errorMessage) {
                view.toggleLoading(false);
                view.onFailureRegister(errorMessage);
            }
        });
    }
}
