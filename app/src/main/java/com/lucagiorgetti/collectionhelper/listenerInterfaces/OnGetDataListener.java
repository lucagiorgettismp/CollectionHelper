package com.lucagiorgetti.collectionhelper.listenerInterfaces;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by Luca on 23/11/2017.
 */

public interface OnGetDataListener extends OnGetListener {
    void onSuccess(DataSnapshot data);
}