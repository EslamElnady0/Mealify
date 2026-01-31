package com.mealify.mealify.presentation.search.views.ingredient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mealify.mealify.R;
import com.mealify.mealify.data.meals.model.ingredient.IngredientDto;

import java.util.ArrayList;
import java.util.List;

public class SearchIngredientAdapter extends RecyclerView.Adapter<SearchIngredientAdapter.IngredientViewHolder> {

    private List<IngredientDto> ingredients = new ArrayList<>();
    private OnIngredientClickListener listener;

    public interface OnIngredientClickListener {
        void onIngredientClick(IngredientDto ingredient);
    }

    public void setOnIngredientClickListener(OnIngredientClickListener listener) {
        this.listener = listener;
    }

    public void setIngredients(List<IngredientDto> ingredients) {
        this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        IngredientDto ingredient = ingredients.get(position);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ingredientImage;
        private final TextView ingredientName;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.ingredientImage);
            ingredientName = itemView.findViewById(R.id.ingredientName);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onIngredientClick(ingredients.get(position));
                }
            });
        }

        public void bind(IngredientDto ingredient) {
            ingredientName.setText(ingredient.getName());

            String imageUrl = "https://www.themealdb.com/images/ingredients/" + ingredient.getName() + "-Small.png";

            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.shimmer_circle)
                    .into(ingredientImage);
        }
    }
}
