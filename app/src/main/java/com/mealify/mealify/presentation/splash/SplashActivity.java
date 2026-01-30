package com.mealify.mealify.presentation.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.mealify.mealify.R;
import com.mealify.mealify.core.helper.CustomLogger;
import com.mealify.mealify.data.auth.datasources.PrefsDataSource;
import com.mealify.mealify.presentation.auth.AuthActivity;
import com.mealify.mealify.presentation.home.views.HomeActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class SplashActivity extends AppCompatActivity {

    private final CompositeDisposable disposable = new CompositeDisposable();

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

        checkLoginStatus();
    }

    private void checkLoginStatus() {
        PrefsDataSource prefsDataSource = new PrefsDataSource(this);

        disposable.add(Observable.timer(3, TimeUnit.SECONDS)
                .flatMap(aLong -> prefsDataSource.observeString("user_id", ""))
                .take(1)
                .subscribeOn(Schedulers.io())
                .subscribe(userId -> {
                    runOnUiThread(() -> navigate(userId));
                }, throwable -> {
                    CustomLogger.log("Splash Error: " + throwable.getMessage());
                    runOnUiThread(() -> navigate(""));
                }));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}