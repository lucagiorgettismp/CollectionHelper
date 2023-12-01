package com.lucagiorgetti.surprix.listenerInterfaces;

public interface CallbackInterface<T> {
    void onStart();

    void onSuccess(T item);

    void onFailure();
}