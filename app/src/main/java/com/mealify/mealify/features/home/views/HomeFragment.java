package com.mealify.mealify.features.home.views;

import android.os.Bundle;

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

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        if (savedInstanceState == null) {
            loadContentFragment();
        }
        
        return view;
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