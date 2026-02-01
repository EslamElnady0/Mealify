package com.mealify.mealify.data.models.meal;

public class Ingredient {
    public String name;
    public String measure;

    public Ingredient() {
    }

    public Ingredient(String name, String measure) {
        this.name = name;
        this.measure = measure;
    }
}
