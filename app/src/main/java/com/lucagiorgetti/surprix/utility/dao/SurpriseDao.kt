package com.lucagiorgetti.surprix.utility.dao

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback
import com.lucagiorgetti.surprix.model.Surprise

object SurpriseDao {
    private val surprises = SurprixApplication.instance.databaseReference!!.child("surprises")
    fun getSurpriseById(listen: CallbackInterface<Surprise>, surpId: String) {
        surprises.child(surpId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val s = snapshot.getValue(Surprise::class.java)
                    listen.onSuccess(s!!)
                }
                listen.onFailure()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                listen.onFailure()
            }
        })
    }

    fun getAllSurprises(listen: FirebaseListCallback<Surprise>) {
        listen.onStart()
        val surpriseList = ArrayList<Surprise>()
        surprises.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val s = dataSnapshot.getValue(Surprise::class.java)!!
                        surpriseList.add(s)
                        listen.onSuccess(surpriseList)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                listen.onFailure()
            }
        })
    }

    fun addSurprise(surprise: Surprise) {
        surprise.id?.let { surprises.child(it).setValue(surprise) }
    }
}
