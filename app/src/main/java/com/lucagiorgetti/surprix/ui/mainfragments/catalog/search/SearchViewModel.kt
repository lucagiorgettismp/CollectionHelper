package com.lucagiorgetti.surprix.ui.mainfragments.catalog.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback
import com.lucagiorgetti.surprix.model.Set
import com.lucagiorgetti.surprix.model.Surprise
import com.lucagiorgetti.surprix.ui.BaseViewModel
import com.lucagiorgetti.surprix.utility.dao.SetDao
import com.lucagiorgetti.surprix.utility.dao.SurpriseDao

class SearchViewModel(application: Application) : BaseViewModel(application) {
    private var allSurprises: MutableLiveData<MutableList<Surprise>>? = null
    private var allSets: MutableLiveData<List<Set>>? = null

    init {
        setLoading(false)
    }

    val surprises: MutableLiveData<MutableList<Surprise>>
        get() {
            if (allSurprises == null) {
                allSurprises = MutableLiveData()
                loadSurprises()
            }
            return allSurprises!!
        }
    val sets: MutableLiveData<List<Set>>
        get() {
            if (allSets == null) {
                allSets = MutableLiveData()
                loadSets()
            }
            return allSets!!
        }

    private fun loadSets() {
        SetDao.getAllSets(object : FirebaseListCallback<Set> {
            override fun onStart() {
                setLoading(true)
            }

            override fun onSuccess(sets: MutableList<Set>) {
                allSets!!.value = sets
                setLoading(false)
            }

            override fun onFailure() {
                allSets!!.value = null
                setLoading(false)
            }
        })
    }

    private fun loadSurprises() {
        SurpriseDao.getAllSurprises(object : FirebaseListCallback<Surprise> {
            override fun onStart() {
                setLoading(true)
            }

            override fun onSuccess(surprises: MutableList<Surprise>) {
                allSurprises!!.value = surprises
                setLoading(false)
            }

            override fun onFailure() {
                allSurprises!!.value = null
                setLoading(false)
            }
        })
    }
}
