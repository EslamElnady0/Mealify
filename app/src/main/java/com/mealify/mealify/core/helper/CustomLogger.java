package com.mealify.mealify.core.helper;

import android.util.Log;

public class CustomLogger {

    public static void log(String... data) {
        String msg = data.length > 0 ? data[0] : "No message";
        String tag = data.length > 1 && data[1] != null ? data[1] : "app";
        Log.i(tag, msg);
    }
}
