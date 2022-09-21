package com.lucagiorgetti.surprix.listenerInterfaces;

public interface CallbackWithExceptionInterface<T> {
    void onStart();

    void onSuccess(T item);

    void onFailure(Exception exception);
}