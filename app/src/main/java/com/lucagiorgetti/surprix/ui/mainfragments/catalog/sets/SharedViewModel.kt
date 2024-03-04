package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private var checked = MutableLiveData<Boolean>()
    var position = 0
    fun isChecked(): LiveData<Boolean> {
        return checked
    }

    fun setChecked(checked: Boolean) {
        this.checked.value = checked
    }

    fun getChecked(): MutableLiveData<Boolean> {
        return checked
    }

    fun setChecked(checked: MutableLiveData<Boolean>) {
        this.checked = checked
    }
}
