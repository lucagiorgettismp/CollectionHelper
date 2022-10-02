package com.lucagiorgetti.surprix;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.lucagiorgetti.surprix.model.User;
import com.lucagiorgetti.surprix.utility.ForceUpdateChecker;
import com.lucagiorgetti.surprix.utility.SystemUtils;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by Luca Giorgetti on 11/04/2018.
 */

public class SurprixApplication extends Application {
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseRemoteConfig firebaseRemoteConfig;

    private static SurprixApplication mInstance;
    private User currentUser;

    public void setUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        boolean dark = SystemUtils.getDarkThemePreference();
        AppCompatDelegate.setDefaultNightMode(dark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static synchronized SurprixApplication getInstance() {
        return mInstance;
    }

    public static synchronized Context getSurprixContext() {
        return mInstance.getApplicationContext();
    }

    synchronized public FirebaseStorage getFirebaseStorage() {
        if (firebaseStorage == null) {
            firebaseStorage = FirebaseStorage.getInstance();
        }
        return firebaseStorage;
    }

    synchronized public DatabaseReference getDatabaseReference() {
        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
        }
        if (databaseReference == null) {
            databaseReference = firebaseDatabase.getReference();
        }

        return databaseReference;
    }

    synchronized public FirebaseRemoteConfig getFirebaseRemoteConfig() {
        if (firebaseRemoteConfig == null) {

            firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

            Map<String, Object> remoteConfigDefaults = new HashMap();
            remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
            remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, "1.0.0");
            remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL,
                    "https://play.google.com/store/apps/details?id=com.lucagiorgetti.surprix");

            FirebaseRemoteConfigSettings settings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(60)
                    .build();

            firebaseRemoteConfig.setConfigSettingsAsync(settings);
            firebaseRemoteConfig.setDefaultsAsync(remoteConfigDefaults);

            firebaseRemoteConfig.fetchAndActivate() // fetch every minutes
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Timber.d("remote config is fetched.");
                        }
                    });
        }
        return firebaseRemoteConfig;
    }
}
