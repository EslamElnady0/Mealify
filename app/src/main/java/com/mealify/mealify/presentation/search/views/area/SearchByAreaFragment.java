package com.mealify.mealify.presentation.search.views.area;

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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.mealify.mealify.InnerAppFragmentDirections;
import com.mealify.mealify.R;
import com.mealify.mealify.core.helper.CustomToast;
import com.mealify.mealify.core.utils.NetworkObservation;
import com.mealify.mealify.data.meals.model.country.CountryDto;
import com.mealify.mealify.data.meals.model.filteredmeals.FilterType;
import com.mealify.mealify.presentation.search.presenter.area.SearchAreaPresenter;
import com.mealify.mealify.presentation.search.presenter.area.SearchAreaPresenterImpl;

import java.util.List;

public class SearchByAreaFragment extends Fragment implements SearchAreaView {

    private final io.reactivex.rxjava3.disposables.CompositeDisposable disposables = new io.reactivex.rxjava3.disposables.CompositeDisposable();
    private SearchAreaPresenter presenter;
    private SearchAreaAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextInputEditText searchEditText;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        setupPresenter();
        setupSearchListener();

        loadData();
        setupNetworkMonitoring();
    }

    private void loadData() {
        if (presenter != null) {
            presenter.getAreas();
        }
    }

    private void setupNetworkMonitoring() {
        disposables.add(
                NetworkObservation.getInstance(requireContext())
                        .observeConnection()
                        .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
                        .subscribe(isConnected -> {
                            if (isConnected) {
                                loadData();
                            }
                        })
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
    }

    private void initViews(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.areasRecyclerView);
        searchEditText = view.findViewById(R.id.search_edit_text);
    }

    private void setupRecyclerView() {
        adapter = new SearchAreaAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnAreaClickListener(area -> {
            InnerAppFragmentDirections.ActionInnerAppFragmentToMealSearchResultFragment action =
                    InnerAppFragmentDirections.actionInnerAppFragmentToMealSearchResultFragment(FilterType.AREA, area.getStrArea());
            NavHostFragment navHostFragment = (NavHostFragment)
                    getActivity().getSupportFragmentManager().findFragmentById(R.id.inner_home_container);
            NavController navController = navHostFragment.getNavController();
            navController.navigate(action);
        });
    }

    private void setupPresenter() {
        presenter = new SearchAreaPresenterImpl(requireContext(), this);
    }

    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.searchAreas(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void showAreas(List<CountryDto> areas) {
        adapter.setAreas(areas);
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
