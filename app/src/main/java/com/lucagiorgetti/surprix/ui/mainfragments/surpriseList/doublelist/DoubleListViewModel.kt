package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.doublelist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.model.Surprise
import com.lucagiorgetti.surprix.ui.BaseViewModel
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.SurpiseListFirebaseCallback
import com.lucagiorgetti.surprix.utility.dao.DoubleListDao

class DoubleListViewModel(application: Application) : BaseViewModel(application) {
    private var allDoubleSurprises: MutableLiveData<MutableList<Surprise>> ? = null
    val doublesSurprisesCount = MutableLiveData<Int?>()

    init {
        setLoading(false)
    }

    fun getDoubleSurprises(): MutableLiveData<MutableList<Surprise>>{
        if (allDoubleSurprises == null) {
            allDoubleSurprises = MutableLiveData()
            loadDoubleSurprises()
        }
        return allDoubleSurprises!!
    }

    fun loadDoubleSurprises() {
        val surprises: MutableList<Surprise> = ArrayList()

        DoubleListDao(SurprixApplication.instance.currentUser?.username).getDoubles(object : SurpiseListFirebaseCallback {
            override fun onStart() {
                setLoading(true)
                doublesSurprisesCount.value = null
            }

            override fun onSuccess(doubleSurprises: Surprise) {
                surprises.add(doubleSurprises)
                allDoubleSurprises!!.value = surprises
                setLoading(false)
            }

            override fun onFailure() {
                allDoubleSurprises!!.value = ArrayList()
                setLoading(false)
            }

            override fun onNewData() {
                surprises.clear()
                allDoubleSurprises!!.value = surprises
            }

            override fun onCountDiscovered(count: Int) {
                doublesSurprisesCount.value = count
            }
        })
    }

    fun addDouble(surprise: Surprise, position: Int) {
        val list = allDoubleSurprises!!.value
        list!!.add(position, surprise)
        allDoubleSurprises!!.value = list
    }
}