package com.lucagiorgetti.surprix;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by Luca Giorgetti on 11/04/2018.
 */

public class SurprixApplication extends Application{
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth fireAuth;
    private static SurprixApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized SurprixApplication getInstance() {
        return mInstance;
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
            firebaseDatabase.setPersistenceEnabled(true);
        }
        if (databaseReference == null){
            databaseReference = firebaseDatabase.getReference();
            databaseReference.keepSynced(true);
        }

        return databaseReference;
    }

    synchronized public FirebaseAuth getFirebaseAuth() {
        if (fireAuth == null) {
            fireAuth = FirebaseAuth.getInstance();
        }
        return fireAuth;
    }
    
}
