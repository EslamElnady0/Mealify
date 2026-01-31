package com.mealify.mealify.presentation.search.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mealify.mealify.R;
import com.mealify.mealify.core.utils.NetworkObservation;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchFragment extends Fragment {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private View offlineContainer;
    private SearchPagerAdapter pagerAdapter;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        offlineContainer = view.findViewById(R.id.offlineContainer);

        pagerAdapter = new SearchPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(R.string.name);
                            break;
                        case 1:
                            tab.setText(R.string.category);
                            break;
                        case 2:
                            tab.setText(R.string.ingredient);
                            break;
                        case 3:
                            tab.setText(R.string.area);
                            break;
                    }
                }).attach();

        setupNetworkMonitoring();
    }

    private void setupNetworkMonitoring() {
        disposables.add(
                NetworkObservation.getInstance(requireContext())
                        .observeConnection()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isConnected -> {
                                    if (isConnected) {
                                        tabLayout.setVisibility(View.VISIBLE);
                                        viewPager.setVisibility(View.VISIBLE);
                                        offlineContainer.setVisibility(View.GONE);
                                    } else {
                                        tabLayout.setVisibility(View.GONE);
                                        viewPager.setVisibility(View.GONE);
                                        offlineContainer.setVisibility(View.VISIBLE);
                                    }
                                },
                                throwable -> {
                                }
                        )
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
    }
}
