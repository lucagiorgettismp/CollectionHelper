package com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback
import com.lucagiorgetti.surprix.model.Surprise
import com.lucagiorgetti.surprix.ui.BaseViewModel
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.CatalogNavigationMode
import com.lucagiorgetti.surprix.utility.dao.CollectionDao
import com.lucagiorgetti.surprix.utility.dao.SetDao
import java.util.stream.Collectors

class SetDetailViewModel(application: Application) : BaseViewModel(application) {
    private var allSurprises: MutableLiveData<List<CollectionSurprise>>? = null

    init {
        setLoading(false)
    }

    fun getSurprises(setId: String?, mode: CatalogNavigationMode?): MutableLiveData<List<CollectionSurprise>> {
        if (allSurprises == null) {
            allSurprises = MutableLiveData()
            loadSurprises(setId, mode)
        }
        return allSurprises!!
    }

    private fun loadSurprises(setId: String?, mode: CatalogNavigationMode?) {
        if (mode == CatalogNavigationMode.CATALOG) {
            SetDao.getSurprisesBySet(setId, object : FirebaseListCallback<Surprise> {
                override fun onStart() {
                    setLoading(true)
                }

                override fun onSuccess(surprises: MutableList<Surprise>) {
                    allSurprises!!.value = surprises.stream().map { surprise: Surprise -> CollectionSurprise(surprise) }.collect(Collectors.toList())
                    setLoading(false)
                }

                override fun onFailure() {
                    setLoading(false)
                }
            })
        } else {
            CollectionDao(SurprixApplication.instance.currentUser?.username).getSetItemsWithMissing(setId, object : FirebaseListCallback<CollectionSurprise> {
                override fun onStart() {
                    setLoading(true)
                }

                override fun onSuccess(items: MutableList<CollectionSurprise>) {
                    allSurprises!!.value = items
                    setLoading(false)
                }

                override fun onFailure() {
                    setLoading(false)
                }
            })
        }
    }
}