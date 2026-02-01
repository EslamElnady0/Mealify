package com.mealify.mealify.presentation.meals.views;

import android.Manifest;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mealify.mealify.R;
import com.mealify.mealify.core.helper.CalendarHelper;
import com.mealify.mealify.core.helper.CustomSnackbar;
import com.mealify.mealify.core.utils.DialogUtils;
import com.mealify.mealify.core.utils.MealDetailsUtils;
import com.mealify.mealify.data.datasources.auth.remote.services.FirebaseAuthService;
import com.mealify.mealify.data.models.meal.MealEntity;
import com.mealify.mealify.data.models.weeklyplan.DayOfWeek;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealEntity;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealType;
import com.mealify.mealify.data.models.weeklyplan.WeeklyPlanMealWithMeal;
import com.mealify.mealify.presentation.meals.presenter.MealDetailsPresenter;
import com.mealify.mealify.presentation.meals.presenter.MealDetailsPresenterImpl;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.time.LocalDate;
import java.util.Calendar;

public class MealDetailsFragment extends Fragment implements MealDetailsView {

    private ProgressBar progressBar;
    private NestedScrollView contentScrollView;
    private TextView errorText;
    private ImageView mealImageView;
    private TextView areaText;
    private TextView mealNameText;
    private TextView categoryText;
    private TextView ingCountText;
    private RecyclerView ingredientsRecycler;
    private RecyclerView stepsRecycler;
    private YouTubePlayerView youtubePlayerView;
    private ImageButton backButton;
    private ImageButton favButton;
    private ImageButton weeklyPlanBtn;
    private IngredientAdapter ingredientAdapter;
    private StepAdapter stepAdapter;
    private MealDetailsPresenter presenter;
    private int mealId;
    private MealEntity currentMeal;
    private boolean isFavorite = false;
    private View offlineContainer;
    
    private CalendarHelper calendarHelper;
    private ActivityResultLauncher<String[]> calendarPermissionLauncher;
    private String pendingMealName;
    private String pendingMealDate;

    public MealDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mealId = MealDetailsFragmentArgs.fromBundle(getArguments()).getMealId();
        }
        
        calendarHelper = new CalendarHelper(requireContext());
        
        calendarPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean readGranted = result.get(Manifest.permission.READ_CALENDAR);
                    Boolean writeGranted = result.get(Manifest.permission.WRITE_CALENDAR);
                    
                    if (readGranted != null && writeGranted != null && readGranted && writeGranted) {
                        if (pendingMealName != null && pendingMealDate != null) {
                            calendarHelper.addMealToSystemCalendar(pendingMealName, pendingMealDate);
                            pendingMealName = null;
                            pendingMealDate = null;
                        }
                    } else {
                        Toast.makeText(getContext(), 
                                "Calendar permission denied. Meal saved to plan only.", 
                                Toast.LENGTH_SHORT).show();
                        pendingMealName = null;
                        pendingMealDate = null;
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_details, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupRecyclerView();

        presenter = new MealDetailsPresenterImpl(getContext(), this);
        presenter.startNetworkMonitoring(String.valueOf(mealId));

        backButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });
        favButton.setOnClickListener(v -> {
            FirebaseAuthService authService =
                    FirebaseAuthService.getInstance(getContext());
            if (authService.getCurrentUser() != null && authService.getCurrentUser().isAnonymous()) {
                DialogUtils.showGuestLoginDialog(getContext());
                return;
            }
            if (currentMeal != null) {
                if (isFavorite) {
                    DialogUtils.showConfirmationDialog(
                            getContext(),
                            currentMeal.getThumbnail(),
                            getString(R.string.remove_from_favorites),
                            getString(R.string.are_you_sure_you_want_to_remove_this_meal_from_your_favorites),
                            new DialogUtils.DialogCallback() {
                                @Override
                                public void onConfirm() {
                                    presenter.toggleFavorite(currentMeal);
                                }

                                @Override
                                public void onCancel() {
                                }
                            }
                    );
                } else {
                    presenter.toggleFavorite(currentMeal);
                }
            }
        });
        weeklyPlanBtn.setOnClickListener(v -> {
            FirebaseAuthService authService =
                    FirebaseAuthService.getInstance(getContext());
            if (authService.getCurrentUser() != null && authService.getCurrentUser().isAnonymous()) {
                DialogUtils.showGuestLoginDialog(getContext());
                return;
            }
            if (currentMeal != null) {
                showDatePicker();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    private void initViews(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        errorText = view.findViewById(R.id.errorText);
        contentScrollView = view.findViewById(R.id.contentScrollView);
        offlineContainer = view.findViewById(R.id.offlineContainer);

        // Header views
        View header = view.findViewById(R.id.mealHeader);
        mealImageView = header.findViewById(R.id.mealImageView);
        areaText = header.findViewById(R.id.area_text);
        mealNameText = header.findViewById(R.id.meal_name);
        categoryText = header.findViewById(R.id.category_text);
        backButton = view.findViewById(R.id.back_button);
        favButton = view.findViewById(R.id.fav_btn);
        weeklyPlanBtn = view.findViewById(R.id.weekly_plan_btn);

        ingCountText = view.findViewById(R.id.ing_count);
        ingredientsRecycler = view.findViewById(R.id.ingredientsRecycler);
        stepsRecycler = view.findViewById(R.id.stepsRecycler);
        youtubePlayerView = view.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youtubePlayerView);
    }

    private void setupRecyclerView() {
        ingredientAdapter = new IngredientAdapter();
        ingredientsRecycler.setAdapter(ingredientAdapter);

        stepAdapter = new StepAdapter();
        stepsRecycler.setAdapter(stepAdapter);
    }

    @Override
    public void toggleOffline(boolean isOffline) {
        if (isOffline) {
            offlineContainer.setVisibility(View.VISIBLE);
            contentScrollView.setVisibility(View.GONE);
            favButton.setVisibility(View.GONE);
            weeklyPlanBtn.setVisibility(View.GONE);
        } else {
            offlineContainer.setVisibility(View.GONE);
            favButton.setVisibility(View.VISIBLE);
            weeklyPlanBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void toggleLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            contentScrollView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            contentScrollView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(MealEntity mealDetails) {
        if (mealDetails != null && getContext() != null) {
            Glide.with(this).load(mealDetails.getThumbnail()).into(mealImageView);
            areaText.setText(mealDetails.getArea());
            mealNameText.setText(mealDetails.getName());
            categoryText.setText(mealDetails.getCategory());

            if (mealDetails.getInstructions() != null) {
                stepAdapter.setSteps(MealDetailsUtils.parseInstructions(mealDetails.getInstructions()));
            }

            if (mealDetails.getYoutubeUrl() != null && !mealDetails.getYoutubeUrl().isEmpty()) {
                loadYoutubeVideo(mealDetails.getYoutubeUrl());
            } else {
                youtubePlayerView.setVisibility(View.GONE);
                getView().findViewById(R.id.watch_tut_text).setVisibility(View.GONE);
            }

            if (mealDetails.getIngredients() != null) {
                ingCountText.setText(mealDetails.getIngredients().size() + " items");
                ingredientAdapter.setIngredients(mealDetails.getIngredients());
            }
            this.currentMeal = mealDetails;
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        errorText.setVisibility(View.VISIBLE);
        errorText.setText(errorMessage);
        contentScrollView.setVisibility(View.GONE);
    }

    private void loadYoutubeVideo(String videoUrl) {
        String videoId = MealDetailsUtils.extractVideoId(videoUrl);
        if (videoId != null) {
            youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youtubePlayer) {
                    youtubePlayer.cueVideo(videoId, 0);
                }
            });
        } else {
            youtubePlayerView.setVisibility(View.GONE);
            if (getView() != null) {
                getView().findViewById(R.id.watch_tut_text).setVisibility(View.GONE);
            }
        }
    }

    private void updateFavButton() {
        if (isFavorite) {
            favButton.setImageResource(R.drawable.favourite);
        } else {
            favButton.setImageResource(R.drawable.fav_24);
        }
    }

    @Override
    public void onIsFavoriteResult(boolean isFavorite) {
        this.isFavorite = isFavorite;
        updateFavButton();
    }

    @Override
    public void onToggleFavoriteSuccess(boolean isFavorite) {
        this.isFavorite = isFavorite;
        updateFavButton();
        String message = isFavorite ? "Added to favorites" : "Removed from favorites";
        CustomSnackbar.showSuccess(getView(), message);

    }

    @Override
    public void onWeeklyPlanMealAdded(String message) {
        CustomSnackbar.showSuccess(getView(), message);
        
        if (pendingMealName != null && pendingMealDate != null) {
            if (CalendarHelper.hasCalendarPermissions(requireContext())) {
                calendarHelper.addMealToSystemCalendar(pendingMealName, pendingMealDate);
                pendingMealName = null;
                pendingMealDate = null;
            } else {
                calendarPermissionLauncher.launch(new String[]{
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR
                });
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                R.style.CustomDatePickerDialogTheme,
                (view, year, month, day) -> {
                    String formattedDate = year + "-" + (month + 1) + "-" + day;
                    LocalDate localDate = LocalDate.of(year, month + 1, day);
                    DayOfWeek dayEnum = DayOfWeek.valueOf(
                            localDate.getDayOfWeek().name()
                    );
                    showMealTypeSelector(formattedDate, dayEnum);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void showMealTypeSelector(String formattedDate, DayOfWeek dayEnum) {
        String[] mealTypes = {"🍳  Breakfast", "🍔  Lunch", "🍛  Dinner", "🍿  Snack"};

        MaterialAlertDialogBuilder builder =
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.select_meal_type)
                        .setItems(mealTypes, (dialog, which) -> {
                            WeeklyPlanMealType selectedType;
                            switch (which) {
                                case 0:
                                    selectedType = WeeklyPlanMealType.BREAKFAST;
                                    break;
                                case 1:
                                    selectedType = WeeklyPlanMealType.LUNCH;
                                    break;
                                case 2:
                                    selectedType = WeeklyPlanMealType.DINNER;
                                    break;
                                case 3:
                                    selectedType = WeeklyPlanMealType.SNACK;
                                    break;
                                default:
                                    selectedType = WeeklyPlanMealType.BREAKFAST;
                            }

                            WeeklyPlanMealEntity planEntry = new WeeklyPlanMealEntity(
                                    currentMeal.getId(),
                                    formattedDate,
                                    dayEnum,
                                    selectedType,
                                    System.currentTimeMillis()
                            );
                            WeeklyPlanMealWithMeal meal = new WeeklyPlanMealWithMeal(
                                    currentMeal,
                                    planEntry
                            );
                            
                            // Store pending data for calendar integration
                            pendingMealName = currentMeal.getName();
                            pendingMealDate = formattedDate;
                            
                            presenter.addToWeeklyPlan(meal);
                        })
                        .setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_rounded);
    }

    @Override
    public void showReplaceConfirmation(WeeklyPlanMealWithMeal newMeal, WeeklyPlanMealWithMeal existingMeal) {
        DialogUtils.showReplaceMealDialog(
                requireContext(),
                existingMeal.meal.getName(),
                existingMeal.meal.getThumbnail(),
                new DialogUtils.DialogCallback() {
                    @Override
                    public void onConfirm() {
                        presenter.forceAddToWeeklyPlan(newMeal, existingMeal.planEntry.getMealId());
                    }

                    @Override
                    public void onCancel() {
                    }
                }
        );
    }
}