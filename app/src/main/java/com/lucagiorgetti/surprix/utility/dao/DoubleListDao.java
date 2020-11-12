package com.lucagiorgetti.surprix.utility.dao;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.model.User;

import java.util.ArrayList;
import java.util.Objects;

public class DoubleListDao {
    private String username;
    private DatabaseReference userDoubles;
    private DatabaseReference surpriseDoubleOwners;

    public DoubleListDao(String username) {
        this.username = username;
        DatabaseReference reference = SurprixApplication.getInstance().getDatabaseReference();
        this.userDoubles = reference.child("user_doubles").child(username);
        this.surpriseDoubleOwners = reference.child("surprise_doubles");
    }

    public void addDouble(String surpId) {
        userDoubles.child(surpId).setValue(true);
        surpriseDoubleOwners.child(surpId).child(username).setValue(true);
    }

    public void removeDouble(String surpId) {
        userDoubles.child(surpId).setValue(null);
        surpriseDoubleOwners.child(surpId).child(username).setValue(null);
    }

    public void getMissingOwners(String surpId, final FirebaseListCallback<User> listen) {
        listen.onStart();
        final ArrayList<User> owners = new ArrayList<>();
        surpriseDoubleOwners.child(surpId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        UserDao.getUserByUsername(d.getKey(), new CallbackInterface<User>() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(User user) {
                                owners.add(user);
                                listen.onSuccess(owners);
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

    public void getDoubles(final FirebaseListCallback<Surprise> listen) {
        listen.onStart();
        final ArrayList<Surprise> doubles = new ArrayList<>();

        if (username != null) {
            userDoubles.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                    doubles.add(surprise);
                                    listen.onSuccess(doubles);
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

    }

    public void clearDoubles() {
        userDoubles.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        surpriseDoubleOwners.child(Objects.requireNonNull(d.getKey())).child(username).setValue(null);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userDoubles.setValue(null);
    }
}
