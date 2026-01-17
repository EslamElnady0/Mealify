package com.mealify.mealify.features.home.data.network;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeNetwork {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static HomeNetwork instance = null;
    private final HomeService homeService;
    private HomeNetwork(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        homeService = retrofit.create(HomeService.class);
    }

    public static HomeNetwork getInstance(){
        if(instance == null){
            instance = new HomeNetwork();
        }
        return instance;
    }

    public HomeService getHomeService(){
        return homeService;
    }
}
