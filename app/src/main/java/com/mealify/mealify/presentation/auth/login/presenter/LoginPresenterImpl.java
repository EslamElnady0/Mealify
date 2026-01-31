package com.mealify.mealify.presentation.auth.login.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mealify.mealify.core.helper.CustomLogger;
import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.data.repos.auth.AuthRepo;
import com.mealify.mealify.data.repos.meals.MealsRepo;
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
        
        disposables.add(authRepo.login(email, password)
                .flatMapCompletable(userId -> {
                    CustomLogger.log("Presenter: Login success, syncing data...", "LOGIN_PRESENTER");
                    return mealsRepo.syncDataFromFirebase();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    CustomLogger.log("Presenter: Data sync complete", "LOGIN_PRESENTER");
                    view.toggleLoading(false);
                    view.onSuccessLogin(authRepo.getCurrentUserId());
                }, error -> {
                    CustomLogger.log("Presenter: Login or Data sync failed: " + error.getMessage(), "LOGIN_PRESENTER");
                    view.toggleLoading(false);
                    view.onFailureLogin(error.getMessage());
                }));
    }


    @Override
    public void googleSignIn() {
        view.toggleLoading(true);
        CustomLogger.log("Presenter: Starting Google Sign-In", "LOGIN_PRESENTER");
        
        disposables.add(authRepo.googleSignIn()
                .flatMapCompletable(userId -> {
                    CustomLogger.log("Presenter: Google Sign-In success, syncing data...", "LOGIN_PRESENTER");
                    return mealsRepo.syncDataFromFirebase();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    CustomLogger.log("Presenter: Data sync complete after Google Sign-In", "LOGIN_PRESENTER");
                    view.toggleLoading(false);
                    view.onSuccessLogin(authRepo.getCurrentUserId());
                }, error -> {
                    CustomLogger.log("Presenter: Google Sign-In or Data sync failed: " + error.getMessage(), "LOGIN_PRESENTER");
                    view.toggleLoading(false);
                    view.onFailureLogin(error.getMessage());
                }));
    }

    @Override
    public void signInAnonymously() {
        view.toggleLoading(true);
        CustomLogger.log("Presenter: Starting anonymous sign-in", "LOGIN_PRESENTER");
        
        disposables.add(authRepo.signInAnonymously()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userId -> {
                    CustomLogger.log("Presenter: Anonymous sign-in success", "LOGIN_PRESENTER");
                    view.toggleLoading(false);
                    view.onSuccessLogin(userId);
                }, error -> {
                    CustomLogger.log("Presenter: Anonymous sign-in failed: " + error.getMessage(), "LOGIN_PRESENTER");
                    view.toggleLoading(false);
                    view.onFailureLogin(error.getMessage());
                }));
    }
}
