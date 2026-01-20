package com.mealify.mealify.data.auth.repo;

import android.content.Context;

import com.mealify.mealify.data.auth.datasources.AuthRemoteDataSource;

public class AuthRepo {
    private AuthRemoteDataSource authRemoteDataSource;
    public AuthRepo(Context context) {
        this.authRemoteDataSource = authRemoteDataSource = AuthRemoteDataSource.getInstance(context);
    }

    public void login(String email, String password, com.mealify.mealify.core.response.ApiResponse<String> apiResponse) {
        authRemoteDataSource.login(email, password, apiResponse);
    }

    public void register(String email, String password, String name, com.mealify.mealify.core.response.ApiResponse<String> apiResponse) {
        authRemoteDataSource.register(email, password, name, apiResponse);
    }
    public void googleSignIn(com.mealify.mealify.core.response.ApiResponse<String> apiResponse) {
        authRemoteDataSource.googleSignIn(apiResponse);
    }
    public void signInAnonymously(com.mealify.mealify.core.response.ApiResponse<String> apiResponse) {
        authRemoteDataSource.signInAnonymously(apiResponse);
    }
    public void logout(com.mealify.mealify.core.response.ApiResponse<String> apiResponse) {
        authRemoteDataSource.logout(apiResponse);
    }
}
