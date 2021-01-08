package com.lucagiorgetti.surprix.utility.dao;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.Set;
import com.lucagiorgetti.surprix.model.Year;
import com.lucagiorgetti.surprix.ui.mainfragments.catalog.sets.CatalogSet;

import java.util.ArrayList;

public class YearDao {
    private static DatabaseReference years = SurprixApplication.getInstance().getDatabaseReference().child("years");

    public static void getYearById(String yearId, CallbackInterface<Year> listen) {
        years.child(yearId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Year y = snapshot.getValue(Year.class);
                    listen.onSuccess(y);
                }
                listen.onFailure();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listen.onFailure();
            }
        });
    }

    public static void getYearSets(String yearId, final FirebaseListCallback<Set> listen) {
        listen.onStart();

        final ArrayList<Set> sets = new ArrayList<>();

        years.child(yearId).child("sets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        SetDao.getSetById(d.getKey(), new CallbackInterface<Set>() {
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
                } else {
                    listen.onSuccess(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getYearCatalogSets(String yearId, final FirebaseListCallback<CatalogSet> listen) {
        listen.onStart();

        final ArrayList<CatalogSet> sets = new ArrayList<>();

        years.child(yearId).child("sets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        SetDao.getSetById(d.getKey(), new CallbackInterface<Set>() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(Set item) {
                                new CollectionDao(SurprixApplication.getInstance().getCurrentUser().getUsername()).isSetInCollection(item, new CallbackInterface<Boolean>() {
                                    @Override
                                    public void onStart() {

                                    }

                                    @Override
                                    public void onSuccess(Boolean inCollection) {
                                        sets.add(new CatalogSet(inCollection, item));
                                        listen.onSuccess(sets);
                                    }

                                    @Override
                                    public void onFailure() {

                                    }
                                });
                            }

                            @Override
                            public void onFailure() {

                            }
                        });
                    }
                } else {
                    listen.onSuccess(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
