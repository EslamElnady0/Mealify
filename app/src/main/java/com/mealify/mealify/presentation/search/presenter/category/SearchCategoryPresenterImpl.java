package com.mealify.mealify.presentation.search.presenter.category;

import android.content.Context;

import com.mealify.mealify.core.response.ApiResponse;
import com.mealify.mealify.data.meals.model.category.CategoryDto;
import com.mealify.mealify.data.meals.repo.MealsRepo;
import com.mealify.mealify.presentation.search.views.category.SearchCategoryView;

import java.util.ArrayList;
import java.util.List;

public class SearchCategoryPresenterImpl implements SearchCategoryPresenter {

    private final SearchCategoryView view;
    private final MealsRepo mealsRepo;
    private List<CategoryDto> allCategories = new ArrayList<>();

    public SearchCategoryPresenterImpl(Context context, SearchCategoryView view) {
        this.view = view;
        this.mealsRepo = new MealsRepo(context);
    }

    @Override
    public void getCategories() {
        view.toggleLoading(true);
        mealsRepo.getCategories(new ApiResponse<List<CategoryDto>>() {
            @Override
            public void onSuccess(List<CategoryDto> data) {
                view.toggleLoading(false);
                allCategories = data;
                view.showCategories(data);
            }

            @Override
            public void onError(String errorMessage) {
                view.toggleLoading(false);
                view.showError(errorMessage);
            }
        });
    }

    @Override
    public void searchCategories(String query) {
        if (query == null || query.trim().isEmpty()) {
            view.showCategories(allCategories);
            return;
        }

        List<CategoryDto> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase().trim();

        for (CategoryDto category : allCategories) {
            if (category.name != null && category.name.toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(category);
            }
        }

        view.showCategories(filteredList);
    }
}
