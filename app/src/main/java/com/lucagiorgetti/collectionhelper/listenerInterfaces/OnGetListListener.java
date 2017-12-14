package com.lucagiorgetti.collectionhelper.listenerInterfaces;

import java.util.ArrayList;

/**
 * Interface which contains methods to be implemented for a query with Firebase
 * if a generic list is expected
 *
 * Created by Luca on 23/11/2017.
 */

public interface OnGetListListener<T> extends OnGetListener {
    void onSuccess(ArrayList<T> surprises);
}