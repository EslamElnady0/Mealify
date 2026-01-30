package com.mealify.mealify.data.auth.repo;

import android.content.Context;

import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.data.auth.datasources.AuthRemoteDataSource;
import com.mealify.mealify.data.auth.datasources.PrefsDataSource;

public class AuthRepo {
    private AuthRemoteDataSource authRemoteDataSource;
    private PrefsDataSource prefsDataSource;

    public AuthRepo(Context context) {
        this.authRemoteDataSource = AuthRemoteDataSource.getInstance(context);
        this.prefsDataSource = new PrefsDataSource(context);
    }

    public void login(String email, String password, GeneralResponse<String> generalResponse) {
        authRemoteDataSource.login(email, password, new GeneralResponse<String>() {
            @Override
            public void onSuccess(String data) {
                generalResponse.onSuccess(data);
                prefsDataSource.saveString("user_id", data);
            }

            @Override
            public void onError(String errorMessage) {
                generalResponse.onError(errorMessage);
            }
        });
    }

    public void register(String email, String password, String name, GeneralResponse<String> generalResponse) {
        authRemoteDataSource.register(email, password, name, new GeneralResponse<String>() {
            @Override
            public void onSuccess(String data) {
                generalResponse.onSuccess(data);
                prefsDataSource.saveString("user_id", data);
            }

            @Override
            public void onError(String errorMessage) {
                generalResponse.onError(errorMessage);
            }
        });
    }

    public void googleSignIn(GeneralResponse<String> generalResponse) {
        authRemoteDataSource.googleSignIn(new GeneralResponse<String>() {
            @Override
            public void onSuccess(String data) {
                generalResponse.onSuccess(data);
                prefsDataSource.saveString("user_id", data);
            }

            @Override
            public void onError(String errorMessage) {
                generalResponse.onError(errorMessage);
            }
        });
    }

    public void signInAnonymously(GeneralResponse<String> generalResponse) {
        authRemoteDataSource.signInAnonymously(new GeneralResponse<String>() {
            @Override
            public void onSuccess(String data) {
                generalResponse.onSuccess(data);
                prefsDataSource.saveString("user_id", "guest_" + data);
            }

            @Override
            public void onError(String errorMessage) {
                generalResponse.onError(errorMessage);
            }
        });
    }

    public void logout(GeneralResponse<String> generalResponse) {
        authRemoteDataSource.logout(new GeneralResponse<String>() {
            @Override
            public void onSuccess(String data) {
                generalResponse.onSuccess(data);
            }

            @Override
            public void onError(String errorMessage) {
                generalResponse.onError(errorMessage);
            }
        });
    }

    public String getCurrentUserId() {
        return authRemoteDataSource.getCurrentUserId();
    }
}
