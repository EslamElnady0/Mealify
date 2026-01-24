package com.mealify.mealify.data.meals.datasources.remote;

import android.content.Context;

import com.mealify.mealify.core.response.ApiResponse;
import com.mealify.mealify.data.meals.mapper.MealMapper;
import com.mealify.mealify.data.meals.model.category.CategoriesResponse;
import com.mealify.mealify.data.meals.model.category.CategoriesStrResponse;
import com.mealify.mealify.data.meals.model.category.CategoryDto;
import com.mealify.mealify.data.meals.model.category.CategoryStrDto;
import com.mealify.mealify.data.meals.model.country.CountriesResponse;
import com.mealify.mealify.data.meals.model.country.CountryDto;
import com.mealify.mealify.data.meals.model.filteredmeals.FilterType;
import com.mealify.mealify.data.meals.model.filteredmeals.FilteredMeal;
import com.mealify.mealify.data.meals.model.filteredmeals.FilteredMealsResponse;
import com.mealify.mealify.data.meals.model.ingredient.IngredientDto;
import com.mealify.mealify.data.meals.model.ingredient.IngredientsResponse;
import com.mealify.mealify.data.meals.model.meal.MealDto;
import com.mealify.mealify.data.meals.model.meal.MealEntity;
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

    public void getRandomMeal(ApiResponse<List<MealDto>> apiResponse) {
        mealService.getRandomMeal().enqueue(new Callback<MealsResponse>() {
            @Override
            public void onResponse(Call<MealsResponse> call, Response<MealsResponse> response) {
                if (response.isSuccessful()) {
                    apiResponse.onSuccess(response.body().meals);
                } else {
                    apiResponse.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<MealsResponse> call, Throwable t) {
                apiResponse.onError("Error: " + t.getMessage());
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

    public void getMealDetails(String mealId, ApiResponse<MealEntity> apiResponse) {
        mealService.getMealDetails(mealId).enqueue(new Callback<MealsResponse>() {
            @Override
            public void onResponse(Call<MealsResponse> call, Response<MealsResponse> response) {
                if (response.isSuccessful()) {
                    MealDto mealDto = response.body().meals.get(0);
                    MealEntity meal = MealMapper.toEntity(mealDto);
                    apiResponse.onSuccess(meal);
                } else {
                    apiResponse.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<MealsResponse> call, Throwable t) {
                apiResponse.onError("Error: " + t.getMessage());
            }
        });
    }

    public void listIngredients(ApiResponse<List<IngredientDto>> apiResponse) {
        mealService.listIngredients().enqueue(new Callback<IngredientsResponse>() {
            @Override
            public void onResponse(Call<IngredientsResponse> call, Response<IngredientsResponse> response) {
                if (response.isSuccessful()) {
                    apiResponse.onSuccess(response.body().getIngredients());
                } else {
                    apiResponse.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<IngredientsResponse> call, Throwable t) {
                apiResponse.onError("Error: " + t.getMessage());
            }
        });
    }

    public void listCategories(ApiResponse<List<CategoryStrDto>> apiResponse) {
        mealService.listCategories().enqueue(new Callback<CategoriesStrResponse>() {
            @Override
            public void onResponse(Call<CategoriesStrResponse> call, Response<CategoriesStrResponse> response) {
                if (response.isSuccessful()) {
                    apiResponse.onSuccess(response.body().getCategoriesStr());
                } else {
                    apiResponse.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<CategoriesStrResponse> call, Throwable t) {
                apiResponse.onError("Error: " + t.getMessage());
            }
        });
    }

    public void listAreas(ApiResponse<List<CountryDto>> apiResponse) {
        mealService.listAreas().enqueue(new Callback<CountriesResponse>() {
            @Override
            public void onResponse(Call<CountriesResponse> call, Response<CountriesResponse> response) {
                if (response.isSuccessful()) {
                    apiResponse.onSuccess(response.body().getMeals());
                } else {
                    apiResponse.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<CountriesResponse> call, Throwable t) {
                apiResponse.onError("Error: " + t.getMessage());
            }
        });
    }

    public void getFilteredMeals(FilterType filterType, String query, ApiResponse<List<FilteredMeal>> apiResponse) {
        Call<FilteredMealsResponse> call;

        switch (filterType) {
            case AREA:
                call = mealService.filterMealsByArea(query);
                break;
            case CATEGORY:
                call = mealService.filterMealsByCategory(query);
                break;
            case INGREDIENT:
                call = mealService.filterMealsByIngredient(query);
                break;
            default:
                call = mealService.filterMealsByCategory(query);
        }
        call.enqueue(new Callback<FilteredMealsResponse>() {
            @Override
            public void onResponse(Call<FilteredMealsResponse> call, Response<FilteredMealsResponse> response) {
                if (response.isSuccessful()) {
                    apiResponse.onSuccess(response.body().getMeals());
                } else {
                    apiResponse.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<FilteredMealsResponse> call, Throwable t) {
                apiResponse.onError("Error: " + t.getMessage());
            }
        });

    }

    public void searchMealsByName(String name, ApiResponse<List<FilteredMeal>> apiResponse) {
        mealService.searchMealsByName(name).enqueue(new Callback<MealsResponse>() {
            @Override
            public void onResponse(Call<MealsResponse> call, Response<MealsResponse> response) {
                if (response.isSuccessful()) {
                    List<FilteredMeal> filteredMeals = MealMapper.toFilteredMeals(response.body().meals);
                    apiResponse.onSuccess(filteredMeals);
                } else {
                    apiResponse.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<MealsResponse> call, Throwable t) {
                apiResponse.onError("Error: " + t.getMessage());
            }
        });
    }
}

