package com.lucagiorgetti.surprix.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.preference.PreferenceManager;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.model.User;

import timber.log.Timber;

/**
 * Utility which contain all the implementations of methods which needs a connection with Firebase Database.
 * <p>
 * Created by Luca on 13/11/2017.
 */

public class SystemUtils {
    private static final String FIRST_TIME_YEAR_HELP_SHOW = "showYearHelp";
    private static final String FIRST_TIME_SET_HELP_SHOW = "showSetHelp";
    private static final String PRIVACY_POLICY_ACCEPTED = "privacyPolicyAccepted";
    private static final String THEME_DARK_SELECTED = "darkThemeSelected";
    private static final String TAG = "SystemUtility";

    public static boolean checkNetworkAvailability() {
        boolean available = false;

        Context context = SurprixApplication.getSurprixContext();
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

        return available;
    }

    public static void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        View view = activity.getCurrentFocus();

        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void openNewActivityWithFinishing(Activity activity, Class<?> cls) {
        Context applicationContext = SurprixApplication.getSurprixContext();
        Intent i = new Intent(applicationContext, cls);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        applicationContext.startActivity(i);
        activity.finish();
    }

    public static void firstTimeOpeningApp() {
        Context applicationContext = SurprixApplication.getSurprixContext();
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(applicationContext).edit();
        edit.putBoolean(FIRST_TIME_SET_HELP_SHOW, true);
        edit.putBoolean(FIRST_TIME_YEAR_HELP_SHOW, true);
        edit.apply();
    }

    public static void setDarkThemePreference(boolean darkEnabled) {
        Context applicationContext = SurprixApplication.getSurprixContext();
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(applicationContext).edit();
        edit.putBoolean(THEME_DARK_SELECTED, darkEnabled);
        edit.apply();
    }

    public static boolean getDarkThemePreference() {
        return PreferenceManager.getDefaultSharedPreferences(SurprixApplication.getSurprixContext()).getBoolean(THEME_DARK_SELECTED, false);
    }

    public static void enableFCM() {
        // Enable FCM via enable Auto-init service which generate new token and receive in FCMService
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        Timber.i("FCM enable");
        FirebaseMessaging.getInstance().subscribeToTopic("global");
    }

    private static void disableFCM() {
        // Disable auto init
        FirebaseMessaging.getInstance().setAutoInitEnabled(false);
        new Thread(() -> {
            // Remove InstanceID initiate to unsubscribe all topic
            FirebaseMessaging.getInstance().unsubscribeFromTopic("global");
            Timber.i("FCM disable");
        }).start();
    }

    public static void openNewActivity(Class<?> cls, Bundle b) {
        Context applicationContext = SurprixApplication.getSurprixContext();
        Intent i = new Intent(applicationContext, cls);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (b != null) {
            i.putExtras(b);
        }
        applicationContext.startActivity(i);
    }


    public static void setSessionUser(String email, CallbackInterface<Boolean> listener) {
        DatabaseUtils.getCurrentUser(new CallbackInterface<User>() {
            @Override
            public void onSuccess(User currentUser) {
                if (currentUser != null) {
                    SurprixApplication.getInstance().setUser(currentUser);
                    DatabaseUtils.setUsername(currentUser.getUsername());
                    listener.onSuccess(true);
                }
            }

            @Override
            public void onStart() {
                listener.onStart();
            }

            @Override
            public void onFailure() {
                listener.onFailure();
            }
        }, email);
    }

    private static void removeSessionUser() {
        SurprixApplication.getInstance().setUser(null);
    }

    public static void logout() {
        SystemUtils.disableFCM();
        SystemUtils.removeSessionUser();
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        DatabaseUtils.setUsername(null);
    }

    public static void setPrivacyPolicyAccepted(boolean accepted) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(SurprixApplication.getSurprixContext()).edit();
        edit.putBoolean(PRIVACY_POLICY_ACCEPTED, accepted);
        edit.apply();
    }
}
