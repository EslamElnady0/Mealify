package com.mealify.mealify.presentation.search.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.mealify.mealify.R;

public class SearchByAreaFragment extends Fragment {

    public SearchByAreaFragment() {
    }

    public static SearchByAreaFragment newInstance() {
        return new SearchByAreaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_by_area, container, false);
    }
}
