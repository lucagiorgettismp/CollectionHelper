package com.lucagiorgetti.surprix.listenerInterfaces

interface FirebaseListCallback<T> {
    fun onStart()
    fun onSuccess(items: MutableList<T>)
    fun onFailure()
}