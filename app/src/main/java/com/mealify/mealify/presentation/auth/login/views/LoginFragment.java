package com.mealify.mealify.presentation.auth.login.views;

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
import com.mealify.mealify.presentation.auth.login.presenter.LoginPresenter;
import com.mealify.mealify.presentation.auth.login.presenter.LoginPresenterImpl;
import com.mealify.mealify.presentation.home.views.HomeActivity;

public class LoginFragment extends Fragment implements LoginView {

    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private MaterialButton loginButton;
    private MaterialButton googleSignInButton;
    private MaterialButton guestLoginButton;
    private TextView signUpText;
    private ProgressBar loading;
    private View loadingOverlay;

    private LoginPresenter presenter;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        presenter = new LoginPresenterImpl(requireContext(), this);
        setupButtonListeners();
    }

    private void initViews(View view) {
        emailInput = view.findViewById(R.id.emailInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        loginButton = view.findViewById(R.id.loginButton);
        googleSignInButton = view.findViewById(R.id.googleSignInButton);
        guestLoginButton = view.findViewById(R.id.guestLoginButton);
        signUpText = view.findViewById(R.id.signUpText);
        loading = view.findViewById(R.id.loading);
        loadingOverlay = view.findViewById(R.id.loadingOverlay);
    }

    private void setupButtonListeners() {
        loginButton.setOnClickListener(v -> handleEmailLogin());

        googleSignInButton.setOnClickListener(v ->
                presenter.googleSignIn()
        );

        guestLoginButton.setOnClickListener(v ->
                presenter.signInAnonymously()
        );

        signUpText.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_loginFragment_to_registerFragment)
        );
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

        presenter.login(email, password);
    }

    @Override
    public void onSuccessLogin(String message) {
        navigateToHome(message);
    }


    @Override
    public void onFailureLogin(String errorMessage) {
        CustomSnackbar.showFailure(getView(), errorMessage);
    }

    @Override
    public void toggleLoading(boolean isLoading) {
        loading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        loadingOverlay.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!isLoading);
        googleSignInButton.setEnabled(!isLoading);
        guestLoginButton.setEnabled(!isLoading);
        signUpText.setEnabled(!isLoading);
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