package com.lucagiorgetti.surprix.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.views.OnboardActivity;

/**
 * Utility which contain all the implementations of methods which needs a connection with Firebase Database.
 *
 * Created by Luca on 13/11/2017.
 */

public class SystemUtility {
    public static final String FIRST_TIME_YEAR_HELP_SHOW= "showYearHelp";
    public static final String FIRST_TIME_SET_HELP_SHOW = "showSetHelp";
    public static final String PRIVACY_POLICY_ACCEPTED = "privacyPolicyAccepted";
    public static final String USER_USERNAME = "loggedUsername";
    public static final String USER_EMAIL = "loggedEmail";

    public static boolean checkNetworkAvailability(Context context) {
        boolean available = false;
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = null;
                if (connectivityManager != null) {
                    activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                }
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
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void openNewActivityWithFinishing(Activity activity, Class<?> cls, Bundle b) {
        Context applicationContext = SurprixApplication.getSurprixContext();
        Intent i = new Intent(applicationContext, cls);

        if (b != null){
            i.putExtras(b);
        }

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        applicationContext.startActivity(i);
        activity.finish();
    }

    public static void firstTimeOpeningApp(Activity activity, Class<?> cls, Bundle b) {
        Context applicationContext = SurprixApplication.getSurprixContext();
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(applicationContext).edit();
        edit.putBoolean(FIRST_TIME_SET_HELP_SHOW, true);
        edit.putBoolean(FIRST_TIME_YEAR_HELP_SHOW, true);
        edit.apply();

        enableFCM();
        openNewActivityWithFinishing(activity, cls, b);
        openNewActivity(OnboardActivity.class, null);
    }


    public static void enableFCM(){
        // Enable FCM via enable Auto-init service which generate new token and receive in FCMService
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().subscribeToTopic("global");
    }

    public static void disableFCM(){
        // Disable auto init
        FirebaseMessaging.getInstance().setAutoInitEnabled(false);
        new Thread(() -> {
            // Remove InstanceID initiate to unsubscribe all topic
            FirebaseMessaging.getInstance().unsubscribeFromTopic("global");
        }).start();
    }

    public static void openNewActivity(Class<?> cls, Bundle b) {
        Context applicationContext = SurprixApplication.getSurprixContext();
        Intent i = new Intent(applicationContext, cls);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (b != null){
            i.putExtras(b);
        }
        applicationContext.startActivity(i);
    }

    public static void sendMail(Context context, String to, String subject, Spanned html_body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        if (subject != null && !subject.isEmpty()){
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (html_body != null && html_body.length() > 0){
            intent.putExtra(Intent.EXTRA_TEXT, html_body);
        }
        intent.setData(Uri.parse("mailto:" + to));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        context.startActivity(intent);
    }

    public static void openUrl(Context context, String url) {
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static void writeUserInfo(String username, String email) {
        Context applicationContext = SurprixApplication.getSurprixContext();
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(applicationContext).edit();
        edit.putString(USER_USERNAME, username);
        edit.putString(USER_EMAIL, email);
        edit.apply();
    }

    public static String getLoggedUserUsername(){
        Context applicationContext = SurprixApplication.getSurprixContext();
        return PreferenceManager.getDefaultSharedPreferences(applicationContext).getString(USER_USERNAME, null);
    }
}
