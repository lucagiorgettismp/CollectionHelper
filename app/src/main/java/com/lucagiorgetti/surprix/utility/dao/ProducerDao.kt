package com.lucagiorgetti.surprix.utility.dao

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.lucagiorgetti.surprix.SurprixApplication.Companion.getInstance
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback
import com.lucagiorgetti.surprix.model.Producer
import com.lucagiorgetti.surprix.model.Year

object ProducerDao {
    private val producers = getInstance().databaseReference!!.child("producers")
    fun getProducerYears(producerId: String?, listen: FirebaseListCallback<Year>) {
        listen.onStart()
        val years = ArrayList<Year>()
        producers.child(producerId!!).child("years").orderByChild("year").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (d in dataSnapshot.children) {
                        YearDao.getYearById(d.key, object : CallbackInterface<Year> {
                            override fun onStart() {}
                            override fun onSuccess(item: Year) {
                                years.add(item)
                                listen.onSuccess(years)
                            }

                            override fun onFailure() {}
                        })
                    }
                } else {
                    listen.onSuccess(years)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getProducers(listen: FirebaseListCallback<Producer>) {
        listen.onStart()
        val prods: MutableList<Producer> = ArrayList()
        producers.orderByChild("order").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (d in dataSnapshot.children) {
                        val p = d.getValue(Producer::class.java)!!
                        prods.add(p)
                        listen.onSuccess(prods)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                listen.onFailure()
            }
        })
    }

    fun getProducerById(producerId: String?, listen: CallbackInterface<Producer>) {
        producers.child(producerId!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val producer = snapshot.getValue(Producer::class.java)!!
                    listen.onSuccess(producer)
                }
                listen.onFailure()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
