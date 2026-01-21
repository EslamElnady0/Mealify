package com.mealify.mealify.presentation.home.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mealify.mealify.R;

public class HomeFragment extends Fragment {

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
        if (savedInstanceState == null) {
            loadContentFragment();
        }
    }

    private void loadContentFragment() {
        HomeContentFragment contentFragment = HomeContentFragment.newInstance();
        
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.homeContentContainer, contentFragment);
        transaction.commit();
    }

    public void switchToOfflineMode() {
        // TODO: Create and load offline fragment
        // OfflineFragment offlineFragment = OfflineFragment.newInstance();
        // FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        // transaction.replace(R.id.homeContentContainer, offlineFragment);
        // transaction.commit();
    }
    public void switchToOnlineMode() {
        loadContentFragment();
    }
}