package com.mealify.mealify.presentation.search.presenter.ingredient;

import android.annotation.SuppressLint;
import android.content.Context;

import com.mealify.mealify.data.models.ingredient.IngredientDto;
import com.mealify.mealify.data.repos.meals.MealsRepo;
import com.mealify.mealify.presentation.search.views.ingredient.SearchIngredientView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class SearchIngredientPresenterImpl implements SearchIngredientPresenter {

    private final SearchIngredientView view;
    private final MealsRepo mealsRepo;
    private List<IngredientDto> allIngredients = new ArrayList<>();

    public SearchIngredientPresenterImpl(Context context, SearchIngredientView view) {
        this.view = view;
        this.mealsRepo = new MealsRepo(context);
    }

    @Override
    @SuppressLint("CheckResult")
    public void getIngredients() {
        view.toggleLoading(true);
        mealsRepo.listIngredients()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> {
                            view.toggleLoading(false);
                            allIngredients = data;
                            view.showIngredients(data);
                        },
                        error -> {
                            view.toggleLoading(false);
                            view.showError(error.getMessage());
                        }
                );
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
