package com.mealify.mealify.presentation.search.views.name;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.mealify.mealify.InnerAppFragmentDirections;
import com.mealify.mealify.R;
import com.mealify.mealify.core.helper.CustomSnackbar;
import com.mealify.mealify.data.models.filteredmeals.FilteredMeal;
import com.mealify.mealify.presentation.search.presenter.name.SearchNamePresenter;
import com.mealify.mealify.presentation.search.presenter.name.SearchNamePresenterImpl;
import com.mealify.mealify.presentation.search.views.searchresults.MealSearchResultAdapter;

import java.util.List;

public class SearchByNameFragment extends Fragment implements SearchNameView {

    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private SearchNamePresenter presenter;
    private MealSearchResultAdapter adapter;
    private LinearLayout shimmerViewContainer;
    private RecyclerView recyclerView;
    private TextInputEditText searchEditText;
    private View infoCard;
    private View noResultsLayout;
    private Runnable searchRunnable;

    public SearchByNameFragment() {
    }

    public static SearchByNameFragment newInstance() {
        return new SearchByNameFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_by_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        setupPresenter();
        setupSearchListener();
    }

    private void initViews(View view) {
        shimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        recyclerView = view.findViewById(R.id.resultsRecyclerView);
        searchEditText = view.findViewById(R.id.search_edit_text);
        infoCard = view.findViewById(R.id.info_card);
        noResultsLayout = view.findViewById(R.id.no_results_layout);
    }

    private void setupRecyclerView() {
        adapter = new MealSearchResultAdapter(meal -> {
            int mealId = Integer.parseInt(meal.getIdMeal());
            var action = InnerAppFragmentDirections
                    .actionInnerAppFragmentToMealDetailsFragment(mealId);

            Fragment innerAppFragment = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentById(R.id.inner_home_container);

            if (innerAppFragment != null) {
                NavController navController = NavHostFragment.findNavController(innerAppFragment);
                navController.navigate(action);
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(adapter);
    }

    private void setupPresenter() {
        presenter = new SearchNamePresenterImpl(requireContext(), this);
    }

    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    showInitialState();
                    return;
                }

                searchRunnable = () -> presenter.searchMealsByName(query);
                searchHandler.postDelayed(searchRunnable, 500);
            }
        });
    }

    private void showInitialState() {
        infoCard.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        noResultsLayout.setVisibility(View.GONE);
        shimmerViewContainer.clearAnimation();
        shimmerViewContainer.setVisibility(View.GONE);
        adapter.setMeals(null);
    }

    @Override
    public void toggleLoading(boolean isLoading) {
        if (isLoading) {
            shimmerViewContainer.setVisibility(View.VISIBLE);
            Animation shimmer = AnimationUtils.loadAnimation(requireContext(), R.anim.shimmer_fade);
            shimmerViewContainer.startAnimation(shimmer);
        } else {
            shimmerViewContainer.clearAnimation();
            shimmerViewContainer.setVisibility(View.GONE);
        }
        if (isLoading) {
            infoCard.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            noResultsLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMeals(List<FilteredMeal> meals) {
        if (meals == null || meals.isEmpty()) {
            infoCard.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            noResultsLayout.setVisibility(View.VISIBLE);
            return;
        }

        infoCard.setVisibility(View.GONE);
        noResultsLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        adapter.setMeals(meals);
    }

    @Override
    public void showError(String message) {
        CustomSnackbar.showFailure(getView(), message);
        noResultsLayout.setVisibility(View.VISIBLE);
        infoCard.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }
    }
}
