package com.mealify.mealify.presentation.home.views;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mealify.mealify.R;
import com.mealify.mealify.core.utils.NetworkObservation;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HomeActivity extends AppCompatActivity {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private android.view.View offlineBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        offlineBanner = findViewById(R.id.offline_banner);
        setupNetworkMonitoring();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupNetworkMonitoring() {
        disposables.add(
                NetworkObservation.getInstance(this)
                        .observeConnection()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isConnected -> {
                            offlineBanner.setVisibility(isConnected ? android.view.View.GONE : android.view.View.VISIBLE);
                        })
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}