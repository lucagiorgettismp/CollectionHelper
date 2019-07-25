package com.lucagiorgetti.surprix.listenerInterfaces;

/**
 * Interface which contains methods to be implemented for a query with Firebase
 * if a Datasnapshot is expected
 * <p>
 * Created by Luca on 23/11/2017.
 */

public interface OnGetResultListener {
    void onSuccess(boolean result);

    void onFailure();
}