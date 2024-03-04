package com.lucagiorgetti.surprix.utility.dao

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.lucagiorgetti.surprix.SurprixApplication.Companion.getInstance
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback
import com.lucagiorgetti.surprix.model.Set
import com.lucagiorgetti.surprix.model.Surprise

object SetDao {
    private val sets = getInstance().databaseReference!!.child("sets")
    fun getSetById(setId: String?, listen: CallbackInterface<Set>) {
        sets.child(setId!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val s = snapshot.getValue(Set::class.java)
                    listen.onSuccess(s!!)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                listen.onFailure()
            }
        })
    }

    fun getSurprisesBySet(setClicked: String?, listen: FirebaseListCallback<Surprise>) {
        listen.onStart()
        val surprises = ArrayList<Surprise>()
        sets.child(setClicked!!).child("surprises").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (d in dataSnapshot.children) {
                        SurpriseDao.getSurpriseById(object : CallbackInterface<Surprise> {
                            override fun onStart() {}
                            override fun onSuccess(surprise: Surprise) {
                                surprises.add(surprise)
                                listen.onSuccess(surprises)
                            }

                            override fun onFailure() {}
                        }, d.key!!)
                    }
                } else {
                    listen.onSuccess(surprises)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getAllSets(listen: FirebaseListCallback<Set>) {
        listen.onStart()
        val setList = ArrayList<Set>()
        sets.orderByChild("name").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val s = dataSnapshot.getValue(Set::class.java)!!
                        setList.add(s)
                        listen.onSuccess(setList)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                listen.onFailure()
            }
        })
    }

    fun moveSetIntoAnother(fromId: String?, toId: String?) {
        // get surprises by set
        // update surprises set
        // put surprises into set
        getSetById(toId, object : CallbackInterface<Set> {
            override fun onStart() {}
            override fun onSuccess(set: Set) {
                sets.child(fromId!!).child("surprises").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (d in dataSnapshot.children) {
                                SurpriseDao.getSurpriseById(object : CallbackInterface<Surprise> {
                                    override fun onStart() {}
                                    override fun onSuccess(surprise: Surprise) {
                                        surprise.set_name = set.name
                                        surprise.set_nation = set.nation
                                        surprise.set_year_id = set.year_id
                                        surprise.set_category = set.category
                                        surprise.set_producer_color = set.producer_color
                                        surprise.set_producer_id = set.producer_id
                                        surprise.set_producer_name = set.producer_name
                                        surprise.set_year_name = set.year_desc
                                        surprise.set_year_year = set.year_year
                                        SurpriseDao.addSurprise(surprise)
                                        sets.child(fromId).child("surprises").child(surprise.id!!).setValue(null)
                                        sets.child(toId!!).child("surprises").child(surprise.id!!).setValue(true)
                                    }

                                    override fun onFailure() {}
                                }, d.key!!)
                            }
                        } else {
                            onFailure() // no result
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }

            override fun onFailure() {}
        })
    }
}
