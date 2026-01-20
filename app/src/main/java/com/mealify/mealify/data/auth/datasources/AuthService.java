package com.mealify.mealify.data.auth.datasources;

import com.mealify.mealify.core.response.ApiResponse;

public interface AuthService {
    public void login(String email, String password, ApiResponse<String> apiResponse);
    public void register(String email, String password, String name,ApiResponse<String> apiResponse);
    public void signInAnonymously(ApiResponse<String> apiResponse);
    public void signInWithGoogle(ApiResponse<String> apiResponse);
    public void signOut(ApiResponse<String> apiResponse);
}
