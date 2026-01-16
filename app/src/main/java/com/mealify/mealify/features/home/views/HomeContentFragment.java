package com.mealify.mealify.features.home.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mealify.mealify.R;
import com.mealify.mealify.features.home.adapters.CategoryAdapter;
import com.mealify.mealify.features.home.models.Category;

import java.util.ArrayList;
import java.util.List;


public class HomeContentFragment extends Fragment {

    private RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;

    public HomeContentFragment() {

    }

    public static HomeContentFragment newInstance() {
        return new HomeContentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_content, container, false);

        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        setupCategoriesRecyclerView();
        
        return view;
    }

    private void setupCategoriesRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(), 
                LinearLayoutManager.HORIZONTAL, 
                false
        );
        categoriesRecyclerView.setLayoutManager(layoutManager);

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Beef", R.drawable.category_beef));
        categories.add(new Category("Chicken", R.drawable.category_chicken));
        categories.add(new Category("Beef", R.drawable.category_beef));
        categories.add(new Category("Chicken", R.drawable.category_chicken));
        categories.add(new Category("Beef", R.drawable.category_beef));
        categories.add(new Category("Chicken", R.drawable.category_chicken));

        categoryAdapter = new CategoryAdapter(categories, category -> {
            // TODO: Navigate to category details or filter meals
        });
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }
}
