package com.mealify.mealify.features.home.data.model.category;

import com.google.gson.annotations.SerializedName;

public class CategoryDto {

    @SerializedName("idCategory")
    public String id;

    @SerializedName("strCategory")
    public String name;

    @SerializedName("strCategoryThumb")
    public String thumbnail;

    @SerializedName("strCategoryDescription")
    public String description;
}
