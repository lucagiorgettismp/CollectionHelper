package com.lucagiorgetti.surprix.ui.mainfragments.catalog.years

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lucagiorgetti.surprix.SurprixApplication.Companion.getInstance
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback
import com.lucagiorgetti.surprix.model.Year
import com.lucagiorgetti.surprix.ui.BaseViewModel
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode
import com.lucagiorgetti.surprix.utility.dao.CollectionDao
import com.lucagiorgetti.surprix.utility.dao.ProducerDao

class YearViewModel(application: Application) : BaseViewModel(application) {
    private var allYears: MutableLiveData<List<Year>>? = null

    init {
        setLoading(false)
    }

    fun getYears(producerId: String?, mode: CatalogNavigationMode?): MutableLiveData<List<Year>> {
        if (allYears == null) {
            allYears = MutableLiveData()
            loadYears(producerId, mode)
        }
        return allYears!!
    }

    private fun loadYears(producerId: String?, mode: CatalogNavigationMode?) {
        if (mode == CatalogNavigationMode.CATALOG) {
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
        } else {
            CollectionDao(getInstance().currentUser?.username).getCollectionYears(producerId, object : FirebaseListCallback<Year> {
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
}