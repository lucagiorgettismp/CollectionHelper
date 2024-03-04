package com.lucagiorgetti.surprix.ui.mainfragments.catalog.producers

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lucagiorgetti.surprix.SurprixApplication.Companion.getInstance
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback
import com.lucagiorgetti.surprix.model.Producer
import com.lucagiorgetti.surprix.ui.BaseViewModel
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode
import com.lucagiorgetti.surprix.utility.dao.CollectionDao
import com.lucagiorgetti.surprix.utility.dao.ProducerDao

class ProducerViewModel(application: Application) : BaseViewModel(application) {
    private var allProducers: MutableLiveData<List<Producer>?>? = null

    init {
        setLoading(false)
    }

    fun getProducers(mode: CatalogNavigationMode): MutableLiveData<List<Producer>?> {
        if (allProducers == null) {
            allProducers = MutableLiveData()
            loadProducers(mode)
        }
        return allProducers!!
    }

    private fun loadProducers(mode: CatalogNavigationMode) {
        if (mode == CatalogNavigationMode.CATALOG) {
            ProducerDao.getProducers(object : FirebaseListCallback<Producer> {
                override fun onStart() {
                    setLoading(true)
                }

                override fun onSuccess(producers: MutableList<Producer>) {
                    allProducers!!.value = producers
                    setLoading(false)
                }

                override fun onFailure() {
                    allProducers!!.value = null
                    setLoading(false)
                }
            })
        } else {
            CollectionDao(getInstance().currentUser?.username).getCollectionProducers(object : FirebaseListCallback<Producer> {
                override fun onStart() {
                    setLoading(true)
                }

                override fun onSuccess(items: MutableList<Producer>) {
                    allProducers!!.value = items
                    setLoading(false)
                }

                override fun onFailure() {
                    setLoading(false)
                }
            })
        }
    }
}