package com.mealify.mealify.features.auth.data.datasource;

import android.content.Context;

import com.mealify.mealify.core.datasource.remote.response.ApiResponse;
import com.mealify.mealify.features.auth.data.network.FirebaseAuthService;

public class AuthRemoteDataSource {
    private static AuthRemoteDataSource instance = null;
    FirebaseAuthService authService ;

    private AuthRemoteDataSource(Context context) {
        authService = FirebaseAuthService.getInstance(context);
    }
    public static AuthRemoteDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new AuthRemoteDataSource(context);
        }
        return instance;
    }

    public void login(String email, String password, ApiResponse<String> apiResponse) {
        authService.login(email, password, apiResponse);
    }

    public void register(String email, String password, String name,ApiResponse<String> apiResponse) {
        authService.register(email, password, name ,apiResponse);
    }

    public void googleSignIn(ApiResponse<String> apiResponse) {
        authService.signInWithGoogle(apiResponse);
    }

    public void signInAnonymously(ApiResponse<String> apiResponse) {
        authService.signInAnonymously(apiResponse);
    }
    public void logout(ApiResponse<String> apiResponse) {
        authService.signOut(apiResponse);
    }

}
