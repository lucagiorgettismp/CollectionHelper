package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList

import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseCallback
import com.lucagiorgetti.surprix.model.Surprise

interface SurpiseListFirebaseCallback : FirebaseCallback<Surprise> {

    fun onNewData()

    fun onCountDiscovered(count: Int)
}