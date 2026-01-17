package com.mealify.mealify.features.home.data.model.meal;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class MealDto {

    @SerializedName("idMeal")
    public String id;

    @SerializedName("strMeal")
    public String name;

    @SerializedName("strCategory")
    public String category;

    @SerializedName("strArea")
    public String area;

    @SerializedName("strInstructions")
    public String instructions;

    @SerializedName("strMealThumb")
    public String thumbnail;

    @SerializedName("strIngredient1")  public String ingredient1;
    @SerializedName("strIngredient2")  public String ingredient2;
    @SerializedName("strIngredient3")  public String ingredient3;
    @SerializedName("strIngredient4")  public String ingredient4;
    @SerializedName("strIngredient5")  public String ingredient5;
    @SerializedName("strIngredient6")  public String ingredient6;
    @SerializedName("strIngredient7")  public String ingredient7;
    @SerializedName("strIngredient8")  public String ingredient8;
    @SerializedName("strIngredient9")  public String ingredient9;
    @SerializedName("strIngredient10") public String ingredient10;
    @SerializedName("strIngredient11") public String ingredient11;
    @SerializedName("strIngredient12") public String ingredient12;
    @SerializedName("strIngredient13") public String ingredient13;
    @SerializedName("strIngredient14") public String ingredient14;
    @SerializedName("strIngredient15") public String ingredient15;
    @SerializedName("strIngredient16") public String ingredient16;
    @SerializedName("strIngredient17") public String ingredient17;
    @SerializedName("strIngredient18") public String ingredient18;
    @SerializedName("strIngredient19") public String ingredient19;
    @SerializedName("strIngredient20") public String ingredient20;

    @SerializedName("strMeasure1")  public String measure1;
    @SerializedName("strMeasure2")  public String measure2;
    @SerializedName("strMeasure3")  public String measure3;
    @SerializedName("strMeasure4")  public String measure4;
    @SerializedName("strMeasure5")  public String measure5;
    @SerializedName("strMeasure6")  public String measure6;
    @SerializedName("strMeasure7")  public String measure7;
    @SerializedName("strMeasure8")  public String measure8;
    @SerializedName("strMeasure9")  public String measure9;
    @SerializedName("strMeasure10") public String measure10;
    @SerializedName("strMeasure11") public String measure11;
    @SerializedName("strMeasure12") public String measure12;
    @SerializedName("strMeasure13") public String measure13;
    @SerializedName("strMeasure14") public String measure14;
    @SerializedName("strMeasure15") public String measure15;
    @SerializedName("strMeasure16") public String measure16;
    @SerializedName("strMeasure17") public String measure17;
    @SerializedName("strMeasure18") public String measure18;
    @SerializedName("strMeasure19") public String measure19;
    @SerializedName("strMeasure20") public String measure20;

    public List<Ingredient> getIngredients() {
        List<Ingredient> list = new ArrayList<>();

        addIngredient(list, ingredient1, measure1);
        addIngredient(list, ingredient2, measure2);
        addIngredient(list, ingredient3, measure3);
        addIngredient(list, ingredient4, measure4);
        addIngredient(list, ingredient5, measure5);
        addIngredient(list, ingredient6, measure6);
        addIngredient(list, ingredient7, measure7);
        addIngredient(list, ingredient8, measure8);
        addIngredient(list, ingredient9, measure9);
        addIngredient(list, ingredient10, measure10);
        addIngredient(list, ingredient11, measure11);
        addIngredient(list, ingredient12, measure12);
        addIngredient(list, ingredient13, measure13);
        addIngredient(list, ingredient14, measure14);
        addIngredient(list, ingredient15, measure15);
        addIngredient(list, ingredient16, measure16);
        addIngredient(list, ingredient17, measure17);
        addIngredient(list, ingredient18, measure18);
        addIngredient(list, ingredient19, measure19);
        addIngredient(list, ingredient20, measure20);

        return list;
    }

    private void addIngredient(List<Ingredient> list, String name, String measure) {
        if (name != null && !name.trim().isEmpty()) {
            list.add(new Ingredient(
                    name.trim(),
                    measure != null ? measure.trim() : ""
            ));
        }
    }
}
