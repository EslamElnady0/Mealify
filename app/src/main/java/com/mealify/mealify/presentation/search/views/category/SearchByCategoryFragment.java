package com.mealify.mealify.presentation.search.views.category;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.mealify.mealify.R;
import com.mealify.mealify.core.helper.CustomToast;
import com.mealify.mealify.data.meals.model.category.CategoryDto;
import com.mealify.mealify.presentation.search.presenter.category.SearchCategoryPresenter;
import com.mealify.mealify.presentation.search.presenter.category.SearchCategoryPresenterImpl;

import java.util.List;

public class SearchByCategoryFragment extends Fragment implements SearchCategoryView {

    private SearchCategoryPresenter presenter;
    private SearchCategoryAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextInputEditText searchEditText;

    public SearchByCategoryFragment() {
    }

    public static SearchByCategoryFragment newInstance() {
        return new SearchByCategoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_by_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        setupPresenter();
        setupSearchListener();

        presenter.getCategories();
    }

    private void initViews(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.categoriesRecyclerView);
        searchEditText = view.findViewById(R.id.search_edit_text);
    }

    private void setupRecyclerView() {
        adapter = new SearchCategoryAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnCategoryClickListener(category -> {
           
        });
    }

    private void setupPresenter() {
        presenter = new SearchCategoryPresenterImpl(requireContext(), this);
    }

    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.searchCategories(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void showCategories(List<CategoryDto> categories) {
        adapter.setCategories(categories);
    }

    @Override
    public void showError(String message) {
        CustomToast.show(getContext(), message);
    }

    @Override
    public void toggleLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }
}
