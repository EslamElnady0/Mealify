package com.mealify.mealify.presentation.plan.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.mealify.mealify.InnerAppFragmentDirections;
import com.mealify.mealify.R;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealType;
import com.mealify.mealify.data.weeklyplan.model.weeklyplan.WeeklyPlanMealWithMeal;
import com.mealify.mealify.presentation.plan.presenter.WeeklyPlanPresenter;
import com.mealify.mealify.presentation.plan.presenter.WeeklyPlanPresenterImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlanFragment extends Fragment implements PlanView, SnacksAdapter.OnSnackActionListener {

    private WeeklyPlanPresenter presenter;
    private CalendarView calendarView;
    private TextView selectedDayText;
    private String currentDateString;
    private View breakfastSlot;
    private View lunchSlot;
    private View dinnerSlot;
    private RecyclerView snacksRecyclerView;
    private SnacksAdapter snacksAdapter;

    private NavController navController;

    public PlanFragment() {
    }

    public static PlanFragment newInstance() {
        return new PlanFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new WeeklyPlanPresenterImpl(requireContext(), this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        initializeViews(view);
        setupCalendar();
        setupSnacksRecyclerView();

        setAllSlotsEmpty();
        loadMealsForCurrentDate();

        return view;
    }

    private void initializeViews(View view) {
        calendarView = view.findViewById(R.id.calendarView);
        selectedDayText = view.findViewById(R.id.selected_day_text);

        breakfastSlot = view.findViewById(R.id.breakfast_slot);
        lunchSlot = view.findViewById(R.id.lunch_slot);
        dinnerSlot = view.findViewById(R.id.dinner_slot);

        snacksRecyclerView = view.findViewById(R.id.snacks_recycler_view);
    }

    private void setupCalendar() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            long dateInMillis = calendar.getTimeInMillis();

            updateSelectedDayText(dateInMillis);
            currentDateString = formatDateToString(year, month, dayOfMonth);
            presenter.loadMealsForDate(currentDateString);
        });

        long currentTime = System.currentTimeMillis();
        updateSelectedDayText(currentTime);

        Calendar calendar = Calendar.getInstance();
        currentDateString = formatDateToString(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
    }

    private String formatDateToString(int year, int month, int dayOfMonth) {
        return year + "-" + (month + 1) + "-" + dayOfMonth;
    }

    private void setupSnacksRecyclerView() {
        snacksAdapter = new SnacksAdapter(this);
        snacksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        snacksRecyclerView.setAdapter(snacksAdapter);
    }

    private void loadMealsForCurrentDate() {
        if (currentDateString != null) {
            presenter.loadMealsForDate(currentDateString);
        }
    }

    private void updateSelectedDayText(long dateInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM d", Locale.getDefault());
        String formattedDate = sdf.format(new Date(dateInMillis));
        selectedDayText.setText(formattedDate);
    }

    @Override
    public void showMeals(LiveData<List<WeeklyPlanMealWithMeal>> mealsLiveData) {
        mealsLiveData.observe(getViewLifecycleOwner(), this::updateUIWithMeals);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void updateUIWithMeals(List<WeeklyPlanMealWithMeal> meals) {
        if (meals == null || meals.isEmpty()) {
            setAllSlotsEmpty();
            snacksAdapter.setSnacks(null);
            return;
        }

        WeeklyPlanMealWithMeal breakfast = null;
        WeeklyPlanMealWithMeal lunch = null;
        WeeklyPlanMealWithMeal dinner = null;
        List<WeeklyPlanMealWithMeal> snacks = new ArrayList<>();

        for (WeeklyPlanMealWithMeal meal : meals) {
            if (meal.planEntry != null) {
                WeeklyPlanMealType mealType = meal.planEntry.getMealType();

                switch (mealType) {
                    case BREAKFAST:
                        breakfast = meal;
                        break;
                    case LUNCH:
                        lunch = meal;
                        break;
                    case DINNER:
                        dinner = meal;
                        break;
                    case SNACK:
                        snacks.add(meal);
                        break;
                }
            }
        }

        if (breakfast != null) {
            setSlotFilled(breakfastSlot, breakfast);
        } else {
            setSlotEmpty(breakfastSlot, "breakfast");
        }

        if (lunch != null) {
            setSlotFilled(lunchSlot, lunch);
        } else {
            setSlotEmpty(lunchSlot, "lunch");
        }

        if (dinner != null) {
            setSlotFilled(dinnerSlot, dinner);
        } else {
            setSlotEmpty(dinnerSlot, "dinner");
        }

        snacksAdapter.setSnacks(snacks);
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

        selectButton.setOnClickListener(v -> openMealSelection(mealType));
    }

    private void setSlotFilled(View slotView, WeeklyPlanMealWithMeal mealWithPlan) {
        TextView emptyText = slotView.findViewById(R.id.empty_text);
        MaterialButton selectButton = slotView.findViewById(R.id.select_button);
        LinearLayout filledContent = slotView.findViewById(R.id.filled_content);
        MaterialCardView mealImageCard = slotView.findViewById(R.id.meal_image);
        ImageView mealImage = slotView.findViewById(R.id.meal_image_view);
        TextView mealName = slotView.findViewById(R.id.meal_name);
        TextView mealDetails = slotView.findViewById(R.id.meal_details);
        MaterialButton removeButton = slotView.findViewById(R.id.remove_button);

        emptyText.setVisibility(View.GONE);
        selectButton.setVisibility(View.GONE);

        mealImageCard.setVisibility(View.VISIBLE);
        filledContent.setVisibility(View.VISIBLE);

        if (mealWithPlan.meal != null) {
            mealName.setText(mealWithPlan.meal.getName());

            if (mealDetails != null) {
                String details = mealWithPlan.meal.getCategory();
                mealDetails.setText(details);
            }

            Glide.with(this)
                    .load(mealWithPlan.meal.getThumbnail())
                    .placeholder(R.drawable.mealify_logo)
                    .error(R.drawable.mealify_logo)
                    .into(mealImage);
            filledContent.setOnClickListener(v -> {
                openMealDetails(mealWithPlan.meal.getId());
            });
            removeButton.setOnClickListener(v -> {
                if (mealWithPlan.planEntry != null) {
                    presenter.deleteMealFromPlan(mealWithPlan.planEntry.getPlanId());
                }
            });
        }
    }

    private void openMealSelection(String mealType) {
    }

    private void openMealDetails(String mealId) {
        InnerAppFragmentDirections.ActionInnerAppFragmentToMealDetailsFragment action =
                InnerAppFragmentDirections.actionInnerAppFragmentToMealDetailsFragment(Integer.parseInt(mealId));
        NavHostFragment navHostFragment = (NavHostFragment)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.inner_home_container);
        navController = navHostFragment.getNavController();
        navController.navigate(action);
    }

    @Override
    public void onRemoveSnack(long planId) {
        presenter.deleteMealFromPlan(planId);
    }

    @Override
    public void onSnackClick(String mealId) {
        openMealDetails(mealId);
    }
}