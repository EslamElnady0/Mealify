package com.mealify.mealify.data.models.meal;

import com.mealify.mealify.data.models.filteredmeals.FilteredMeal;
import com.mealify.mealify.data.models.meal.MealDto;
import com.mealify.mealify.data.models.meal.MealEntity;

import java.util.ArrayList;
import java.util.List;

public class MealMapper {
    public static MealEntity toEntity(MealDto dto) {
        if (dto == null) {
            return null;
        }

        MealEntity entity = new MealEntity();
        entity.setId(dto.id);
        entity.setName(dto.name);
        entity.setCategory(dto.category);
        entity.setArea(dto.area);
        entity.setInstructions(dto.instructions);
        entity.setThumbnail(dto.thumbnail);
        entity.setYoutubeUrl(dto.strYoutube);
        entity.setIngredients(dto.getIngredients());

        return entity;
    }

    public static List<FilteredMeal> toFilteredMeals(List<MealDto> meals) {
        List<FilteredMeal> filteredMeals = new ArrayList<>();
        if (meals == null || meals.isEmpty()) {
            return filteredMeals;
        }

        for (MealDto meal : meals) {
            FilteredMeal filteredMeal = new FilteredMeal();
            filteredMeal.setStrMeal(meal.name);
            filteredMeal.setStrMealThumb(meal.thumbnail);
            filteredMeal.setIdMeal(meal.id);
            filteredMeals.add(filteredMeal);
        }
        return filteredMeals;
    }

}