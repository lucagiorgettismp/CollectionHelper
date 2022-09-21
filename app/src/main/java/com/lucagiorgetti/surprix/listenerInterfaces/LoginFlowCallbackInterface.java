package com.lucagiorgetti.surprix.listenerInterfaces;

public interface LoginFlowCallbackInterface {
    void onStart();

    void onSuccess();

    void onFailure(Exception e);
}