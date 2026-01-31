package com.mealify.mealify.data.auth.datasources;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface AuthService {
    Single<String> login(String email, String password);

    Single<String> register(String email, String password, String name);

    Single<String> signInAnonymously();

    Single<String> signInWithGoogle();

    Completable signOut();

    String getCurrentUserId();
}
