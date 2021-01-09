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
import com.lucagiorgetti.surprix.model.Missing;
import com.lucagiorgetti.surprix.model.Set;
import com.lucagiorgetti.surprix.model.Surprise;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class MissingListDao {

    private DatabaseReference missingRef;
    private DatabaseReference userDoubles;
    private DatabaseReference years;
    private DatabaseReference sets;
    private CollectionDao collectionDao;

    public MissingListDao(String username) {
        missingRef = SurprixApplication.getInstance().getDatabaseReference().child("missings").child(username);
        userDoubles = SurprixApplication.getInstance().getDatabaseReference().child("user_doubles");
        years = SurprixApplication.getInstance().getDatabaseReference().child("years");
        sets = SurprixApplication.getInstance().getDatabaseReference().child("sets");
        collectionDao = new CollectionDao(username);
    }

    public static void fixDb() {
        SurprixApplication.getInstance().getDatabaseReference().child("missings").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Timber.d("Ci passa");
                String userId = snapshot.getKey();
                CollectionDao collectionDao = new CollectionDao(userId);
                for (DataSnapshot d : snapshot.getChildren()) {
                    String surpriseId = d.getKey() != null ? d.getKey() : "";

                    SurpriseDao.getSurpriseById(new CallbackInterface<Surprise>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(Surprise surprise) {
                            collectionDao.addSetInCollection(surprise.getSet_producer_id(), surprise.getSet_year_id(), surprise.getSet_id());
                        }

                        @Override
                        public void onFailure() {

                        }
                    }, surpriseId);
                }
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

            }
        });
    }

    public static void fixDb2() {
        SurprixApplication.getInstance().getDatabaseReference().child("missings").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String userId = snapshot.getKey();
                CollectionDao collectionDao = new CollectionDao(userId);
                for (DataSnapshot d : snapshot.getChildren()) {
                    String surpriseId = d.getKey() != null ? d.getKey() : "";

                    SurpriseDao.getSurpriseById(new CallbackInterface<Surprise>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(Surprise surprise) {
                            collectionDao.addMissingInCollectionSet(surprise);
                        }

                        @Override
                        public void onFailure() {

                        }
                    }, surpriseId);
                }
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

            }
        });
    }

    public void addMissing(String surpId) {
        Missing missing = new Missing(surpId);
        missingRef.child(surpId).setValue(missing);
        collectionDao.addMissingInCollectionSet(surpId);
    }

    public void removeMissing(String surpId) {
        missingRef.child(surpId).setValue(null);
        collectionDao.removeMissingFromCollection(surpId);
    }

    public void getMissingList(FirebaseListCallback<Surprise> listen) {
        missingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Surprise> surprises = new ArrayList<>();
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        String surpriseId = d.getKey() != null ? d.getKey() : "";

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
                        }, surpriseId);
                    }
                } else {
                    listen.onSuccess(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getMissingOwnerOtherSurprises(String ownerUsername, FirebaseListCallback<Surprise> listen) {
        listen.onStart();

        final ArrayList<Surprise> surprises = new ArrayList<>();

        missingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        String surpriseId = d.getKey() != null ? d.getKey() : "";

                        userDoubles.child(ownerUsername).child(surpriseId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
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
                                    }, surpriseId);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                listen.onFailure();
                            }
                        });
                    }
                } else {
                    listen.onSuccess(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listen.onFailure();
            }
        });
    }

    public void addMissingsByYear(String yearId) {
        years.child(yearId).child("sets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        sets.child(Objects.requireNonNull(d.getKey())).child("surprises").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        addMissing(d.getKey());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addMissingsBySet(String setId) {
        sets.child(setId).child("surprises").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        addMissing(d.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void setNote(String surpId, String notes, CallbackInterface<Boolean> listen) {
        listen.onStart();
        Missing missing = new Missing(surpId, notes);
        missingRef.child(surpId).setValue(missing);
        listen.onSuccess(true);
    }

    public void clearMissings() {
        missingRef.setValue(null);
    }

    public void removeItemsFromSet(Set set) {
        sets.child(set.getId()).child("surprises").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                removeMissing(snapshot.getKey());
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
            }
        });
    }

}
