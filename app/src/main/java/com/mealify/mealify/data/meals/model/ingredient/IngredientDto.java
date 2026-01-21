package com.mealify.mealify.data.meals.model.ingredient;

import com.google.gson.annotations.SerializedName;

public class IngredientDto {

    @SerializedName("idIngredient")
    private String id;

    @SerializedName("strIngredient")
    private String name;

    @SerializedName("strDescription")
    private String description;

    @SerializedName("strThumb")
    private String thumbnailUrl;

    @SerializedName("strType")
    private String type;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getType() {
        return type;
    }
}
