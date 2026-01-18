package com.mealify.mealify.features.auth.views;

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
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mealify.mealify.R;

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

        // Set up click listeners
        loginButton.setOnClickListener(v -> handleEmailLogin());
        
        googleSignInButton.setOnClickListener(v -> handleGoogleSignIn());
        
        guestLoginButton.setOnClickListener(v -> handleGuestLogin());
        
        signUpText.setOnClickListener(v -> {
            // Navigate to Register Fragment
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment);
        });
    }

    private void handleEmailLogin() {
        String email = emailInput.getText() != null ? emailInput.getText().toString().trim() : "";
        String password = passwordInput.getText() != null ? passwordInput.getText().toString().trim() : "";

        if (email.isEmpty()) {
            emailInput.setError("Email is required");
            emailInput.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordInput.setError("Password is required");
            passwordInput.requestFocus();
            return;
        }


        loading.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        // TODO: Implement Firebase Authentication
        Toast.makeText(getContext(), "Email login will be implemented with Firebase", Toast.LENGTH_SHORT).show();

//        overlay.postDelayed(() -> {
//            loading.setVisibility(View.GONE);
//            loginButton.setEnabled(true);
//        }, 1500);
    }

    private void handleGoogleSignIn() {
        loading.setVisibility(View.VISIBLE);
        
        // TODO: Implement Google Sign-In with Firebase
        Toast.makeText(getContext(), "Google Sign-In will be implemented with Firebase", Toast.LENGTH_SHORT).show();
        
        loading.setVisibility(View.GONE);
    }

    private void handleGuestLogin() {
        loading.setVisibility(View.VISIBLE);
        
        // TODO: Implement Guest Login
        Toast.makeText(getContext(), "Guest login will be implemented", Toast.LENGTH_SHORT).show();
        
        loading.setVisibility(View.GONE);
    }
}