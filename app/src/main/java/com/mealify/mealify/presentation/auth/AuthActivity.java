package com.mealify.mealify.presentation.auth;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.mealify.mealify.R;
import com.mealify.mealify.core.helper.CustomSnackbar;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);

        if (getIntent().getBooleanExtra("logout_success", false)) {
            CustomSnackbar.showSuccess(findViewById(android.R.id.content), "Logged out successfully");
        }
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
//            return insets;
//        });

    }
}