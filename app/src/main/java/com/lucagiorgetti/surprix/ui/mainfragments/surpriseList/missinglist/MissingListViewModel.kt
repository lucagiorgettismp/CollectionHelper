package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.missinglist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseCallback
import com.lucagiorgetti.surprix.model.Surprise
import com.lucagiorgetti.surprix.ui.BaseViewModel
import com.lucagiorgetti.surprix.utility.dao.MissingListDao

class MissingListViewModel(application: Application) : BaseViewModel(application) {
    private var allMissingSurprises: MutableLiveData<MutableList<Surprise>?>? = null

    init {
        setLoading(false)
    }

    val missingSurprises: MutableLiveData<MutableList<Surprise>?>
        get() {
            if (allMissingSurprises == null) {
                allMissingSurprises = MutableLiveData()
                loadMissingSurprises()
            }
            return allMissingSurprises!!
        }

    fun loadMissingSurprises() {
        val surprises: MutableList<Surprise> = ArrayList()

        MissingListDao(SurprixApplication.instance.currentUser?.username).getMissingList(object : FirebaseCallback<Surprise> {
            override fun onStart() {
                setLoading(true)
            }

            override fun onSuccess(missingSurprise: Surprise) {
                surprises.add(missingSurprise)
                allMissingSurprises!!.value = surprises
                setLoading(false)
            }

            override fun onFailure() {
                allMissingSurprises!!.value = ArrayList()
                setLoading(false)
            }

            override fun onNewData() {
                surprises.clear()
                allMissingSurprises!!.value = surprises
            }
        })
    }

    fun addMissing(missingSurprise: Surprise) {
        val list = allMissingSurprises!!.value
        list!!.add(missingSurprise)
        allMissingSurprises!!.value = list
    }
}