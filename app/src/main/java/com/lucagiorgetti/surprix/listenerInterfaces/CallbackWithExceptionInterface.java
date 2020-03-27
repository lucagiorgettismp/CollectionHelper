package com.lucagiorgetti.surprix.listenerInterfaces;

public interface CallbackWithExceptionInterface {
    void onStart();

    void onSuccess();

    void onFailure(Exception e);
}