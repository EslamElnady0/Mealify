package com.mealify.mealify.core.helper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.CalendarContract;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendarHelper {

    private final Context context;

    public CalendarHelper(Context context) {
        this.context = context;
    }

    public static boolean hasCalendarPermissions(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) 
                == PackageManager.PERMISSION_GRANTED &&
               ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) 
                == PackageManager.PERMISSION_GRANTED;
    }

    public void addMealToSystemCalendar(String mealName, String dateStr) {
        long startMillis = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
            Date date = sdf.parse(dateStr);
            if (date != null) startMillis = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startMillis + (60 * 60 * 1000)) 
                .putExtra(CalendarContract.Events.TITLE, "Meal: " + mealName)
                .putExtra(CalendarContract.Events.DESCRIPTION, "Planned from Food Planner App")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        context.startActivity(intent);
    }
}
