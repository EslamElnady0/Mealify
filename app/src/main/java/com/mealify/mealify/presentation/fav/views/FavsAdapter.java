package com.mealify.mealify.presentation.fav.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mealify.mealify.R;
import com.mealify.mealify.data.favs.model.fav.FavouriteWithMeal;

import java.util.ArrayList;
import java.util.List;

public class FavsAdapter extends RecyclerView.Adapter<FavsAdapter.FavViewHolder> {

    private List<FavouriteWithMeal> favs = new ArrayList<>();
    private final OnFavClickListener clickListener;
    private final OnFavRemoveListener removeListener;

    public interface OnFavClickListener {
        void onFavClick(FavouriteWithMeal fav);
    }

    public interface OnFavRemoveListener {
        void onFavRemove(FavouriteWithMeal fav);
    }

    public FavsAdapter(OnFavClickListener clickListener, OnFavRemoveListener removeListener) {
        this.clickListener = clickListener;
        this.removeListener = removeListener;
    }

    public void setFavs(List<FavouriteWithMeal> favs) {
        this.favs = favs != null ? favs : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favourite_meal, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        holder.bind(favs.get(position));
    }

    @Override
    public int getItemCount() {
        return favs.size();
    }

    class FavViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mealImage;
        private final TextView mealName;
        private final ImageButton favBtn;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImage = itemView.findViewById(R.id.meal_image);
            mealName = itemView.findViewById(R.id.meal_name);
            favBtn = itemView.findViewById(R.id.fav_btn);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener.onFavClick(favs.get(position));
                }
            });

            if (favBtn != null) {
                favBtn.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && removeListener != null) {
                        removeListener.onFavRemove(favs.get(position));
                    }
                });
            }
        }

        public void bind(FavouriteWithMeal fav) {
            if (fav.meal != null) {
                mealName.setText(fav.meal.getName());
                Glide.with(itemView.getContext())
                        .load(fav.meal.getThumbnail())
                        .placeholder(R.drawable.mealify_logo)
                        .into(mealImage);
            }
        }
    }
}
