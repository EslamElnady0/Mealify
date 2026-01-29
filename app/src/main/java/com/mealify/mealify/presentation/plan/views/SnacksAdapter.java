package com.mealify.mealify.presentation.plan.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.mealify.mealify.R;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;

import java.util.ArrayList;
import java.util.List;

public class SnacksAdapter extends RecyclerView.Adapter<SnacksAdapter.SnackViewHolder> {

    private List<WeeklyPlanMealWithMeal> snacks = new ArrayList<>();
    private OnSnackActionListener listener;

    public SnacksAdapter(OnSnackActionListener listener) {
        this.listener = listener;
    }

    public void setSnacks(List<WeeklyPlanMealWithMeal> snacks) {
        this.snacks = snacks != null ? snacks : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SnackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal_slot, parent, false);
        return new SnackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SnackViewHolder holder, int position) {
        if (snacks.isEmpty()) {
            holder.bindEmpty(listener);
        } else {
            WeeklyPlanMealWithMeal snack = snacks.get(position);
            holder.bind(snack, listener);
        }
    }

    @Override
    public int getItemCount() {
        return snacks.isEmpty() ? 1 : snacks.size();
    }

    public interface OnSnackActionListener {
        void onRemoveSnack(WeeklyPlanMealWithMeal snack);

        void onSnackClick(String mealId);

        void onAddMealClick();
    }

    static class SnackViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardView;
        private TextView emptyText;
        private MaterialButton selectButton;
        private LinearLayout filledContent;
        private MaterialCardView mealImageCard;
        private ImageView mealImage;
        private TextView mealName;
        private TextView mealDetails;
        private MaterialButton removeButton;

        public SnackViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.meal_card);
            emptyText = itemView.findViewById(R.id.empty_text);
            selectButton = itemView.findViewById(R.id.select_button);
            filledContent = itemView.findViewById(R.id.filled_content);
            mealImageCard = itemView.findViewById(R.id.meal_image);
            mealImage = itemView.findViewById(R.id.meal_image_view);
            mealName = itemView.findViewById(R.id.meal_name);
            mealDetails = itemView.findViewById(R.id.meal_details);
            removeButton = itemView.findViewById(R.id.remove_button);
        }

        public void bind(WeeklyPlanMealWithMeal snack, OnSnackActionListener listener) {
            emptyText.setVisibility(View.GONE);
            selectButton.setVisibility(View.GONE);

            mealImageCard.setVisibility(View.VISIBLE);
            filledContent.setVisibility(View.VISIBLE);

            if (snack.meal != null) {
                mealName.setText(snack.meal.getName());

                String details = snack.meal.getCategory();
                if (mealDetails != null) {
                    mealDetails.setText(details);
                }

                Glide.with(itemView.getContext())
                        .load(snack.meal.getThumbnail())
                        .placeholder(R.drawable.mealify_logo)
                        .error(R.drawable.mealify_logo)
                        .into(mealImage);

                removeButton.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onRemoveSnack(snack);
                    }
                });

                cardView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onSnackClick(snack.meal.getId());
                    }
                });
            }
        }

        public void bindEmpty(OnSnackActionListener listener) {
            emptyText.setVisibility(View.VISIBLE);
            selectButton.setVisibility(View.VISIBLE);

            mealImageCard.setVisibility(View.GONE);
            filledContent.setVisibility(View.GONE);

            selectButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddMealClick();
                }
            });
        }
    }
}