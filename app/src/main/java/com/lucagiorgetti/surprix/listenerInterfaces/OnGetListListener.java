package com.lucagiorgetti.surprix.listenerInterfaces;

import java.util.ArrayList;

/**
 * Interface which contains methods to be implemented for a query with Firebase
 * if a generic list is expected
 * <p>
 * Created by Luca on 23/11/2017.
 */

public interface OnGetListListener<T> extends OnGetListener {
    void onSuccess(ArrayList<T> surprises);
}