package com.mealify.mealify.core.datasource.remote.response;

public interface ApiResponse<T> {
    public void onSuccess(T data);
    public void onError(String errorMessage);
}
