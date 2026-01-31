package com.mealify.mealify.core.helper;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.mealify.mealify.R;

public class CustomSnackbar {

    public static Snackbar showSuccess(View view, String message) {
        return show(view, message, R.drawable.snackbar_success_bg, R.color.primary);
    }

    public static Snackbar showFailure(View view, String message) {
        return show(view, message, R.drawable.snackbar_error_bg, R.color.error);
    }

    private static Snackbar show(View view, String message, int backgroundRes, int colorRes) {
        if (view == null) return null;
        
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();

        snackbarView.setBackgroundTintList(null);
        snackbarView.setBackgroundResource(backgroundRes);

        if (snackbarView.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
            params.setMargins(48, 0, 48, 100);
            snackbarView.setLayoutParams(params);
        }

        int color = view.getContext().getColor(colorRes);
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        if (textView != null) {
            textView.setTextColor(color);
        }

        snackbar.setAction("OK", v -> snackbar.dismiss());
        snackbar.setActionTextColor(color);

        snackbar.show();
        return snackbar;
    }
}
