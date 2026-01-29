package com.mealify.mealify.core.response;

public interface GeneralResponse<T> {
    public void onSuccess(T data);

    public void onError(String errorMessage);
}
