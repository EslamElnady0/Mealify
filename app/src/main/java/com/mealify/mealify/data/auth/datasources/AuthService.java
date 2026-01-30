package com.mealify.mealify.data.auth.datasources;

import com.mealify.mealify.core.response.GeneralResponse;

public interface AuthService {
    public void login(String email, String password, GeneralResponse<String> generalResponse);

    public void register(String email, String password, String name, GeneralResponse<String> generalResponse);

    public void signInAnonymously(GeneralResponse<String> generalResponse);

    public void signInWithGoogle(GeneralResponse<String> generalResponse);

    public void signOut(GeneralResponse<String> generalResponse);

    public String getCurrentUserId();
}
