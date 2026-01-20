package com.mealify.mealify.presentation.auth.register.presenter;

import android.content.Context;
import com.mealify.mealify.core.response.ApiResponse;
import com.mealify.mealify.data.auth.repo.AuthRepo;
import com.mealify.mealify.presentation.auth.register.views.RegisterView;

public class RegisterPresenterImpl implements RegisterPresenter {

    private final AuthRepo authRepo;
    private RegisterView view;

    public RegisterPresenterImpl(Context context, RegisterView view) {
        this.view = view;
        this.authRepo = new AuthRepo(context);
    }

    @Override
    public void register(String email, String password, String name) {
        if (view == null) return;

        view.toggleLoading(true);

        authRepo.register(email, password, name, new ApiResponse<String>() {
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
        authRepo.googleSignIn(new ApiResponse<String>() {
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

    @Override
    public void signInAnonymously() {
        view.toggleLoading(true);
        authRepo.signInAnonymously(new ApiResponse<String>() {
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
