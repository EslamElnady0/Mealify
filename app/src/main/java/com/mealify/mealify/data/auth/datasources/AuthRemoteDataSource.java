package com.mealify.mealify.data.auth.datasources;

import android.content.Context;

import com.mealify.mealify.core.response.GeneralResponse;

public class AuthRemoteDataSource {
    private static AuthRemoteDataSource instance = null;
    AuthService authService;

    private AuthRemoteDataSource(Context context) {
        authService = FirebaseAuthService.getInstance(context);
    }

    public static AuthRemoteDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new AuthRemoteDataSource(context);
        }
        return instance;
    }

    public void login(String email, String password, GeneralResponse<String> generalResponse) {
        authService.login(email, password, generalResponse);
    }

    public void register(String email, String password, String name, GeneralResponse<String> generalResponse) {
        authService.register(email, password, name, generalResponse);
    }

    public void googleSignIn(GeneralResponse<String> generalResponse) {
        authService.signInWithGoogle(generalResponse);
    }

    public void signInAnonymously(GeneralResponse<String> generalResponse) {
        authService.signInAnonymously(generalResponse);
    }

    public void logout(GeneralResponse<String> generalResponse) {
        authService.signOut(generalResponse);
    }

}
