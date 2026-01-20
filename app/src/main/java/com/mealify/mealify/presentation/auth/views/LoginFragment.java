package com.mealify.mealify.presentation.auth.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mealify.mealify.R;
import com.mealify.mealify.core.response.ApiResponse;
import com.mealify.mealify.core.helper.AuthValidator;
import com.mealify.mealify.core.helper.CustomToast;
import com.mealify.mealify.data.auth.datasources.AuthRemoteDataSource;
import com.mealify.mealify.presentation.home.views.HomeActivity;

/**
 * Login Fragment with Firebase Authentication support
 */
public class LoginFragment extends Fragment {

    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private MaterialButton loginButton;
    private MaterialButton googleSignInButton;
    private MaterialButton guestLoginButton;
    private TextView signUpText;
    private ProgressBar loading;
    private View loadingOverlay;
    private AuthRemoteDataSource remoteDs;

    public LoginFragment() {

    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        emailInput = view.findViewById(R.id.emailInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        loginButton = view.findViewById(R.id.loginButton);
        googleSignInButton = view.findViewById(R.id.googleSignInButton);
        guestLoginButton = view.findViewById(R.id.guestLoginButton);
        signUpText = view.findViewById(R.id.signUpText);
        loading = view.findViewById(R.id.loading);
        loadingOverlay = view.findViewById(R.id.loadingOverlay);

        // Initialize data source
        remoteDs = AuthRemoteDataSource.getInstance(getContext());

        // Set up click listeners
        loginButton.setOnClickListener(v -> handleEmailLogin());
        
        googleSignInButton.setOnClickListener(v -> handleGoogleSignIn());
        
        guestLoginButton.setOnClickListener(v -> handleGuestLogin());
        
        signUpText.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment);
        });
    }

    private void showLoading(boolean isLoading) {
            loading.setVisibility(isLoading? View.VISIBLE : View.GONE);
            loadingOverlay.setVisibility(isLoading? View.VISIBLE : View.GONE);
            loginButton.setEnabled(!isLoading);
            googleSignInButton.setEnabled(!isLoading);
            guestLoginButton.setEnabled(!isLoading);
            signUpText.setEnabled(!isLoading);

    }

    private void handleEmailLogin() {
        String email = String.valueOf(emailInput.getText()).trim();
        String password = String.valueOf(passwordInput.getText()).trim();

        String emailError = AuthValidator.validateEmail(email);
        String passwordError = AuthValidator.validatePassword(password);

        if (emailError != null) {
            emailInput.setError(emailError);
            emailInput.requestFocus();
            return;
        }

        if (passwordError != null) {
            passwordInput.setError(passwordError);
            passwordInput.requestFocus();
            return;
        }
        showLoading(true);

        remoteDs.login(email, password, new ApiResponse<String>() {
            @Override
            public void onSuccess(String data) {
                showLoading(false);
                CustomToast.show(getContext(), "Login successful: " + data);
                navigateToHome();
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                CustomToast.show(getContext(),  errorMessage);
            }
        });

    }

    private void handleGoogleSignIn() {
        showLoading(true);
        remoteDs.googleSignIn(new ApiResponse<String>() {
            @Override
            public void onSuccess(String data) {
                showLoading(false);
                CustomToast.show(getContext(), "Google Sign-In successful: " + data);
                navigateToHome();
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                CustomToast.show(getContext(),  errorMessage);
            }
        });
    }

    private void handleGuestLogin() {
        showLoading(true);
        
        remoteDs.signInAnonymously(new ApiResponse<String>() {
            @Override
            public void onSuccess(String data) {
                showLoading(false);
                CustomToast.show(getContext(), "Guest login successful: " + data);
                navigateToHome();
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                CustomToast.show(getContext(),  errorMessage);
            }
        });
    }

    private void navigateToHome() {
        if(getActivity() != null) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}