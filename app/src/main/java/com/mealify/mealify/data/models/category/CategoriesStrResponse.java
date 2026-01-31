package com.mealify.mealify.data.models.category;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriesStrResponse {
   @SerializedName
  ("meals") private List<CategoryStrDto> categoriesStr;



    public CategoriesStrResponse(List<CategoryStrDto> meals) {
        this.categoriesStr = meals;
    }

    public List<CategoryStrDto> getCategoriesStr() {
        return categoriesStr;
    }

    public void setCategoriesStr(List<CategoryStrDto> meals) {
        this.categoriesStr = meals;
    }
}
