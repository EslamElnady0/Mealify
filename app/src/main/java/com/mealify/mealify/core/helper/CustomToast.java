package com.mealify.mealify.core.helper;

import android.content.Context;
import android.widget.Toast;

public class CustomToast {

    public static void show(Context context, String message) {
        if (context == null) return;

        new android.os.Handler(android.os.Looper.getMainLooper())
                .post(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }
}

