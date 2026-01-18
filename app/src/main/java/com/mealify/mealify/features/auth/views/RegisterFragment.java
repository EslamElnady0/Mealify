package com.mealify.mealify.features.auth.views;

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
import com.mealify.mealify.core.datasource.remote.response.ApiResponse;
import com.mealify.mealify.core.helper.AuthValidator;
import com.mealify.mealify.core.helper.CustomToast;
import com.mealify.mealify.features.auth.data.datasource.AuthRemoteDataSource;
import com.mealify.mealify.features.home.views.HomeActivity;

public class RegisterFragment extends Fragment {

    private TextInputEditText nameInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;

    private MaterialButton registerButton;
    private MaterialButton googleSignUpButton;
    private MaterialButton guestLoginButton;
    private TextView signInText;

    private ProgressBar loading;
    private View loadingOverlay;

    private AuthRemoteDataSource remoteDs;

    public RegisterFragment() {}

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        // Views
        nameInput = view.findViewById(R.id.nameInput);
        emailInput = view.findViewById(R.id.emailInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        confirmPasswordInput = view.findViewById(R.id.confirmPasswordInput);

        registerButton = view.findViewById(R.id.registerButton);
        googleSignUpButton = view.findViewById(R.id.googleSignUpButton);
        guestLoginButton = view.findViewById(R.id.guestLoginButton);
        signInText = view.findViewById(R.id.signInText);

        loading = view.findViewById(R.id.loading);
        loadingOverlay = view.findViewById(R.id.loadingOverlay);

        // Data source
        remoteDs = AuthRemoteDataSource.getInstance(requireContext());

        // Click listeners
        registerButton.setOnClickListener(v -> handleEmailRegister());
        googleSignUpButton.setOnClickListener(v -> handleGoogleSignUp());
        guestLoginButton.setOnClickListener(v -> handleGuestLogin());

        signInText.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_registerFragment_to_loginFragment)
        );
    }

    private void showLoading(boolean isLoading) {
        loading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        loadingOverlay.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        registerButton.setEnabled(!isLoading);
        googleSignUpButton.setEnabled(!isLoading);
        guestLoginButton.setEnabled(!isLoading);
        signInText.setEnabled(!isLoading);
    }

    private void handleEmailRegister() {
        clearErrors();

        String name = String.valueOf(nameInput.getText()).trim();
        String email = String.valueOf(emailInput.getText()).trim();
        String password = String.valueOf(passwordInput.getText()).trim();
        String confirmPassword =
                String.valueOf(confirmPasswordInput.getText()).trim();

        String nameError = AuthValidator.validateName(name);
        String emailError = AuthValidator.validateEmail(email);
        String passwordError = AuthValidator.validatePassword(password);
        String confirmError =
                AuthValidator.validateConfirmPassword(password, confirmPassword);

        if (nameError != null) {
            nameInput.setError(nameError);
            nameInput.requestFocus();
            return;
        }

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

        if (confirmError != null) {
            confirmPasswordInput.setError(confirmError);
            confirmPasswordInput.requestFocus();
            return;
        }

        showLoading(true);

        remoteDs.register(email, password, name,new ApiResponse<String>() {
            @Override
            public void onSuccess(String data) {
                showLoading(false);
                CustomToast.show(getContext(),"registration successful for: "+ data);
                navigateToHome();
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                CustomToast.show(getContext(), errorMessage);
            }
        });
    }

    private void handleGoogleSignUp() {
        showLoading(true);

        remoteDs.googleSignIn(new ApiResponse<String>() {
            @Override
            public void onSuccess(String data) {
                showLoading(false);
                CustomToast.show(getContext(), "Google Sign-Up successful");
                navigateToHome();
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                CustomToast.show(getContext(), errorMessage);
            }
        });
    }

    private void handleGuestLogin() {
        showLoading(true);

        remoteDs.signInAnonymously(new ApiResponse<String>() {
            @Override
            public void onSuccess(String data) {
                showLoading(false);
                CustomToast.show(getContext(), "Guest login successful");
                navigateToHome();
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                CustomToast.show(getContext(), errorMessage);
            }
        });
    }

    private void clearErrors() {
        nameInput.setError(null);
        emailInput.setError(null);
        passwordInput.setError(null);
        confirmPasswordInput.setError(null);
    }

    private void navigateToHome() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}
