package com.mealify.mealify.features.home.views;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mealify.mealify.R;
import com.mealify.mealify.core.datasource.remote.response.ApiResponse;
import com.mealify.mealify.core.helper.CustomToast;
import com.mealify.mealify.features.home.data.datasource.remote.HomeRemoteDataSource;
import com.mealify.mealify.features.home.data.model.category.CategoryDto;

import java.util.List;


public class HomeContentFragment extends Fragment {
    private HomeRemoteDataSource homeRemoteDataSource;

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
        homeRemoteDataSource = new HomeRemoteDataSource();
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
        categoryAdapter = new CategoryAdapter( category -> {
            CustomToast.show(requireContext(), "Clicked on category: " + category.name);
        });

        categoriesRecyclerView.setAdapter(categoryAdapter);
        homeRemoteDataSource.getCategories(new ApiResponse<List<CategoryDto>>() {
            @Override
            public void onSuccess(List<CategoryDto> data) {
                categoryAdapter.setCategories(data);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });


        categoriesRecyclerView.setAdapter(categoryAdapter);
    }
}
