package com.mealify.mealify.presentation.home.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mealify.mealify.R;
import com.mealify.mealify.data.meals.model.category.CategoryDto;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<CategoryDto> categories = new ArrayList<CategoryDto>();
    private CategoryOnClickListener listener;

    public CategoryAdapter(CategoryOnClickListener listener) {
        this.listener = listener;
    }
    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryDto category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private View convertView;
        private ImageView categoryImage;
        private TextView categoryName;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            convertView = itemView;
            categoryImage = convertView.findViewById(R.id.category_image);
            categoryName = convertView.findViewById(R.id.category_name);
        }

        public void bind(CategoryDto category) {
            Glide.with(convertView).load(category.thumbnail).into(categoryImage);
            categoryName.setText(category.name);
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategoryClick(category);
                }
            });
        }
    }
}
