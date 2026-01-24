package com.mealify.mealify.presentation.search.views.ingredient;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.mealify.mealify.R;
import com.mealify.mealify.data.meals.model.ingredient.IngredientDto;
import com.mealify.mealify.presentation.search.presenter.ingredient.SearchIngredientPresenter;
import com.mealify.mealify.presentation.search.presenter.ingredient.SearchIngredientPresenterImpl;

import java.util.List;

public class SearchByIngredientFragment extends Fragment implements SearchIngredientView {

    private SearchIngredientPresenter presenter;
    private SearchIngredientAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextInputEditText searchEditText;

    public SearchByIngredientFragment() {
    }

    public static SearchByIngredientFragment newInstance() {
        return new SearchByIngredientFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_by_ingredient, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        setupPresenter();
        setupSearchListener();

        presenter.getIngredients();
    }

    private void initViews(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.ingredientsRecyclerView);
        searchEditText = view.findViewById(R.id.search_edit_text);
    }

    private void setupRecyclerView() {
        adapter = new SearchIngredientAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnIngredientClickListener(ingredient -> {
            Toast.makeText(getContext(), "Clicked: " + ingredient.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    private void setupPresenter() {
        presenter = new SearchIngredientPresenterImpl(requireContext(), this);
    }

    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.searchIngredients(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void showIngredients(List<IngredientDto> ingredients) {
        adapter.setIngredients(ingredients);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toggleLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }
}
