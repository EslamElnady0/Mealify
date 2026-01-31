package com.mealify.mealify.presentation.search.views.searchresults;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.mealify.mealify.R;
import com.mealify.mealify.core.helper.CustomToast;
import com.mealify.mealify.data.models.filteredmeals.FilterType;
import com.mealify.mealify.data.models.filteredmeals.FilteredMeal;
import com.mealify.mealify.presentation.search.presenter.searchresults.MealSearchResultPresenter;
import com.mealify.mealify.presentation.search.presenter.searchresults.MealSearchResultPresenterImpl;

import java.util.List;

public class MealSearchResultFragment extends Fragment implements MealSearchResultView {

    private FilterType filterType;
    private String query;
    private MealSearchResultPresenter presenter;
    private MealSearchResultAdapter adapter;
    private LinearLayout shimmerViewContainer;
    private TextView headerText;
    private RecyclerView recyclerView;
    private TextInputEditText searchEditText;

    public MealSearchResultFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            MealSearchResultFragmentArgs args = MealSearchResultFragmentArgs.fromBundle(getArguments());
            filterType = args.getFilterType();
            query = args.getQuery();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_search_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        setupPresenter();
        setupSearchListener();

        updateHeader();
        fetchResults();
    }

    private void initViews(View view) {
        shimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        headerText = view.findViewById(R.id.result_header_text);
        recyclerView = view.findViewById(R.id.resultsRecyclerView);
        searchEditText = view.findViewById(R.id.search_edit_text);

        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.inner_home_container);
            NavController navController = navHostFragment.getNavController();
            navController.navigateUp();
        });
    }

    private void setupRecyclerView() {
        adapter = new MealSearchResultAdapter(meal -> {
            int mealId = Integer.parseInt(meal.getIdMeal());
            MealSearchResultFragmentDirections.ActionMealSearchResultFragmentToMealDetailsFragment action = MealSearchResultFragmentDirections
                    .actionMealSearchResultFragmentToMealDetailsFragment(mealId);
            NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.inner_home_container);
            NavController navController = navHostFragment.getNavController();
            navController.navigate(action);
        });
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(adapter);
    }

    private void setupPresenter() {
        presenter = new MealSearchResultPresenterImpl(requireContext(), this);
    }

    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.searchMeals(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void updateHeader() {
        if (filterType != null && query != null) {
            String header = filterType.getFilterName() + " : " + query;
            headerText.setText(header);
        }
    }

    private void fetchResults() {
        if (filterType != null && query != null) {
            presenter.getFilteredMeals(filterType, query);
        }
    }

    @Override
    public void showSearchResults(List<FilteredMeal> meals) {
        adapter.setMeals(meals);
    }

    @Override
    public void showError(String errorMessage) {
        CustomToast.show(requireContext(), errorMessage);
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
        recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }
}