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
 * Register Fragment with Firebase Authentication support
 */
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

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameInput = view.findViewById(R.id.nameInput);
        emailInput = view.findViewById(R.id.emailInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        confirmPasswordInput = view.findViewById(R.id.confirmPasswordInput);
        registerButton = view.findViewById(R.id.registerButton);
        googleSignUpButton = view.findViewById(R.id.googleSignUpButton);
        guestLoginButton = view.findViewById(R.id.guestLoginButton);
        signInText = view.findViewById(R.id.signInText);
        loading = view.findViewById(R.id.loading);

        registerButton.setOnClickListener(v -> handleEmailRegister());
        
        googleSignUpButton.setOnClickListener(v -> handleGoogleSignUp());
        
        guestLoginButton.setOnClickListener(v -> handleGuestLogin());
        
        signInText.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment);
        });
    }

    private void handleEmailRegister() {
        String name = nameInput.getText() != null ? nameInput.getText().toString().trim() : "";
        String email = emailInput.getText() != null ? emailInput.getText().toString().trim() : "";
        String password = passwordInput.getText() != null ? passwordInput.getText().toString().trim() : "";
        String confirmPassword = confirmPasswordInput.getText() != null ? confirmPasswordInput.getText().toString().trim() : "";

        if (name.isEmpty()) {
            nameInput.setError("Name is required");
            nameInput.requestFocus();
            return;
        }

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

        if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            passwordInput.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("Passwords do not match");
            confirmPasswordInput.requestFocus();
            return;
        }

        loading.setVisibility(View.VISIBLE);
        registerButton.setEnabled(false);

        // TODO: Implement Firebase Authentication
        Toast.makeText(getContext(), "Email registration will be implemented with Firebase", Toast.LENGTH_SHORT).show();
    }

    private void handleGoogleSignUp() {
        loading.setVisibility(View.VISIBLE);
        
        // TODO: Implement Google Sign-Up with Firebase
        Toast.makeText(getContext(), "Google Sign-Up will be implemented with Firebase", Toast.LENGTH_SHORT).show();
        
        loading.setVisibility(View.GONE);
    }

    private void handleGuestLogin() {
        loading.setVisibility(View.VISIBLE);
        
        // TODO: Implement Guest Login
        Toast.makeText(getContext(), "Guest login will be implemented", Toast.LENGTH_SHORT).show();
        
        loading.setVisibility(View.GONE);
    }
}