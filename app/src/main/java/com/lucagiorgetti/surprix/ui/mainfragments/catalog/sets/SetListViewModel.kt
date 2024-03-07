package com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback
import com.lucagiorgetti.surprix.ui.BaseViewModel
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode
import com.lucagiorgetti.surprix.utility.dao.CollectionDao
import com.lucagiorgetti.surprix.utility.dao.YearDao

class SetListViewModel(application: Application) : BaseViewModel(application) {
    private var allSets: MutableLiveData<List<CatalogSet>>? = null

    init {
        setLoading(false)
    }

    fun getSets(yearId: String?, producerId: String?, mode: CatalogNavigationMode?): MutableLiveData<List<CatalogSet>> {
        if (allSets == null) {
            allSets = MutableLiveData()
            loadSets(yearId, producerId, mode)
        }
        return allSets!!
    }

    fun loadSets(yearId: String?, producerId: String?, mode: CatalogNavigationMode?) {
        if (mode == CatalogNavigationMode.CATALOG) {
            YearDao.getYearCatalogSets(yearId, object : FirebaseListCallback<CatalogSet> {
                override fun onStart() {
                    setLoading(true)
                }

                override fun onSuccess(sets: MutableList<CatalogSet>) {
                    allSets!!.value = sets
                    setLoading(false)
                }

                override fun onFailure() {
                    setLoading(false)
                }
            })
        } else {
            CollectionDao(SurprixApplication.instance.currentUser?.username).getCollectionSets(producerId, yearId, object : FirebaseListCallback<CatalogSet> {
                override fun onStart() {}
                override fun onSuccess(items: MutableList<CatalogSet>) {
                    allSets!!.value = items
                    setLoading(false)
                }

                override fun onFailure() {}
            })
        }
    }
}