package com.mealify.mealify.presentation.profile.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.mealify.mealify.R;
import com.mealify.mealify.presentation.auth.AuthActivity;
import com.mealify.mealify.presentation.profile.presenter.ProfilePresenter;
import com.mealify.mealify.presentation.profile.presenter.ProfilePresenterImpl;

public class ProfileFragment extends Fragment implements ProfileView {

    private TextView usernameText;
    private TextView emailText;
    private TextView favCountText;
    private TextView planCountText;
    private MaterialButton logoutBtn;
    private ProfilePresenter presenter;
    private View loadingOverlay;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ProfilePresenterImpl(requireContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameText = view.findViewById(R.id.username_text);
        emailText = view.findViewById(R.id.email_text);
        favCountText = view.findViewById(R.id.fav_count_text);
        planCountText = view.findViewById(R.id.plan_count_text);
        logoutBtn = view.findViewById(R.id.logout_btn);
        loadingOverlay = view.findViewById(R.id.loading_overlay);
        logoutBtn.setOnClickListener(v -> presenter.logout());
        presenter.loadUserData();
        presenter.loadStats();
    }

    @Override
    public void displayUserData(String name, String email) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (usernameText != null) usernameText.setText(name);
                if (emailText != null) emailText.setText(email);
            });
        }
    }

    @Override
    public void displayStats(int favCount, int planCount) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (favCountText != null) favCountText.setText(String.valueOf(favCount));
                if (planCountText != null) planCountText.setText(String.valueOf(planCount));
            });
        }
    }

    @Override
    public void onLogoutSuccess() {
        if (getContext() != null) {
            Intent intent = new Intent(getContext(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onLogoutError(String errorMessage) {
        if (getContext() != null) {
            Toast.makeText(getContext(), "Logout failed: " + errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void toggleLoading(boolean isLoading) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (loadingOverlay != null) {
                    loadingOverlay.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                }
                if (logoutBtn != null) {
                    logoutBtn.setEnabled(!isLoading);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}