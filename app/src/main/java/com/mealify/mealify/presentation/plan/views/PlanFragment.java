package com.mealify.mealify.presentation.plan.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.mealify.mealify.R;

public class PlanFragment extends Fragment {

    private View breakfastSlot;
    private View lunchSlot;
    private View dinnerSlot;

    public PlanFragment() {
    }

    public static PlanFragment newInstance(String param1, String param2) {
        PlanFragment fragment = new PlanFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        initializeViews(view);
        setAllSlotsEmpty();

        return view;
    }

    private void initializeViews(View view) {
        breakfastSlot = view.findViewById(R.id.breakfast_slot);
        lunchSlot = view.findViewById(R.id.lunch_slot);
        dinnerSlot = view.findViewById(R.id.dinner_slot);
    }

    private void setAllSlotsEmpty() {
        setSlotEmpty(breakfastSlot, "breakfast");
        setSlotEmpty(lunchSlot, "lunch");
        setSlotEmpty(dinnerSlot, "dinner");
    }

    private void setSlotEmpty(View slotView, String mealType) {
        TextView emptyText = slotView.findViewById(R.id.empty_text);
        MaterialButton selectButton = slotView.findViewById(R.id.select_button);
        LinearLayout filledContent = slotView.findViewById(R.id.filled_content);
        MaterialCardView mealImageCard = slotView.findViewById(R.id.meal_image);

        emptyText.setVisibility(View.VISIBLE);
        selectButton.setVisibility(View.VISIBLE);

        filledContent.setVisibility(View.GONE);
        mealImageCard.setVisibility(View.GONE);

        selectButton.setOnClickListener(v -> {
            openMealSelection(mealType);
        });
    }

    private void openMealSelection(String mealType) {
    }
}