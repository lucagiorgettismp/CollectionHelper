package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback
import com.lucagiorgetti.surprix.ui.BaseViewModel
import com.lucagiorgetti.surprix.utility.dao.YearDao
import com.lucagiorgetti.surprix.model.Set

class SetListViewModel(application: Application) : BaseViewModel(application) {
    private var allSets: MutableLiveData<List<Set>>? = null

    init {
        setLoading(false)
    }

    fun getSets(yearId: String?): MutableLiveData<List<Set>> {
        if (allSets == null) {
            allSets = MutableLiveData()
            loadSets(yearId)
        }
        return allSets!!
    }

    fun loadSets(yearId: String?) {
        YearDao.getYearSets(yearId, object : FirebaseListCallback<Set> {
            override fun onStart() {
                setLoading(true)
            }

            override fun onSuccess(sets: MutableList<Set>) {
                allSets!!.value = sets
                setLoading(false)
            }

            override fun onFailure() {
                setLoading(false)
            }
        })
    }
}