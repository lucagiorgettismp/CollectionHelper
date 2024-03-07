package com.lucagiorgetti.surprix.utility.dao

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback
import com.lucagiorgetti.surprix.model.Set
import com.lucagiorgetti.surprix.model.Surprise
import java.util.Objects

class MissingListDao(username: String?) {
    private val missingRef: DatabaseReference
    private val userDoubles: DatabaseReference
    private val years: DatabaseReference
    private val sets: DatabaseReference
    private val collectionDao: CollectionDao

    init {
        val reference = SurprixApplication.instance.databaseReference!!

        missingRef = reference.child("missings").child(username!!)
        userDoubles = reference.child("user_doubles")
        years = reference.child("years")
        sets = reference.child("sets")
        collectionDao = CollectionDao(username)
    }

    fun addMissing(surpId: String?) {
        SurpriseDao.getSurpriseById(object : CallbackInterface<Surprise> {
            override fun onStart() {}
            override fun onSuccess(surprise: Surprise) {
                missingRef.child(surpId!!).setValue(if (surprise.isSet_effective_code) surprise.code else "ZZZ_$surpId")
                collectionDao.addMissingInCollectionSet(surpId)
            }

            override fun onFailure() {}
        }, surpId!!)
    }

    fun removeMissing(surpId: String?) {
        missingRef.child(surpId!!).setValue(null)
        collectionDao.removeMissingFromCollection(surpId)
    }

    fun getMissingList(listen: CallbackInterface<Surprise>) {
        missingRef.orderByValue().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (d in dataSnapshot.children) {
                        val surpriseId = if (d.key != null) d.key else ""
                        SurpriseDao.getSurpriseById(object : CallbackInterface<Surprise> {
                            override fun onStart() {}
                            override fun onSuccess(surprise: Surprise) {
                                listen.onSuccess(surprise)
                            }

                            override fun onFailure() {}
                        }, surpriseId!!)
                    }
                } else {
                    listen.onFailure()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getMissingOwnerOtherSurprises(ownerUsername: String?, listen: FirebaseListCallback<Surprise>) {
        listen.onStart()
        val surprises = ArrayList<Surprise>()
        missingRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (d in dataSnapshot.children) {
                        val surpriseId = if (d.key != null) d.key else ""
                        userDoubles.child(ownerUsername!!).child(surpriseId!!).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    SurpriseDao.getSurpriseById(object : CallbackInterface<Surprise> {
                                        override fun onStart() {}
                                        override fun onSuccess(surprise: Surprise) {
                                            surprises.add(surprise)
                                            listen.onSuccess(surprises)
                                        }

                                        override fun onFailure() {}
                                    }, surpriseId)
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                listen.onFailure()
                            }
                        })
                    }
                } else {
                    listen.onSuccess(surprises)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                listen.onFailure()
            }
        })
    }

    fun addMissingsByYear(yearId: String?) {
        years.child(yearId!!).child("sets").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (d in dataSnapshot.children) {
                        sets.child(Objects.requireNonNull(d.key!!)).child("surprises").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (missing in dataSnapshot.children) {
                                        addMissing(missing.key)
                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun addMissingsBySet(setId: String?) {
        sets.child(setId!!).child("surprises").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (d in dataSnapshot.children) {
                        addMissing(d.key)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun clearMissings() {
        missingRef.setValue(null)
    }

    fun removeItemsFromSet(set: Set?) {
        sets.child(set?.id!!).child("surprises").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                removeMissing(snapshot.key)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
