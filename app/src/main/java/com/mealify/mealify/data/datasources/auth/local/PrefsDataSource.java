package com.mealify.mealify.data.datasources.auth.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

import io.reactivex.Observable;

public class PrefsDataSource {
    private final static String PREFS_NAME = "mealify_prefs";
    private final SharedPreferences sharedPreferences;
    private final RxSharedPreferences rxPrefs;

    public PrefsDataSource(Context ctx) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        rxPrefs = RxSharedPreferences.create(sharedPreferences);
    }

    public void saveString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public Observable<String> observeString(String key, String defaultValue) {
        return rxPrefs.getString(key, defaultValue).asObservable();
    }
}