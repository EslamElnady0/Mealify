package com.mealify.mealify.data.auth.repo;

import android.content.Context;

import com.mealify.mealify.data.auth.datasources.AuthRemoteDataSource;
import com.mealify.mealify.data.auth.datasources.PrefsDataSource;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class AuthRepo {
    private AuthRemoteDataSource authRemoteDataSource;
    private PrefsDataSource prefsDataSource;

    public AuthRepo(Context context) {
        this.authRemoteDataSource = AuthRemoteDataSource.getInstance(context);
        this.prefsDataSource = new PrefsDataSource(context);
    }

    public Single<String> login(String email, String password) {
        return authRemoteDataSource.login(email, password)
                .doOnSuccess(userId -> prefsDataSource.saveString("user_id", userId));
    }

    public Single<String> register(String email, String password, String name) {
        return authRemoteDataSource.register(email, password, name)
                .doOnSuccess(userId -> prefsDataSource.saveString("user_id", userId));
    }

    public Single<String> googleSignIn() {
        return authRemoteDataSource.googleSignIn()
                .doOnSuccess(userId -> prefsDataSource.saveString("user_id", userId));
    }

    public Single<String> signInAnonymously() {
        return authRemoteDataSource.signInAnonymously()
                .doOnSuccess(userId -> prefsDataSource.saveString("user_id", "guest_" + userId));
    }

    public Completable logout() {
        return authRemoteDataSource.logout();
    }

    public String getCurrentUserId() {
        return authRemoteDataSource.getCurrentUserId();
    }
}
