package com.lucagiorgetti.surprix.utility.dao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.Producer;
import com.lucagiorgetti.surprix.model.Set;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.model.Year;
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.setdetail.CollectionSurprise;
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets.CatalogSet;

import java.util.ArrayList;
import java.util.List;

public class CollectionDao {
    private final DatabaseReference collectionRef;
    private final DatabaseReference setsRef;

    public CollectionDao(String username) {
        collectionRef = SurprixApplication.getInstance().getDatabaseReference().child("collection").child(username);
        setsRef = SurprixApplication.getInstance().getDatabaseReference().child("sets");
    }

    public void getCollectionProducers(FirebaseListCallback<Producer> listen) {
        collectionRef.child("producers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Producer> producers = new ArrayList<>();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        String producerId = d.getKey();

                        ProducerDao.getProducerById(producerId, new CallbackInterface<Producer>() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(Producer item) {
                                producers.add(item);
                                listen.onSuccess(producers);
                            }

                            @Override
                            public void onFailure() {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getCollectionYears(String producerId, FirebaseListCallback<Year> listen) {
        collectionRef.child("producers").child(producerId).child("years").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Year> years = new ArrayList<>();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        String yearId = d.getKey();

                        YearDao.getYearById(yearId, new CallbackInterface<Year>() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(Year item) {
                                years.add(item);
                                listen.onSuccess(years);
                            }

                            @Override
                            public void onFailure() {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getCollectionSets(String producerId, String yearId, FirebaseListCallback<CatalogSet> listen) {
        collectionRef.child("producers").child(producerId).child("years").child(yearId).child("sets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<CatalogSet> sets = new ArrayList<>();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        String setId = d.getKey();

                        SetDao.getSetById(setId, new CallbackInterface<Set>() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(Set item) {
                                sets.add(new CatalogSet(item, d.child("missings").getChildrenCount() > 0));
                                listen.onSuccess(sets);
                            }

                            @Override
                            public void onFailure() {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void isSetInCollection(Set set, CallbackInterface<Boolean> listen) {
        collectionRef.child("producers").child(set.getProducer_id()).child("years").child(set.getYear_id()).child("sets").child(set.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listen.onSuccess(snapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void isSetInCollection(String setId, CallbackInterface<Boolean> listen) {
        SetDao.getSetById(setId, new CallbackInterface<Set>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Set set) {
                collectionRef.child("producers").child(set.getProducer_id()).child("years").child(set.getYear_id()).child("sets").child(set.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listen.onSuccess(snapshot.exists());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onFailure() {

            }
        });
    }

    public void addSetInCollection(Set set) {
        collectionRef.child("producers").child(set.getProducer_id()).child("years").child(set.getYear_id()).child("sets").child(set.getId()).setValue(true);
    }

    public void addSetInCollection(String setId) {
        SetDao.getSetById(setId, new CallbackInterface<Set>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Set set) {
                addSetInCollection(set);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    public void removeSetInCollection(Set set) {
        // remove set
        collectionRef.child("producers").child(set.getProducer_id()).child("years").child(set.getYear_id()).child("sets").child(set.getId()).setValue(null);

        removeYearIfEmpty(set);
    }

    private void removeYearIfEmpty(Set set) {
        // check if year has sets
        collectionRef.child("producers").child(set.getProducer_id()).child("years").child(set.getYear_id()).child("sets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // remove year
                    collectionRef.child("producers").child(set.getProducer_id()).child("years").child(set.getYear_id()).setValue(null);

                    // check if producer has years
                    collectionRef.child("producers").child(set.getProducer_id()).child("years").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                // delete producer
                                collectionRef.child("producers").child(set.getProducer_id()).setValue(null);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addSetInCollection(String producerId, String yearId, String setId) {
        collectionRef.child("producers").child(producerId).child("years").child(yearId).child("sets").child(setId).setValue(true);
    }

    public void addMissingInCollectionSet(Surprise surprise) {
        collectionRef.child("producers").child(surprise.getSet_producer_id()).child("years").child(surprise.getSet_year_id()).child("sets").child(surprise.getSet_id()).child("missings").child(surprise.getId()).setValue(true);
    }


    public void addMissingInCollectionSet(String surpId) {
        SurpriseDao.getSurpriseById(new CallbackInterface<Surprise>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Surprise item) {
                addMissingInCollectionSet(item);
            }

            @Override
            public void onFailure() {

            }
        }, surpId);
    }

    public void removeMissingFromCollection(String surpId) {
        SurpriseDao.getSurpriseById(new CallbackInterface<Surprise>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Surprise surprise) {
                collectionRef.child("producers").child(surprise.getSet_producer_id()).child("years").child(surprise.getSet_year_id()).child("sets").child(surprise.getSet_id()).child("missings").child(surprise.getId()).setValue(null);
            }

            @Override
            public void onFailure() {

            }
        }, surpId);
    }

    public void getSetItemsWithMissing(String
                                               setId, FirebaseListCallback<CollectionSurprise> listen) {
        final ArrayList<CollectionSurprise> surprises = new ArrayList<>();

        setsRef.child(setId).child("surprises").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String surpriseId = snapshot.getKey();

                SurpriseDao.getSurpriseById(new CallbackInterface<Surprise>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Surprise surprise) {
                        collectionRef.child("producers").child(surprise.getSet_producer_id()).child("years").child(surprise.getSet_year_id()).child("sets").child(surprise.getSet_id()).child("missings").child(surprise.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                surprises.add(new CollectionSurprise(snapshot.exists(), surprise));
                                listen.onSuccess(surprises);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onFailure() {

                    }
                }, surpriseId);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listen.onFailure();
            }
        });
    }
}
