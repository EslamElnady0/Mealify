package com.mealify.mealify.core.helper;

import android.util.Patterns;

public class AuthValidator {

    public static String validateEmail(String email) {
        if (email.isEmpty()) return "Email is required";
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return "Invalid email format";
        return null;
    }

    public static String validatePassword(String password) {
        if (password.isEmpty()) return "Password is required";
        if (password.length() < 6)
            return "Password must be at least 6 characters";
        return null;
    }

    public static String validateName(String name) {
        if (name.isEmpty()) return "Name is required";
        return null;
    }

    public static String validateConfirmPassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword))
            return "Passwords do not match";
        return null;
    }
}
