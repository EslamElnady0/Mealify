package com.mealify.mealify.features.home.data.datasource.remote;

import com.mealify.mealify.core.datasource.remote.response.ApiResponse;
import com.mealify.mealify.features.home.data.model.category.CategoriesResponse;
import com.mealify.mealify.features.home.data.model.category.CategoryDto;
import com.mealify.mealify.features.home.data.model.meal.MealDto;
import com.mealify.mealify.features.home.data.model.meal.MealsResponse;
import com.mealify.mealify.features.home.data.network.HomeNetwork;
import com.mealify.mealify.features.home.data.network.HomeService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRemoteDataSource {
    private final HomeNetwork homeNetwork = HomeNetwork.getInstance();
    private final HomeService homeService = homeNetwork.getHomeService();

    public void getRandomMeal(ApiResponse<List<MealDto>> apiResponse){
                homeService.getRandomMeal().enqueue(new Callback<MealsResponse>() {
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
        homeService.getCategories().enqueue(new Callback<CategoriesResponse>() {
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
