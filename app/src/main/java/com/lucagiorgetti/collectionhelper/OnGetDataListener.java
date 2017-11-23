package com.lucagiorgetti.collectionhelper;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by Luca on 23/11/2017.
 */

public interface OnGetDataListener {
    //this is for callbacks
    void onSuccess(DataSnapshot dataSnapshot);

    void onSuccess();

    void onStart();

    void onFailure();
}