package com.mealify.mealify.presentation.search.views.area;

import com.mealify.mealify.data.models.country.CountryDto;

import java.util.List;

public interface SearchAreaView {
    void showAreas(List<CountryDto> areas);

    void showError(String message);

    void toggleLoading(boolean isLoading);
}
