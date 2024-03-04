package com.lucagiorgetti.surprix.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    val isLoading: MutableLiveData<Boolean>

    init {
        isLoading = MutableLiveData()
    }

    fun setLoading(loading: Boolean) {
        isLoading.value = loading
    }
}
