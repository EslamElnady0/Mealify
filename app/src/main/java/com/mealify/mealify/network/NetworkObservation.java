package com.mealify.mealify.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class NetworkObservation {

    private static NetworkObservation instance;
    private final BehaviorSubject<Boolean> statusSubject = BehaviorSubject.create();
    private final ConnectivityManager connectivityManager;

    private NetworkObservation(Context context) {
        connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        
        // Initial check
        checkConnection();

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                statusSubject.onNext(true);
            }

            @Override
            public void onLost(@NonNull Network network) {
                statusSubject.onNext(false);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else {
            NetworkRequest request = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build();
            connectivityManager.registerNetworkCallback(request, networkCallback);
        }
    }

    public static synchronized NetworkObservation getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkObservation(context);
        }
        return instance;
    }

    private void checkConnection() {
        android.net.NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        statusSubject.onNext(isConnected);
    }

    public Observable<Boolean> observeConnection() {
        return statusSubject.distinctUntilChanged();
    }
}
