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
import com.lucagiorgetti.surprix.model.Surprise;

import java.util.ArrayList;

public class SetDao {
    private static DatabaseReference sets = SurprixApplication.getInstance().getDatabaseReference().child("sets");

    public static void getSetById(String setId, CallbackInterface<Set> listen) {
        sets.child(setId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Set s = snapshot.getValue(Set.class);
                    listen.onSuccess(s);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listen.onFailure();
            }
        });
    }

    public static void getSurprisesBySet(String setClicked, final FirebaseListCallback<Surprise> listen) {
        listen.onStart();
        final ArrayList<Surprise> surprises = new ArrayList<>();

        sets.child(setClicked).child("surprises").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        SurpriseDao.getSurpriseById(new CallbackInterface<Surprise>() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(Surprise surprise) {
                                surprises.add(surprise);
                                listen.onSuccess(surprises);
                            }

                            @Override
                            public void onFailure() {

                            }
                        }, d.getKey());
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

    public static void getAllSets(final FirebaseListCallback<Set> listen) {
        listen.onStart();

        final ArrayList<Set> setList = new ArrayList<>();

        sets.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Set s = dataSnapshot.getValue(Set.class);
                        setList.add(s);
                        listen.onSuccess(setList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listen.onFailure();
            }
        });
    }

    public static void moveSetIntoAnother(String fromId, String toId) {
        // get surprises by set
        // update surprises set
        // put surprises into set

        getSetById(toId, new CallbackInterface<Set>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Set toSet) {

                sets.child(fromId).child("surprises").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                SurpriseDao.getSurpriseById(new CallbackInterface<Surprise>() {
                                    @Override
                                    public void onStart() {

                                    }

                                    @Override
                                    public void onSuccess(Surprise surprise) {
                                        surprise.setSet_name(toSet.getName());
                                        surprise.setSet_nation(toSet.getNation());
                                        surprise.setSet_year_id(toSet.getYear_id());
                                        surprise.setSet_category(toSet.getCategory());
                                        surprise.setSet_producer_color(toSet.getProducer_color());
                                        surprise.setSet_producer_id(toSet.getProducer_id());
                                        surprise.setSet_producer_name(toSet.getProducer_name());
                                        surprise.setSet_year_name(toSet.getYear_desc());
                                        surprise.setSet_year_year(toSet.getYear_year());

                                        SurpriseDao.addSurprise(surprise);

                                        sets.child(fromId).child("surprises").child(surprise.getId()).setValue(null);
                                        sets.child(toId).child("surprises").child(surprise.getId()).setValue(true);
                                    }

                                    @Override
                                    public void onFailure() {

                                    }
                                }, d.getKey());
                            }
                        } else {
                            onSuccess(null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onFailure() {

            }
        });

    }
}
