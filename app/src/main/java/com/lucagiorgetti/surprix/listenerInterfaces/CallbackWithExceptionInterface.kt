package com.lucagiorgetti.surprix.listenerInterfaces

interface CallbackWithExceptionInterface<T> {
    fun onStart()
    fun onSuccess(item: T)
    fun onFailure(exception: Exception)
}