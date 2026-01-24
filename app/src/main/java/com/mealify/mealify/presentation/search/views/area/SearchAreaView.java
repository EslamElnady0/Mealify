package com.mealify.mealify.presentation.search.views.area;

import com.mealify.mealify.data.meals.model.country.CountryDto;

import java.util.List;

public interface SearchAreaView {
    void showAreas(List<CountryDto> areas);

    void showError(String message);

    void toggleLoading(boolean isLoading);
}
