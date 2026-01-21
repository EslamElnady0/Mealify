package com.mealify.mealify.data.meals.model.country;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountriesResponse {
   @SerializedName("meals") private List<CountryDto> countries;

    public CountriesResponse(List<CountryDto> meals) {
        this.countries = meals;
    }

    public List<CountryDto> getMeals() {
        return countries;
    }
}