package com.mealify.mealify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InnerAppFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InnerAppFragment extends Fragment {

    public InnerAppFragment() {
        // Required empty public constructor
    }


    public static InnerAppFragment newInstance(String param1, String param2) {
        InnerAppFragment fragment = new InnerAppFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inner_app, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigationView =
                view.findViewById(R.id.bottomNavBar);

        NavHostFragment navHostFragment =
                (NavHostFragment) getChildFragmentManager()
                        .findFragmentById(R.id.bodyContainer);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }
    }

}