package com.mealify.mealify.presentation.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mealify.mealify.R;
import com.mealify.mealify.data.repos.auth.AuthRepo;
import com.mealify.mealify.presentation.auth.AuthActivity;
import com.mealify.mealify.presentation.home.views.HomeActivity;
import com.mealify.mealify.presentation.splash.presenter.SplashPresenter;
import com.mealify.mealify.presentation.splash.presenter.SplashPresenterImpl;
import com.mealify.mealify.presentation.splash.views.SplashView;

public class SplashActivity extends AppCompatActivity implements SplashView {

    private SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        presenter = new SplashPresenterImpl(this, new AuthRepo(this));
        
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            presenter.checkLoginStatus();
        }, 2000);
    }

    @Override
    public void onUserAuthenticated(String userId) {
        navigate(userId);
    }

    @Override
    public void onUserNotAuthenticated() {
        navigate(null);
    }

    private void navigate(String userId) {
        Intent intent;
        if (userId == null || userId.isEmpty()) {
            intent = new Intent(SplashActivity.this, AuthActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, HomeActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}