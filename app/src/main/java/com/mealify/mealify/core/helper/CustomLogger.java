package com.mealify.mealify.core.helper;

import android.util.Log;

public class CustomLogger {

    public static void log(String... data){
        String msg = data[0];
        String tag = data[1] == null? "app" : data[1];
        Log.i(tag,msg);
    }

}
