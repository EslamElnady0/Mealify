package com.mealify.mealify.data.meals.datasources.remote;

import android.content.Context;

import com.mealify.mealify.core.response.ApiResponse;
import com.mealify.mealify.data.meals.model.category.CategoriesResponse;
import com.mealify.mealify.data.meals.model.category.CategoryDto;
import com.mealify.mealify.data.meals.model.meal.MealDto;
import com.mealify.mealify.data.meals.model.meal.MealsResponse;
import com.mealify.mealify.network.Network;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealRemoteDataSource {
    private final Network network;
    private final MealService mealService;

        public MealRemoteDataSource(Context ctx) {
            network = Network.getInstance(ctx);
            mealService = network.getHomeService();
        }
    public void getRandomMeal(ApiResponse<List<MealDto>> apiResponse){
                mealService.getRandomMeal().enqueue(new Callback<MealsResponse>() {
                    @Override
                    public void onResponse(Call<MealsResponse> call, Response<MealsResponse> response) {
                        if (response.isSuccessful()){
                            apiResponse.onSuccess(response.body().meals);
                        } else {
                            apiResponse.onError("Something went wrong");
                        }
                    }

                    @Override
                    public void onFailure(Call<MealsResponse> call, Throwable t) {
                        apiResponse.onError("Error: "+ t.getMessage());
                    }
                });
    }

    public void getCategories(ApiResponse<List<CategoryDto>> apiResponse) {
        mealService.getCategories().enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                if (response.isSuccessful()) {
                    apiResponse.onSuccess(response.body().categories);
                } else {
                    apiResponse.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                apiResponse.onError("Error: " + t.getMessage());
            }
        });

    }

}
