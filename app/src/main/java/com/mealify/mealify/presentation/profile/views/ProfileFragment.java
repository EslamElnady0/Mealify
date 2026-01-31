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
import com.mealify.mealify.core.utils.NetworkObservation;
import com.mealify.mealify.presentation.auth.AuthActivity;
import com.mealify.mealify.presentation.profile.presenter.ProfilePresenter;
import com.mealify.mealify.presentation.profile.presenter.ProfilePresenterImpl;

public class ProfileFragment extends Fragment implements ProfileView {

    private final io.reactivex.rxjava3.disposables.CompositeDisposable disposables = new io.reactivex.rxjava3.disposables.CompositeDisposable();
    private TextView usernameText;
    private TextView emailText;
    private TextView favCountText;
    private TextView planCountText;
    private MaterialButton logoutBtn;
    private MaterialButton loginBtn;
    private View guestContainer;
    private View statsContainer;
    private View profileImageContainer;
    private View dividerLine;
    private ProfilePresenter presenter;
    private View offlineContainer;
    private View profileContent;
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
        loginBtn = view.findViewById(R.id.login_btn);
        guestContainer = view.findViewById(R.id.guest_container);
        statsContainer = view.findViewById(R.id.stats_container);
        profileImageContainer = view.findViewById(R.id.profile_image_container);
        dividerLine = view.findViewById(R.id.divider_line);
        loadingOverlay = view.findViewById(R.id.loading_overlay);
        offlineContainer = view.findViewById(R.id.offlineContainer);
        profileContent = view.findViewById(R.id.profile_scroll_view);

        logoutBtn.setOnClickListener(v -> presenter.logout());
        loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AuthActivity.class);
            startActivity(intent);
        });

        presenter.loadUserData();
        presenter.loadStats();

        setupNetworkMonitoring();
    }

    private void setupNetworkMonitoring() {
        disposables.add(
                NetworkObservation.getInstance(requireContext())
                        .observeConnection()
                        .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
                        .subscribe(isConnected -> {
                            if (isConnected) {
                                profileContent.setVisibility(View.VISIBLE);
                                offlineContainer.setVisibility(View.GONE);
                                presenter.loadUserData();
                                presenter.loadStats();
                            } else {
                                profileContent.setVisibility(View.GONE);
                                offlineContainer.setVisibility(View.VISIBLE);
                            }
                        })
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
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
    public void setGuestMode(boolean isGuest) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (guestContainer != null) {
                    guestContainer.setVisibility(isGuest ? View.VISIBLE : View.GONE);
                }
                if (statsContainer != null) {
                    statsContainer.setVisibility(isGuest ? View.GONE : View.VISIBLE);
                }
                if (logoutBtn != null) {
                    logoutBtn.setVisibility(isGuest ? View.GONE : View.VISIBLE);
                }
                if (profileImageContainer != null) {
                    profileImageContainer.setVisibility(isGuest ? View.GONE : View.VISIBLE);
                }
                if (usernameText != null) {
                    usernameText.setVisibility(isGuest ? View.GONE : View.VISIBLE);
                }
                if (emailText != null) {
                    emailText.setVisibility(isGuest ? View.GONE : View.VISIBLE);
                }
                if (dividerLine != null) {
                    dividerLine.setVisibility(isGuest ? View.GONE : View.VISIBLE);
                }
            });
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