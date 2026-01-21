package com.mealify.mealify.data.meals.model.country;

import com.google.gson.annotations.SerializedName;

public class CountryDto {
    @SerializedName("strArea")
    private String area;

        public CountryDto(String strArea) {
            this.area = strArea;
        }

        public String getStrArea() {
            return area;
        }

        public void setStrArea(String strArea) {
            this.area = strArea;
        }
}
