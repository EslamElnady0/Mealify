package com.mealify.mealify.presentation.search.views.area;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mealify.mealify.R;
import com.mealify.mealify.data.meals.model.country.CountryDto;

import java.util.ArrayList;
import java.util.List;

public class SearchAreaAdapter extends RecyclerView.Adapter<SearchAreaAdapter.AreaViewHolder> {

    private List<CountryDto> areas = new ArrayList<>();
    private OnAreaClickListener listener;

    public interface OnAreaClickListener {
        void onAreaClick(CountryDto area);
    }

    public void setOnAreaClickListener(OnAreaClickListener listener) {
        this.listener = listener;
    }

    public void setAreas(List<CountryDto> areas) {
        this.areas = areas != null ? areas : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_area, parent, false);
        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        CountryDto area = areas.get(position);
        holder.bind(area);
    }

    @Override
    public int getItemCount() {
        return areas.size();
    }

    class AreaViewHolder extends RecyclerView.ViewHolder {
        private final ImageView areaFlag;
        private final TextView areaName;

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            areaFlag = itemView.findViewById(R.id.areaFlag);
            areaName = itemView.findViewById(R.id.areaName);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onAreaClick(areas.get(position));
                }
            });
        }

        public void bind(CountryDto area) {
            areaName.setText(area.getStrArea());

            Glide.with(itemView.getContext())
                    .load(area.getFlagUrl())
                    .placeholder(R.drawable.mealify_logo)
                    .into(areaFlag);
        }
    }
}
