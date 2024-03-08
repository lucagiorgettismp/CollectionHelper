package com.lucagiorgetti.surprix.utility.dao

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback
import com.lucagiorgetti.surprix.model.Surprise
import com.lucagiorgetti.surprix.model.User
import java.util.Objects

class DoubleListDao(private val username: String?) {
    private val userDoubles: DatabaseReference
    private val surpriseDoubleOwners: DatabaseReference

    init {
        val reference = SurprixApplication.instance.databaseReference!!
        userDoubles = reference.child("user_doubles").child(username!!)
        surpriseDoubleOwners = reference.child("surprise_doubles")
    }

    fun addDouble(surpId: String?) {
        SurpriseDao.getSurpriseById(object : CallbackInterface<Surprise> {
            override fun onStart() {}
            override fun onSuccess(surprise: Surprise) {
                userDoubles.child(surpId!!).setValue(if (surprise.isSet_effective_code) surprise.code else "ZZZ_$surpId")
                surpriseDoubleOwners.child(surpId).child(username!!).setValue(true)
            }

            override fun onFailure() {}
        }, surpId!!)
    }

    fun removeDouble(surpId: String?) {
        userDoubles.child(surpId!!).setValue(null)
        surpriseDoubleOwners.child(surpId).child(username!!).setValue(null)
    }

    fun getMissingOwners(surpId: String?, listen: FirebaseListCallback<User>) {
        listen.onStart()
        val owners = ArrayList<User>()
        surpriseDoubleOwners.child(surpId!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (d in dataSnapshot.children) {
                        UserDao.getUserByUsername(d.key, object : CallbackInterface<User?> {
                            override fun onStart() {}
                            override fun onSuccess(user: User?) {
                                owners.add(user!!)
                                listen.onSuccess(owners)
                            }

                            override fun onFailure() {}
                        })
                    }
                } else {
                    listen.onSuccess(owners)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getDoubles(listen: FirebaseListCallback<Surprise>) {
        listen.onStart()
        val doubles = ArrayList<Surprise>()
        if (username != null) {
            userDoubles.orderByValue().addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (d in dataSnapshot.children) {
                            SurpriseDao.getSurpriseById(object : CallbackInterface<Surprise> {
                                override fun onStart() {}
                                override fun onSuccess(surprise: Surprise) {
                                    doubles.add(surprise)
                                    listen.onSuccess(doubles)
                                }

                                override fun onFailure() {}
                            }, d.key!!)
                        }
                    } else {
                        listen.onSuccess(ArrayList())
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }

    fun clearDoubles() {
        userDoubles.child(username!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (d in dataSnapshot.children) {
                        surpriseDoubleOwners.child(Objects.requireNonNull(d.key!!)).child(username).setValue(null)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        userDoubles.setValue(null)
    }
}
