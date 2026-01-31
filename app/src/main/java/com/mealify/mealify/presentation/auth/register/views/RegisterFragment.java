package com.mealify.mealify.presentation.auth.register.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mealify.mealify.R;
import com.mealify.mealify.core.helper.AuthValidator;
import com.mealify.mealify.core.helper.CustomSnackbar;
import com.mealify.mealify.presentation.auth.register.presenter.RegisterPresenter;
import com.mealify.mealify.presentation.auth.register.presenter.RegisterPresenterImpl;
import com.mealify.mealify.presentation.home.views.HomeActivity;

public class RegisterFragment extends Fragment implements RegisterView {

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

    private RegisterPresenter presenter;

    public RegisterFragment() {
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
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

        initViews(view);

        presenter = new RegisterPresenterImpl(requireContext(), this);
        setupButtonListeners();
    }

    private void initViews(View view) {
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
    }

    private void setupButtonListeners() {
        registerButton.setOnClickListener(v -> handleEmailRegister());

        googleSignUpButton.setOnClickListener(v ->
                presenter.googleSignIn()
        );

        guestLoginButton.setOnClickListener(v ->
                presenter.signInAnonymously()
        );

        signInText.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_registerFragment_to_loginFragment)
        );
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

        presenter.register(email, password, name);
    }


    @Override
    public void onSuccessRegister(String message) {
        navigateToHome(message);
    }

    @Override
    public void onFailureRegister(String errorMessage) {
        CustomSnackbar.showFailure(getView(), errorMessage);
    }

    @Override
    public void toggleLoading(boolean isLoading) {
        loading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        loadingOverlay.setVisibility(isLoading ? View.VISIBLE : View.GONE);

        registerButton.setEnabled(!isLoading);
        googleSignUpButton.setEnabled(!isLoading);
        guestLoginButton.setEnabled(!isLoading);
        signInText.setEnabled(!isLoading);
    }

    private void clearErrors() {
        nameInput.setError(null);
        emailInput.setError(null);
        passwordInput.setError(null);
        confirmPasswordInput.setError(null);
    }

    private void navigateToHome(String message) {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.putExtra("login_success", true);
            intent.putExtra("success_message", message);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        }
    }
}
