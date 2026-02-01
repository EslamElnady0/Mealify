package com.mealify.mealify.presentation.plan.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.mealify.mealify.InnerAppFragmentDirections;
import com.mealify.mealify.R;
import com.mealify.mealify.core.helper.CustomSnackbar;
import com.mealify.mealify.core.utils.DialogUtils;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealType;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealWithMeal;
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
    private View guestContainer;
    private MaterialButton loginBtn;
    private View calendarIcon;
    private View weeklyPlanLabel;
    private View scrollView;

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
        guestContainer = view.findViewById(R.id.guest_container);
        loginBtn = view.findViewById(R.id.login_btn);
        calendarIcon = view.findViewById(R.id.imageView);
        weeklyPlanLabel = view.findViewById(R.id.weekly_plan_text);
        scrollView = view.findViewById(R.id.nestedScrollView);

        if (loginBtn != null) {
            loginBtn.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(getContext(), com.mealify.mealify.presentation.auth.AuthActivity.class);
                startActivity(intent);
            });
        }
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
        if (selectedDayText != null) {
            selectedDayText.setText(formattedDate);
        }
    }

    @Override
    public void showMeals(List<WeeklyPlanMealWithMeal> meals) {
        updateUIWithMeals(meals);
    }

    @Override
    public void showError(String message) {
        CustomSnackbar.showFailure(getView(), message);
    }

    @Override
    public void onDeleteSuccess(String message) {
        CustomSnackbar.showSuccess(getView(), message);
    }

    @Override
    public void showPlannedDates(List<String> dates) {

    }

    @Override
    public void setGuestMode(boolean isGuest) {
        if (guestContainer != null)
            guestContainer.setVisibility(isGuest ? View.VISIBLE : View.GONE);
        if (calendarView != null) calendarView.setVisibility(isGuest ? View.GONE : View.VISIBLE);
        if (selectedDayText != null)
            selectedDayText.setVisibility(isGuest ? View.GONE : View.VISIBLE);
        if (calendarIcon != null) calendarIcon.setVisibility(isGuest ? View.GONE : View.VISIBLE);
        if (weeklyPlanLabel != null)
            weeklyPlanLabel.setVisibility(isGuest ? View.GONE : View.VISIBLE);
        if (scrollView != null) scrollView.setVisibility(isGuest ? View.GONE : View.VISIBLE);
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
                        breakFastSlotFilled(meal);
                        breakfast = meal;
                        break;
                    case LUNCH:
                        lunchSlotFilled(meal);
                        lunch = meal;
                        break;
                    case DINNER:
                        dinnerSlotFilled(meal);
                        dinner = meal;
                        break;
                    case SNACK:
                        snacks.add(meal);
                        break;
                }
            }
        }

        if (breakfast == null) setSlotEmpty(breakfastSlot);
        if (lunch == null) setSlotEmpty(lunchSlot);
        if (dinner == null) setSlotEmpty(dinnerSlot);

        snacksAdapter.setSnacks(snacks);
    }

    private void breakFastSlotFilled(WeeklyPlanMealWithMeal meal) {
        setSlotFilled(breakfastSlot, meal);
    }

    private void lunchSlotFilled(WeeklyPlanMealWithMeal meal) {
        setSlotFilled(lunchSlot, meal);
    }

    private void dinnerSlotFilled(WeeklyPlanMealWithMeal meal) {
        setSlotFilled(dinnerSlot, meal);
    }

    private void setAllSlotsEmpty() {
        if (breakfastSlot != null) setSlotEmpty(breakfastSlot);
        if (lunchSlot != null) setSlotEmpty(lunchSlot);
        if (dinnerSlot != null) setSlotEmpty(dinnerSlot);
    }

    private void setSlotEmpty(View slotView) {
        if (slotView == null) return;
        TextView emptyText = slotView.findViewById(R.id.empty_text);
        MaterialButton selectButton = slotView.findViewById(R.id.select_button);
        LinearLayout filledContent = slotView.findViewById(R.id.filled_content);
        MaterialCardView mealImageCard = slotView.findViewById(R.id.meal_image);

        if (emptyText != null) emptyText.setVisibility(View.VISIBLE);
        if (selectButton != null) {
            selectButton.setVisibility(View.VISIBLE);
            selectButton.setOnClickListener(v -> openMealRedirectionDialog());
        }

        if (filledContent != null) filledContent.setVisibility(View.GONE);
        if (mealImageCard != null) mealImageCard.setVisibility(View.GONE);
    }

    private void setSlotFilled(View slotView, WeeklyPlanMealWithMeal mealWithPlan) {
        if (slotView == null) return;
        TextView emptyText = slotView.findViewById(R.id.empty_text);
        MaterialButton selectButton = slotView.findViewById(R.id.select_button);
        LinearLayout filledContent = slotView.findViewById(R.id.filled_content);
        MaterialCardView mealImageCard = slotView.findViewById(R.id.meal_image);
        ImageView mealImage = slotView.findViewById(R.id.meal_image_view);
        TextView mealName = slotView.findViewById(R.id.meal_name);
        TextView mealDetails = slotView.findViewById(R.id.meal_details);
        MaterialButton removeButton = slotView.findViewById(R.id.remove_button);

        if (emptyText != null) emptyText.setVisibility(View.GONE);
        if (selectButton != null) selectButton.setVisibility(View.GONE);

        if (mealImageCard != null) mealImageCard.setVisibility(View.VISIBLE);
        if (filledContent != null) {
            filledContent.setVisibility(View.VISIBLE);
            filledContent.setOnClickListener(v -> {
                if (mealWithPlan.meal != null) openMealDetails(mealWithPlan.meal.getId());
            });
        }

        if (mealWithPlan.meal != null) {
            if (mealName != null) mealName.setText(mealWithPlan.meal.getName());

            if (mealDetails != null) {
                String details = mealWithPlan.meal.getCategory();
                mealDetails.setText(details);
            }

            if (mealImage != null) {
                Glide.with(this)
                        .load(mealWithPlan.meal.getThumbnail())
                        .placeholder(R.drawable.mealify_logo)
                        .error(R.drawable.mealify_logo)
                        .into(mealImage);
            }

            if (removeButton != null) {
                removeButton.setOnClickListener(v -> {
                    if (mealWithPlan.planEntry != null) {
                        DialogUtils.showConfirmationDialog(
                                getContext(),
                                mealWithPlan.meal.getThumbnail(),
                                getString(R.string.remove_from_plan),
                                getString(R.string.are_you_sure_you_want_to_remove_this_meal_from_your_plan),
                                new DialogUtils.DialogCallback() {
                                    @Override
                                    public void onConfirm() {
                                        presenter.deleteMealFromPlan(Long.parseLong(mealWithPlan.planEntry.getMealId()));
                                    }

                                    @Override
                                    public void onCancel() {
                                    }
                                }
                        );
                    }
                });
            }
        }
    }

    private void openMealRedirectionDialog() {
        DialogUtils.showAddMealOptionDialog(getContext(), new DialogUtils.AddMealOptionCallback() {
            @Override
            public void onGoToFavorites() {
                BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottomNavBar);
                if (bottomNav != null) {
                    bottomNav.setSelectedItemId(R.id.favouritesFragment);
                }
            }

            @Override
            public void onGoToSearch() {
                BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottomNavBar);
                if (bottomNav != null) {
                    bottomNav.setSelectedItemId(R.id.searchFragment);
                }
            }
        });
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
    public void onRemoveSnack(WeeklyPlanMealWithMeal snack) {
        if (snack.meal != null && snack.planEntry != null) {
            DialogUtils.showConfirmationDialog(
                    getContext(),
                    snack.meal.getThumbnail(),
                    getString(R.string.remove_from_plan),
                    getString(R.string.are_you_sure_you_want_to_remove_this_meal_from_your_plan),
                    new DialogUtils.DialogCallback() {
                        @Override
                        public void onConfirm() {
                            presenter.deleteMealFromPlan(Long.parseLong(snack.planEntry.getMealId()));
                        }

                        @Override
                        public void onCancel() {
                        }
                    }
            );
        }
    }

    @Override
    public void onSnackClick(String mealId) {
        openMealDetails(mealId);
    }

    @Override
    public void onAddMealClick() {
        openMealRedirectionDialog();
    }
}