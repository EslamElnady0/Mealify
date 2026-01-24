package com.mealify.mealify.data.meals.model.filteredmeals;

public enum FilterType {
    INGREDIENT("i", "ingredient"),
    CATEGORY("c", "category"),
    AREA("a", "area");

    private final String queryKey;
    private final String filterName;

    FilterType(String queryKey, String filterName) {
        this.queryKey = queryKey;
        this.filterName = filterName;
    }

    public String getQueryKey() {
        return queryKey;
    }

    public String getFilterName() {
        return filterName;
    }
}