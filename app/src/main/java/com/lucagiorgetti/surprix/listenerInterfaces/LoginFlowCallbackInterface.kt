package com.lucagiorgetti.surprix.listenerInterfaces

interface LoginFlowCallbackInterface {
    fun onStart()
    fun onSuccess()
    fun onFailure(e: Exception)
}