package com.mealify.mealify.data.meals.model.filteredmeals;

public enum FilterType {
    INGREDIENT("i"),
    CATEGORY("c"),
    AREA("a");

    private final String queryKey;

    FilterType(String queryKey) {
        this.queryKey = queryKey;
    }

    public String getQueryKey() {
        return queryKey;
    }
}