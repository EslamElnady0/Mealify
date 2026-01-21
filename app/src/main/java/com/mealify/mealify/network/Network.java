package com.mealify.mealify.network;

import android.content.Context;

import com.mealify.mealify.data.auth.datasources.AuthService;
import com.mealify.mealify.data.auth.datasources.FirebaseAuthService;
import com.mealify.mealify.data.meals.datasources.remote.MealService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {

    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static Network instance = null;
    private final MealService mealService;
    private final AuthService authService;
    private Network(Context ctx){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mealService = retrofit.create(MealService.class);
        authService = FirebaseAuthService.getInstance(ctx);
    }

    public static Network getInstance(Context ctx){
        if(instance == null){
            instance = new Network(ctx);
        }
        return instance;
    }

    public MealService getHomeService(){
        return mealService;
    }
    public AuthService getAuthService() {
        return authService;
    }
}
