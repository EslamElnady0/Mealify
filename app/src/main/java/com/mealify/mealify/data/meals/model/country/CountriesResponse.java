package com.mealify.mealify.data.meals.model.country;

import java.util.List;

public class CountriesResponse {
    private List<CountryDto> meals;

    public CountriesResponse(List<CountryDto> meals) {
        this.meals = meals;
    }

    public List<CountryDto> getMeals() {
        return meals;
    }
}