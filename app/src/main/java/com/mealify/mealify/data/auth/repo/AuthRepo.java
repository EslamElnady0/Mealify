package com.mealify.mealify.data.auth.repo;

import android.content.Context;

import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.data.auth.datasources.AuthRemoteDataSource;

public class AuthRepo {
    private AuthRemoteDataSource authRemoteDataSource;

    public AuthRepo(Context context) {
        this.authRemoteDataSource = authRemoteDataSource = AuthRemoteDataSource.getInstance(context);
    }

    public void login(String email, String password, GeneralResponse<String> generalResponse) {
        authRemoteDataSource.login(email, password, generalResponse);
    }

    public void register(String email, String password, String name, GeneralResponse<String> generalResponse) {
        authRemoteDataSource.register(email, password, name, generalResponse);
    }

    public void googleSignIn(GeneralResponse<String> generalResponse) {
        authRemoteDataSource.googleSignIn(generalResponse);
    }

    public void signInAnonymously(GeneralResponse<String> generalResponse) {
        authRemoteDataSource.signInAnonymously(generalResponse);
    }

    public void logout(GeneralResponse<String> generalResponse) {
        authRemoteDataSource.logout(generalResponse);
    }
}
