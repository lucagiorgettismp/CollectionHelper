package com.lucagiorgetti.surprix.utility;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseCallback;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseListCallback;
import com.lucagiorgetti.surprix.model.MissingDetail;
import com.lucagiorgetti.surprix.model.MissingSurprise;
import com.lucagiorgetti.surprix.model.Producer;
import com.lucagiorgetti.surprix.model.Set;
import com.lucagiorgetti.surprix.model.Surprise;
import com.lucagiorgetti.surprix.model.User;
import com.lucagiorgetti.surprix.model.Year;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Utility which contain all the implementations of methods which needs a connection with Firebase Database.
 * <p>
 * Created by Luca on 13/11/2017.
 */

public class DatabaseUtility {
    private static String username;

    private static DatabaseReference reference = SurprixApplication.getInstance().getDatabaseReference();

    public static void checkUserExisting(final String email, final FirebaseCallback<Boolean> listener) {
        final String emailCod = email.replaceAll("\\.", ",");

        reference.child("emails").child(emailCod).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    listener.onSuccess(true);
                } else {
                    listener.onSuccess(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getCurrentUser(final FirebaseCallback<User> listen, String email) {
        listen.onStart();
        if ( email != null && !email.isEmpty()) {
            String emailCod = email.replaceAll("\\.", ",");
            final String[] username = {null};
            reference.child("emails").child(emailCod).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        username[0] = d.getKey();
                    }
                    if (username[0] != null) {
                        reference.child("users").child(username[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                listen.onSuccess(dataSnapshot.getValue(User.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                listen.onFailure();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    listen.onFailure();
                }
            });
        }
    }

    public static void addMissing(String surpId) {
        reference.child("missings").child(username).child(surpId).setValue(true);
    }

    public static void addDouble(String surpId) {
        reference.child("user_doubles").child(username).child(surpId).setValue(true);
        reference.child("surprise_doubles").child(surpId).child(username).setValue(true);
    }

    public static void removeMissing(String surpId) {
        reference.child("missings").child(username).child(surpId).setValue(null);
    }

    public static void removeDouble(String surpId) {
        reference.child("user_doubles").child(username).child(surpId).setValue(null);
        reference.child("surprise_doubles").child(surpId).child(username).setValue(null);
    }

    public static void getDoubleOwners(String surpId, final FirebaseListCallback<User> listen) {
        listen.onStart();
        final ArrayList<User> owners = new ArrayList<>();
        reference.child("surprise_doubles").child(surpId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        reference.child("users").child(Objects.requireNonNull(d.getKey())).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    User u = snapshot.getValue(User.class);
                                    owners.add(u);
                                }
                                listen.onSuccess(owners);
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

            }
        });
    }

    public static void getDoublesForUsername(final FirebaseListCallback<Surprise> listen) {
        listen.onStart();
        final ArrayList<Surprise> doubles = new ArrayList<>();

        if (username != null){
            reference.child("user_doubles").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            reference.child("surprises").child(Objects.requireNonNull(d.getKey())).orderByChild("code").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Surprise s = snapshot.getValue(Surprise.class);
                                        doubles.add(s);
                                    }
                                    listen.onSuccess(doubles);
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
                }
            });
        }

    }

    public static void getYearsFromProducer(String producerId, final FirebaseListCallback<Year> listen) {
        listen.onStart();

        final ArrayList<Year> years = new ArrayList<>();

        reference.child("producers").child(producerId).child("years").orderByChild("year").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        reference.child("years").child(Objects.requireNonNull(d.getKey())).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Year y = snapshot.getValue(Year.class);
                                    years.add(y);
                                }
                                listen.onSuccess(years);
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
            }
        });
    }

    public static void getProducers(final FirebaseListCallback<Producer> listen) {
        listen.onStart();

        final List<Producer> producers = new ArrayList<>();

        reference.child("producers").orderByChild("order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Producer p = d.getValue(Producer.class);
                        producers.add(p);
                    }
                }

                listen.onSuccess(producers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listen.onFailure();
            }
        });
    }

    public static void getMissingsForUsername(final FirebaseListCallback<MissingSurprise> listen) {
        listen.onStart();

        if (username != null) {
            final ArrayList<MissingSurprise> missings = new ArrayList<>();

            reference.child("missings").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            String key = d.getKey();
                            reference.child("surprises").child(Objects.requireNonNull(key)).orderByChild("description").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Surprise s = snapshot.getValue(Surprise.class);
                                        MissingSurprise mp = new MissingSurprise();
                                        mp.setSurprise(s);
                                        MissingDetail md = null;
                                        try {
                                            md = d.getValue(MissingDetail.class);
                                        } catch (Exception ignored) {

                                        }
                                        mp.setDetail(md);
                                        missings.add(mp);
                                    }
                                    listen.onSuccess(missings);
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
                }
            });
        }
    }

    public static void getSetsFromYear(String yearId, final FirebaseListCallback<Set> listen) {
        listen.onStart();

        final ArrayList<Set> sets = new ArrayList<>();

        reference.child("years").child(yearId).child("sets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {

                        reference.child("sets").child(Objects.requireNonNull(d.getKey())).orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Set s = snapshot.getValue(Set.class);
                                    sets.add(s);
                                }
                                listen.onSuccess(sets);
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

            }
        });
    }

    public static void getSurprisesBySet(String setClicked, final FirebaseListCallback<Surprise> listen) {
        listen.onStart();
        final ArrayList<Surprise> surprises = new ArrayList<>();

        reference.child("sets").child(setClicked).child("surprises").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        reference.child("surprises").child(Objects.requireNonNull(d.getKey())).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Surprise s = snapshot.getValue(Surprise.class);
                                    surprises.add(s);
                                }
                                listen.onSuccess(surprises);
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
            }
        });
    }

    public static void getAllSurprises(final FirebaseListCallback<Surprise> listen) {
        listen.onStart();
        final ArrayList<Surprise> surprises = new ArrayList<>();

        reference.child("surprises").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Surprise s = dataSnapshot.getValue(Surprise.class);
                        surprises.add(s);
                    }
                }
                listen.onSuccess(surprises);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listen.onFailure();
            }
        });
    }

    public static void getAllSets( final FirebaseListCallback<Set> listen) {
        listen.onStart();

        final ArrayList<Set> sets = new ArrayList<>();

        reference.child("sets").orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        Set s = dataSnapshot.getValue(Set.class);
                        sets.add(s);
                    }
                }
                listen.onSuccess(sets);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listen.onFailure();
            }
        });
    }

    public static void addMissingsFromYear(String yearId) {
        reference.child("years").child(yearId).child("sets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        reference.child("sets").child(Objects.requireNonNull(d.getKey())).child("surprises").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        reference.child("surprises").child(Objects.requireNonNull(d.getKey())).child("id").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    String surpId = snapshot.getValue(String.class);
                                                    addMissing(surpId);
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
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void addMissingsFromSet(String setId) {
        reference.child("sets").child(setId).child("surprises").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        reference.child("surprises").child(Objects.requireNonNull(d.getKey())).child("id").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String surpId = snapshot.getValue(String.class);
                                    addMissing(surpId);
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

    public static void generateUser(String email, String username, String nation, Boolean facebook) {
        String emailCod = email.replaceAll("\\.", ",");

        User user = new User(emailCod, username, nation, facebook); //ObjectClass for Users

        reference.child("users").child(username).setValue(user);
        reference.child("emails").child(emailCod).child(username).setValue(true);
    }

    public static void updateUser(String nation) {
        reference.child("users").child(username).child("country").setValue(nation);
    }

    public static void checkUsernameDontExists(String username, final FirebaseCallback<Boolean> listener) {
        reference.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listener.onSuccess(false);
                } else {
                    listener.onSuccess(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    public static void deleteUser(final FirebaseCallback<Boolean> listener) {
        listener.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        reference.child("missings").child(username).setValue(null);
        reference.child("users").child(username).setValue(null);
        if (user != null) {
            reference.child("emails").child(Objects.requireNonNull(user.getEmail()).replaceAll("\\.", ",")).setValue(null);
        }
        getDoublesForUsername(new FirebaseListCallback<Surprise>() {
            @Override
            public void onSuccess(List<Surprise> surprises) {
                if (surprises != null) {
                    for (Surprise double_surp : surprises) {
                        removeDouble(double_surp.getId());
                    }
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {
            }
        });

        if (user != null) {
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            listener.onSuccess(true);
                        }
                    });
        }
        listener.onFailure();
    }

    public static void setUsername(String username) {
        DatabaseUtility.username = username;
    }

    public static void addDetailForMissing(String surpId, MissingDetail notes, FirebaseCallback<Boolean> listen) {
        listen.onStart();
        reference.child("missings").child(username).child(surpId).setValue(notes);
        listen.onSuccess(true);
    }
}
