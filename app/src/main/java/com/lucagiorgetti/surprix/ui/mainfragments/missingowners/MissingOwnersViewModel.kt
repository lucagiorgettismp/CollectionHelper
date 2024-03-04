package com.lucagiorgetti.surprix.ui.mainfragments.missingowners

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lucagiorgetti.surprix.SurprixApplication.Companion.getInstance
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback
import com.lucagiorgetti.surprix.model.User
import com.lucagiorgetti.surprix.ui.BaseViewModel
import com.lucagiorgetti.surprix.utility.dao.DoubleListDao

class MissingOwnersViewModel(application: Application) : BaseViewModel(application) {
    private var missingOwners: MutableLiveData<List<User>>? = null

    init {
        setLoading(false)
    }

    fun getMissingOwners(surpriseId: String?): MutableLiveData<List<User>> {
        if (missingOwners == null) {
            missingOwners = MutableLiveData()
            loadOwners(surpriseId)
        }
        return missingOwners!!
    }

    fun loadOwners(surpriseId: String?) {
        DoubleListDao(getInstance().currentUser?.username).getMissingOwners(surpriseId, object : FirebaseListCallback<User> {
            override fun onSuccess(users: MutableList<User>) {
                val owners = ArrayList<User>()
                val abroadOwners = ArrayList<User>()
                for (u in users) {
                    if (u.country == getInstance().currentUser?.country) {
                        owners.add(u)
                    } else {
                        abroadOwners.add(u)
                    }
                }
                owners.addAll(abroadOwners)
                missingOwners!!.value = owners
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
