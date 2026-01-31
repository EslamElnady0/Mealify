package com.mealify.mealify.presentation.search.views.category;

import com.mealify.mealify.data.models.category.CategoryDto;

import java.util.List;

public interface SearchCategoryView {
    void showCategories(List<CategoryDto> categories);

    void showError(String message);

    void toggleLoading(boolean isLoading);
}
