package com.lucagiorgetti.collectionhelper;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Luca on 13/11/2017.
 */
public class DatabaseUtility {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }

        return mDatabase;
    }

}