package com.lucagiorgetti.surprix.ui.mainfragments.catalog.years

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback
import com.lucagiorgetti.surprix.model.Year
import com.lucagiorgetti.surprix.ui.BaseViewModel
import com.lucagiorgetti.surprix.utility.dao.ProducerDao

class YearViewModel(application: Application) : BaseViewModel(application) {
    private var allYears: MutableLiveData<List<Year>>? = null

    init {
        setLoading(false)
    }

    fun getYears(producerId: String?): MutableLiveData<List<Year>> {
        if (allYears == null) {
            allYears = MutableLiveData()
            loadYears(producerId)
        }
        return allYears!!
    }

    private fun loadYears(producerId: String?) {
        ProducerDao.getProducerYears(producerId, object : FirebaseListCallback<Year> {
            override fun onStart() {
                setLoading(true)
            }

            override fun onSuccess(years: MutableList<Year>) {
                allYears!!.value = years
                setLoading(false)
            }

            override fun onFailure() {
                setLoading(false)
            }
        })
    }
}