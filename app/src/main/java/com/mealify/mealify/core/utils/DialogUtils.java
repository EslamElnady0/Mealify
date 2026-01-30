package com.mealify.mealify.core.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mealify.mealify.R;
import com.mealify.mealify.presentation.auth.AuthActivity;

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

    public static void showAddMealOptionDialog(Context context, AddMealOptionCallback callback) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_option, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        View btnGoToFavorites = dialogView.findViewById(R.id.btn_go_to_favorites);
        View btnGoToSearch = dialogView.findViewById(R.id.btn_go_to_search);
        View btnCancel = dialogView.findViewById(R.id.btn_cancel);

        btnGoToFavorites.setOnClickListener(v -> {
            if (callback != null) {
                callback.onGoToFavorites();
            }
            dialog.dismiss();
        });

        btnGoToSearch.setOnClickListener(v -> {
            if (callback != null) {
                callback.onGoToSearch();
            }
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public static void showReplaceMealDialog(Context context,
                                             String existingMealName,
                                             String existingMealThumbnail,
                                             DialogCallback callback) {

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_replace_meal, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ImageView mealImage = dialogView.findViewById(R.id.existing_meal_image);
        TextView mealName = dialogView.findViewById(R.id.existing_meal_name);
        Button btnReplace = dialogView.findViewById(R.id.btn_replace);
        Button btnKeep = dialogView.findViewById(R.id.btn_keep);

        mealName.setText(existingMealName);
        if (existingMealThumbnail != null && !existingMealThumbnail.isEmpty()) {
            Glide.with(context)
                    .load(existingMealThumbnail)
                    .placeholder(R.drawable.mealify_logo)
                    .error(R.drawable.mealify_logo)
                    .into(mealImage);
        }

        btnReplace.setOnClickListener(v -> {
            if (callback != null) {
                callback.onConfirm();
            }
            dialog.dismiss();
        });

        btnKeep.setOnClickListener(v -> {
            if (callback != null) {
                callback.onCancel();
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    public static void showGuestLoginDialog(Context context) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_guest_mode, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Button btnLogin = dialogView.findViewById(R.id.btn_login);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

        btnLogin.setOnClickListener(v -> {
            Intent intent = new android.content.Intent(context, AuthActivity.class);
            context.startActivity(intent);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public interface DialogCallback {
        void onConfirm();

        void onCancel();
    }

    public interface AddMealOptionCallback {
        void onGoToFavorites();

        void onGoToSearch();
    }
}
