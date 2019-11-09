package com.lucagiorgetti.surprix.listenerInterfaces;

import java.util.List;

public interface FirebaseCallback<T> {
    void onStart();

    void onSuccess(T item);

    void onFailure();
}