package com.lucagiorgetti.surprix;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.lucagiorgetti.surprix.model.User;
import com.lucagiorgetti.surprix.utility.SystemUtils;

import timber.log.Timber;

/**
 * Created by Luca Giorgetti on 11/04/2018.
 */

public class SurprixApplication extends Application {
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private static SurprixApplication mInstance;
    private User currentUser;

    public void setUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        boolean dark = SystemUtils.getDarkThemePreference();
        AppCompatDelegate.setDefaultNightMode(dark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        if(BuildConfig.DEBUG){
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
            //firebaseDatabase.setPersistenceEnabled(true);
        }
        if (databaseReference == null) {
            databaseReference = firebaseDatabase.getReference();
            //databaseReference.keepSynced(true);
        }

        return databaseReference;
    }
}
