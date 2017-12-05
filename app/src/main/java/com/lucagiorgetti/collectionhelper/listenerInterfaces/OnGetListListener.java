package com.lucagiorgetti.collectionhelper.listenerInterfaces;

import java.util.ArrayList;

/**
 * Created by Luca on 23/11/2017.
 */

public interface OnGetListListener<T> extends OnGetListener {
    void onSuccess(ArrayList<T> surprises);
}