package com.lucagiorgetti.surprix.listenerInterfaces;

import java.util.List;

public interface FirebaseCallback<T> {
    void onStart();

    void onSuccess(List<T> items);

    void onFailure();
}