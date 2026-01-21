package com.mealify.mealify.data.meals.mapper;

import com.mealify.mealify.data.meals.model.meal.MealDto;
import com.mealify.mealify.data.meals.model.meal.MealEntity;

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
        entity.setIngredients(dto.getIngredients());

        return entity;
    }
}