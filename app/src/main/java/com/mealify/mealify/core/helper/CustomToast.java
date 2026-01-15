package com.mealify.mealify.core.helper;

import android.content.Context;
import android.widget.Toast;

public class CustomToast {
    void show(Context ctx, String msg){
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
}
