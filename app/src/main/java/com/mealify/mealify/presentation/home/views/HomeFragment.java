package com.mealify.mealify.presentation.home.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.mealify.mealify.R;
import com.mealify.mealify.network.NetworkObservation;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HomeFragment extends Fragment {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private View homeContentContainer;
    private View offlineContainer;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeContentContainer = view.findViewById(R.id.homeContentContainer);
        offlineContainer = view.findViewById(R.id.offlineContainer);

        if (savedInstanceState == null) {
            loadContentFragment();
        }

        setupNetworkMonitoring();
    }

    private void setupNetworkMonitoring() {
        disposables.add(
                NetworkObservation.getInstance(requireContext())
                        .observeConnection()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isConnected -> {
                            if (isConnected) {
                                homeContentContainer.setVisibility(View.VISIBLE);
                                offlineContainer.setVisibility(View.GONE);
                            } else {
                                homeContentContainer.setVisibility(View.GONE);
                                offlineContainer.setVisibility(View.VISIBLE);
                            }
                        })
        );
    }

    private void loadContentFragment() {
        HomeContentFragment contentFragment = HomeContentFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.homeContentContainer, contentFragment);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
    }
}