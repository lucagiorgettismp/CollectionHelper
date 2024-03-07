package com.lucagiorgetti.surprix.utility.dao

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback
import com.lucagiorgetti.surprix.model.CollectionSet
import com.lucagiorgetti.surprix.model.Producer
import com.lucagiorgetti.surprix.model.Set
import com.lucagiorgetti.surprix.model.Surprise
import com.lucagiorgetti.surprix.model.Year
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail.CollectionSurprise
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets.CatalogSet

class CollectionDao(username: String?) {
    private val collectionRef: DatabaseReference
    private val setsRef: DatabaseReference

    init {
        val reference = SurprixApplication.instance.databaseReference!!
        collectionRef = reference.child("collection").child(username!!)
        setsRef = reference.child("sets")
    }

    fun getCollectionProducers(listen: FirebaseListCallback<Producer>) {
        listen.onStart()
        collectionRef.child("producers").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val producers: MutableList<Producer> = ArrayList()
                    for (d in snapshot.children) {
                        val producerId = d.key
                        ProducerDao.getProducerById(producerId, object : CallbackInterface<Producer> {
                            override fun onStart() {}
                            override fun onSuccess(item: Producer) {
                                producers.add(item)
                                listen.onSuccess(producers)
                            }

                            override fun onFailure() {}
                        })
                    }
                } else {
                    listen.onSuccess(ArrayList())
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getCollectionYears(producerId: String?, listen: FirebaseListCallback<Year>) {
        collectionRef.child("producers").child(producerId!!).child("years").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val years: MutableList<Year> = ArrayList()
                    for (d in snapshot.children) {
                        val yearId = d.key
                        YearDao.getYearById(yearId, object : CallbackInterface<Year> {
                            override fun onStart() {}
                            override fun onSuccess(item: Year) {
                                years.add(item)
                                listen.onSuccess(years)
                            }

                            override fun onFailure() {}
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getCollectionSets(producerId: String?, yearId: String?, listen: FirebaseListCallback<CatalogSet>) {
        collectionRef.child("producers").child(producerId!!).child("years").child(yearId!!).child("sets").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val sets: MutableList<CatalogSet> = ArrayList()
                    for (d in snapshot.children) {
                        val setId = d.key
                        SetDao.getSetById(setId, object : CallbackInterface<Set> {
                            override fun onStart() {}
                            override fun onSuccess(item: Set) {
                                sets.add(CatalogSet(item, d.child("missings").childrenCount > 0))
                                listen.onSuccess(sets)
                            }

                            override fun onFailure() {}
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun isSetInCollection(set: Set, listen: CallbackInterface<Boolean>) {
        set.producer_id?.let {
            set.year_id?.let { it1 ->
                collectionRef.child("producers").child(it).child("years").child(it1).child("sets").child(set.id!!).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listen.onSuccess(snapshot.exists())
                }

                override fun onCancelled(error: DatabaseError) {}
            })
            }
        }
    }

    fun isSetInCollection(setId: String?, listen: CallbackInterface<Boolean>) {
        SetDao.getSetById(setId, object : CallbackInterface<Set> {
            override fun onStart() {}
            override fun onSuccess(set: Set) {
                collectionRef.child("producers").child(set.producer_id!!).child("years").child(set.year_id!!).child("sets").child(set.id!!).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        listen.onSuccess(snapshot.exists())
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            }

            override fun onFailure() {}
        })
    }

    fun addSetInCollection(set: Set?) {
        val collectionSet = CollectionSet(set?.id)
        collectionRef.child("producers").child(set?.producer_id!!).child("years").child(set.year_id!!).child("sets").child(set.id!!).setValue(collectionSet)
    }

    fun addSetInCollection(setId: String?) {
        SetDao.getSetById(setId, object : CallbackInterface<Set> {
            override fun onStart() {}
            override fun onSuccess(set: Set) {
                addSetInCollection(set)
            }

            override fun onFailure() {}
        })
    }

    fun removeSetInCollection(set: Set?) {
        // remove set
        collectionRef.child("producers").child(set?.producer_id!!).child("years").child(set.year_id!!).child("sets").child(set.id!!).setValue(null)
        removeYearIfEmpty(set)
    }

    private fun removeYearIfEmpty(set: Set?) {
        // check if year has sets
        collectionRef.child("producers").child(set?.producer_id!!).child("years").child(set.year_id!!).child("sets").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    // remove year
                    collectionRef.child("producers").child(set.producer_id!!).child("years").child(set.year_id!!).setValue(null)

                    // check if producer has years
                    collectionRef.child("producers").child(set.producer_id!!).child("years").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists()) {
                                // delete producer
                                collectionRef.child("producers").child(set.producer_id!!).setValue(null)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun addSetInCollection(producerId: String?, yearId: String?, setId: String?) {
        collectionRef.child("producers").child(producerId!!).child("years").child(yearId!!).child("sets").child(setId!!).setValue(true)
    }

    fun addMissingInCollectionSet(surprise: Surprise) {
        collectionRef.child("producers").child(surprise.set_producer_id!!).child("years").child(surprise.set_year_id!!).child("sets").child(surprise.set_id!!).child("missings").child(surprise.id!!).setValue(true)
    }

    fun addMissingInCollectionSet(surpId: String?) {
        SurpriseDao.getSurpriseById(object : CallbackInterface<Surprise> {
            override fun onStart() {}
            override fun onSuccess(item: Surprise) {
                addMissingInCollectionSet(item)
            }

            override fun onFailure() {}
        }, surpId!!)
    }

    fun removeMissingFromCollection(surpId: String?) {
        SurpriseDao.getSurpriseById(object : CallbackInterface<Surprise> {
            override fun onStart() {}
            override fun onSuccess(surprise: Surprise) {
                collectionRef.child("producers").child(surprise.set_producer_id!!).child("years").child(surprise.set_year_id!!).child("sets").child(surprise.set_id!!).child("missings").child(surprise.id!!).setValue(null)
            }

            override fun onFailure() {}
        }, surpId!!)
    }

    fun getSetItemsWithMissing(setId: String?, listen: FirebaseListCallback<CollectionSurprise>) {
        val surprises = ArrayList<CollectionSurprise>()
        setsRef.child(setId!!).child("surprises").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val surpriseId = snapshot.key
                SurpriseDao.getSurpriseById(object : CallbackInterface<Surprise> {
                    override fun onStart() {}
                    override fun onSuccess(surprise: Surprise) {
                        collectionRef.child("producers").child(surprise.set_producer_id!!).child("years").child(surprise.set_year_id!!).child("sets").child(surprise.set_id!!).child("missings").child(surprise.id!!).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                surprises.add(CollectionSurprise(snapshot.exists(), surprise))
                                listen.onSuccess(surprises)
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }

                    override fun onFailure() {}
                }, surpriseId!!)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                listen.onFailure()
            }
        })
    }
}
