package com.mealify.mealify.presentation.auth.register.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mealify.mealify.core.helper.CustomLogger;
import com.mealify.mealify.data.auth.repo.AuthRepo;
import com.mealify.mealify.data.meals.repo.MealsRepo;
import com.mealify.mealify.presentation.auth.register.views.RegisterView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class RegisterPresenterImpl implements RegisterPresenter {

    private final AuthRepo authRepo;
    private final MealsRepo mealsRepo;
    private RegisterView view;
    private CompositeDisposable disposables = new CompositeDisposable();

    public RegisterPresenterImpl(Context context, RegisterView view) {
        this.view = view;
        this.authRepo = new AuthRepo(context);
        this.mealsRepo = new MealsRepo(context);
    }

    @Override
    public void register(String email, String password, String name) {
        if (view == null) return;

        view.toggleLoading(true);

        disposables.add(authRepo.register(email, password, name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userId -> {
                    if (view == null) return;
                    view.toggleLoading(false);
                    view.onSuccessRegister(userId);
                }, error -> {
                    if (view == null) return;
                    view.toggleLoading(false);
                    view.onFailureRegister(error.getMessage());
                }));
    }

    @Override
    public void googleSignIn() {
        view.toggleLoading(true);
        CustomLogger.log("Presenter: Starting Google Sign-In", "REGISTER_PRESENTER");
        
        disposables.add(authRepo.googleSignIn()
                .flatMapCompletable(userId -> {
                    CustomLogger.log("Presenter: Google Sign-In success, syncing data...", "REGISTER_PRESENTER");
                    return mealsRepo.syncDataFromFirebase();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    CustomLogger.log("Presenter: Data sync complete after Google Sign-In", "REGISTER_PRESENTER");
                    view.toggleLoading(false);
                    view.onSuccessRegister(authRepo.getCurrentUserId());
                }, error -> {
                    CustomLogger.log("Presenter: Google Sign-In or Data sync failed: " + error.getMessage(), "REGISTER_PRESENTER");
                    view.toggleLoading(false);
                    view.onFailureRegister(error.getMessage());
                }));
    }


    @Override
    public void signInAnonymously() {
        view.toggleLoading(true);
        disposables.add(authRepo.signInAnonymously()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userId -> {
                    view.toggleLoading(false);
                    view.onSuccessRegister(userId);
                }, error -> {
                    view.toggleLoading(false);
                    view.onFailureRegister(error.getMessage());
                }));
    }
}
