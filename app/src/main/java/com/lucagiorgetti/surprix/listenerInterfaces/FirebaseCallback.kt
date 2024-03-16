package com.lucagiorgetti.surprix.listenerInterfaces

interface FirebaseCallback<T> {
    fun onStart()
    fun onSuccess(items: T)
    fun onFailure()
}