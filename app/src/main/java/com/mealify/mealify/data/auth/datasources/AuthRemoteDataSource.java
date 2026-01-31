package com.mealify.mealify.data.auth.datasources;

import android.content.Context;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

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

    public Single<String> login(String email, String password) {
        return authService.login(email, password);
    }

    public Single<String> register(String email, String password, String name) {
        return authService.register(email, password, name);
    }

    public Single<String> googleSignIn() {
        return authService.signInWithGoogle();
    }

    public Single<String> signInAnonymously() {
        return authService.signInAnonymously();
    }

    public Completable logout() {
        return authService.signOut();
    }

    public String getCurrentUserId() {
        return authService.getCurrentUserId();
    }

}
