package com.lucagiorgetti.surprix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Utility which contain all the implementations of methods which needs a connection with Firebase Database.
 *
 * Created by Luca on 13/11/2017.
 */

public class SystemUtility {
    public static boolean checkNetworkAvailability(Context context) {
        boolean available = false;
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    available = true;
            }
        } catch (Exception e) {
            available = false;
        }

        if (!available){
            Toast.makeText(context, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }
        return available;
    }

    public static void closeKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void openNewActivityWithFinishing(Activity activity, Context applicationContext, Class<?> cls, Bundle b) {
        Intent i = new Intent(applicationContext, cls);

        if (b != null){
            i.putExtras(b);
        }

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        applicationContext.startActivity(i);
        activity.finish();
    }

    public static void firstTimeOpeningApp(Activity activity, Context applicationContext, Class<?> cls, Bundle b) {
        openNewActivityWithFinishing(activity, applicationContext, cls, b);
        openNewActivity(applicationContext, OnboardActivity.class, null);
    }

    public static void openNewActivity(Context applicationContext, Class<?> cls, Bundle b) {
        Intent i = new Intent(applicationContext, cls);

        if (b != null){
            i.putExtras(b);
        }
        applicationContext.startActivity(i);
    }
}
