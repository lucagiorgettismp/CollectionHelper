package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.doublelist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lucagiorgetti.surprix.SurprixApplication.Companion.getInstance
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback
import com.lucagiorgetti.surprix.model.Surprise
import com.lucagiorgetti.surprix.ui.BaseViewModel
import com.lucagiorgetti.surprix.utility.dao.DoubleListDao

class DoubleListViewModel(application: Application) : BaseViewModel(application) {
    private var allDoubleSurprises: MutableLiveData<MutableList<Surprise>> ? = null

    init {
        setLoading(false)
    }

    val doubleSurprises: MutableLiveData<MutableList<Surprise>>
        get() {
            if (allDoubleSurprises == null) {
                allDoubleSurprises = MutableLiveData()
                loadDoubleSurprises()
            }
            return allDoubleSurprises!!
        }

    fun loadDoubleSurprises() {
        DoubleListDao(getInstance().currentUser?.username).getDoubles(object : FirebaseListCallback<Surprise> {
            override fun onStart() {
                setLoading(true)
            }

            override fun onSuccess(doubleSurprises: MutableList<Surprise>) {
                allDoubleSurprises!!.value = doubleSurprises
                setLoading(false)
            }

            override fun onFailure() {
                allDoubleSurprises!!.value = null
                setLoading(false)
            }
        })
    }

    fun addDouble(surprise: Surprise, position: Int) {
        val list = allDoubleSurprises!!.value
        list!!.add(position, surprise)
        allDoubleSurprises!!.value = list
    }
}