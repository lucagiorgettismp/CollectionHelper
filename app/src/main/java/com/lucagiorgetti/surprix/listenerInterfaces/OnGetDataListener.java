package com.lucagiorgetti.surprix.listenerInterfaces;

import com.google.firebase.database.DataSnapshot;

/**
 * Interface which contains methods to be implemented for a query with Firebase
 * if a Datasnapshot is expected
 *
 * Created by Luca on 23/11/2017.
 */

public interface OnGetDataListener extends OnGetListener {
    void onSuccess(DataSnapshot data);
}