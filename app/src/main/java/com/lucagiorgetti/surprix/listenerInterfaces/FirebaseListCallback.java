package com.lucagiorgetti.surprix.listenerInterfaces;

import java.util.List;

public interface FirebaseListCallback<T> {
    void onStart();

    void onSuccess(List<T> items);

    void onFailure();
}