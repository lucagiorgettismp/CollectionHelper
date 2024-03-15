package com.lucagiorgetti.surprix.listenerInterfaces

interface CallbackInterface<T> {
    fun onStart()
    fun onSuccess(item: T)
    fun onFailure()
}