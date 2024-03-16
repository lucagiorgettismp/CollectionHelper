package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback
import com.lucagiorgetti.surprix.model.Surprise
import com.lucagiorgetti.surprix.ui.BaseViewModel
import com.lucagiorgetti.surprix.utility.dao.SetDao

class SetDetailViewModel(application: Application) : BaseViewModel(application) {
    private var allSurprises: MutableLiveData<List<Surprise>>? = null

    init {
        setLoading(false)
    }

    fun getSurprises(setId: String?): MutableLiveData<List<Surprise>> {
        if (allSurprises == null) {
            allSurprises = MutableLiveData()
            loadSurprises(setId)
        }
        return allSurprises!!
    }

    private fun loadSurprises(setId: String?) {
        SetDao.getSurprisesBySet(setId, object : FirebaseListCallback<Surprise> {
            override fun onStart() {
                setLoading(true)
            }

            override fun onSuccess(surprises: MutableList<Surprise>) {
                allSurprises!!.value = surprises
                setLoading(false)
            }

            override fun onFailure() {
                setLoading(false)
            }
        })
    }
}