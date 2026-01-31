package com.mealify.mealify.presentation.search.presenter.ingredient;

import android.content.Context;

import com.mealify.mealify.core.response.GeneralResponse;
import com.mealify.mealify.data.models.ingredient.IngredientDto;
import com.mealify.mealify.data.repos.meals.MealsRepo;
import com.mealify.mealify.presentation.search.views.ingredient.SearchIngredientView;

import java.util.ArrayList;
import java.util.List;

public class SearchIngredientPresenterImpl implements SearchIngredientPresenter {

    private final SearchIngredientView view;
    private final MealsRepo mealsRepo;
    private List<IngredientDto> allIngredients = new ArrayList<>();

    public SearchIngredientPresenterImpl(Context context, SearchIngredientView view) {
        this.view = view;
        this.mealsRepo = new MealsRepo(context);
    }

    @Override
    public void getIngredients() {
        view.toggleLoading(true);
        mealsRepo.listIngredients(new GeneralResponse<List<IngredientDto>>() {
            @Override
            public void onSuccess(List<IngredientDto> data) {
                view.toggleLoading(false);
                allIngredients = data;
                view.showIngredients(data);
            }

            @Override
            public void onError(String errorMessage) {
                view.toggleLoading(false);
                view.showError(errorMessage);
            }
        });
    }

    @Override
    public void searchIngredients(String query) {
        if (query == null || query.trim().isEmpty()) {
            view.showIngredients(allIngredients);
            return;
        }

        List<IngredientDto> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase().trim();

        for (IngredientDto ingredient : allIngredients) {
            if (ingredient.getName() != null && ingredient.getName().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(ingredient);
            }
        }

        view.showIngredients(filteredList);
    }
}
