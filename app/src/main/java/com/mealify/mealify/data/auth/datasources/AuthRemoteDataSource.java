package com.mealify.mealify.data.auth.datasources;

import android.content.Context;

import com.mealify.mealify.core.response.ApiResponse;

public class AuthRemoteDataSource {
    private static AuthRemoteDataSource instance = null;
    AuthService authService ;

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
