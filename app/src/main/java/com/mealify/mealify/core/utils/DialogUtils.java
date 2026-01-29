package com.mealify.mealify.core.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mealify.mealify.R;

public class DialogUtils {

    public static void showConfirmationDialog(Context context,
                                              String title,
                                              String description,
                                              DialogCallback callback) {

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirmation, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView titleTv = dialogView.findViewById(R.id.dialog_title);
        TextView descriptionTv = dialogView.findViewById(R.id.dialog_description);
        Button btnOk = dialogView.findViewById(R.id.btn_ok);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

        titleTv.setText(title);
        descriptionTv.setText(description);

        btnOk.setOnClickListener(v -> {
            if (callback != null) {
                callback.onConfirm();
            }
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            if (callback != null) {
                callback.onCancel();
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    public static void showConfirmationDialog(Context context,
                                              int imageRes,
                                              int iconRes,
                                              String title,
                                              String description,
                                              DialogCallback callback) {

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirmation, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ImageView imageIv = dialogView.findViewById(R.id.dialog_image);
        TextView titleTv = dialogView.findViewById(R.id.dialog_title);
        TextView descriptionTv = dialogView.findViewById(R.id.dialog_description);
        Button btnOk = dialogView.findViewById(R.id.btn_ok);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

        if (imageRes != -1) {
            Glide.with(context)
                    .load(imageRes)
                    .circleCrop()
                    .into(imageIv);
        }

        titleTv.setText(title);
        descriptionTv.setText(description);

        btnOk.setOnClickListener(v -> {
            if (callback != null) {
                callback.onConfirm();
            }
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            if (callback != null) {
                callback.onCancel();
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    public static void showConfirmationDialog(Context context,
                                              String imageUrl,
                                              String title,
                                              String description,
                                              DialogCallback callback) {

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirmation, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ImageView imageIv = dialogView.findViewById(R.id.dialog_image);
        TextView titleTv = dialogView.findViewById(R.id.dialog_title);
        TextView descriptionTv = dialogView.findViewById(R.id.dialog_description);
        Button btnOk = dialogView.findViewById(R.id.btn_ok);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .circleCrop()
                    .into(imageIv);
        }

        titleTv.setText(title);
        descriptionTv.setText(description);

        btnOk.setOnClickListener(v -> {
            if (callback != null) {
                callback.onConfirm();
            }
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            if (callback != null) {
                callback.onCancel();
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    public interface DialogCallback {
        void onConfirm();

        void onCancel();
    }
}
