package com.lucagiorgetti.surprix.utility.dao;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.Producer;
import com.lucagiorgetti.surprix.model.Set;
import com.lucagiorgetti.surprix.model.Year;

import java.util.ArrayList;
import java.util.List;

public class CollectionDao {
    private DatabaseReference collectionRef;

    public CollectionDao(String username) {
        collectionRef = SurprixApplication.getInstance().getDatabaseReference().child("collection").child(username);
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

    public void getCollectionSets(String producerId, String yearId, FirebaseListCallback<Set> listen) {
        collectionRef.child("producers").child(producerId).child("years").child(yearId).child("sets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Set> sets = new ArrayList<>();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        String setId = d.getKey();

                        SetDao.getSetById(setId, new CallbackInterface<Set>() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(Set item) {
                                sets.add(item);
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

        // check if year has sets
        collectionRef.child("producers").child(set.getProducer_id()).child("years").child(set.getYear_id()).child("sets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    // remove year
                    collectionRef.child("producers").child(set.getProducer_id()).child("years").child(set.getYear_id()).setValue(null);

                    // check if producer has years
                    collectionRef.child("producers").child(set.getProducer_id()).child("years").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()){
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

    public void addSetInCollection(String producerId, String yearId, String setId){
        collectionRef.child("producers").child(producerId).child("years").child(yearId).child("sets").child(setId).setValue(true);
    }

}
