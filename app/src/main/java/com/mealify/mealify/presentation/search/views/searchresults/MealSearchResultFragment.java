package com.mealify.mealify.presentation.search.views.searchresults;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.mealify.mealify.R;
import com.mealify.mealify.data.meals.model.filteredmeals.FilterType;


public class MealSearchResultFragment extends Fragment {

    FilterType filterType;
    String query;

    public MealSearchResultFragment() {

    }

    public static MealSearchResultFragment newInstance(String param1, String param2) {
        return new MealSearchResultFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MealSearchResultFragmentArgs args = MealSearchResultFragmentArgs.fromBundle(getArguments());
        filterType = args.getFilterType();
        query = args.getQuery();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_search_result, container, false);
    }
}