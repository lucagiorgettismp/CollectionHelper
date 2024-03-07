package com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.otherforyou

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback
import com.lucagiorgetti.surprix.model.Surprise
import com.lucagiorgetti.surprix.ui.BaseViewModel
import com.lucagiorgetti.surprix.utility.dao.MissingListDao

class OtherForYouViewModel(application: Application) : BaseViewModel(application) {
    private var otherForYou: MutableLiveData<List<Surprise>>? = null

    init {
        setLoading(false)
    }

    fun getOtherForYou(username: String?): MutableLiveData<List<Surprise>> {
        if (otherForYou == null) {
            otherForYou = MutableLiveData()
            loadOtherForYou(username)
        }
        return otherForYou!!
    }

    fun loadOtherForYou(username: String?) {
        MissingListDao(SurprixApplication.instance.currentUser?.username).getMissingOwnerOtherSurprises(username, object : FirebaseListCallback<Surprise> {
            override fun onSuccess(surprises: MutableList<Surprise>) {
                otherForYou!!.value = surprises
                setLoading(false)
            }

            override fun onStart() {
                setLoading(true)
            }

            override fun onFailure() {
                setLoading(false)
            }
        })
    }
}
