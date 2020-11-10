package com.lucagiorgetti.surprix.utility.dao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.model.Uid;
import com.lucagiorgetti.surprix.model.User;

import java.util.Objects;

public class UserDao {
    private static DatabaseReference reference = SurprixApplication.getInstance().getDatabaseReference();
    static DatabaseReference users = reference.child("users");
    static DatabaseReference uids = reference.child("uids");

    public static void getUserByUsername(final CallbackInterface<User> listen, String userId) {
        users.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listen.onSuccess(snapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listen.onFailure();
            }
        });
    }

    public static void updateUser(String nation) {
        String username = SurprixApplication.getInstance().getCurrentUser().getUsername();
        users.child(username).child("country").setValue(nation);
    }

    public static void checkUsernameAvailable(String username, final CallbackInterface<Boolean> listener) {
        users.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
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

    public static void deleteUser(final CallbackInterface<Boolean> listener) {
        listener.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String username = SurprixApplication.getInstance().getCurrentUser().getUsername();

        users.child(username).setValue(null);
        if (firebaseUser != null) {
            users.child(Objects.requireNonNull(firebaseUser.getUid())).setValue(null);
        }

        new MissingListDao(username).clearMissings();
        new DoubleListDao(username).clearDoubles();

        if (firebaseUser != null) {
            firebaseUser.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            listener.onSuccess(true);
                        }
                    });
        }
        listener.onFailure();
    }

    public static void getRegisteredUser(String uid, CallbackInterface<User> completionListener) {
        uids.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Uid uidVal = dataSnapshot.getValue(Uid.class);
                    getUserByUsername(new CallbackInterface<User>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(User user) {
                            completionListener.onSuccess(user);
                        }

                        @Override
                        public void onFailure() {
                            completionListener.onSuccess(null);
                        }
                    }, uidVal.getUsername());

                } else {
                    completionListener.onSuccess(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // TODO: to be implemented
    }

    public static void newCreateUser(String email, String username, String nation, boolean fromFacebook) {
        String emailCod = email.replaceAll("\\.", ",");
        User user = new User(emailCod, username, nation, fromFacebook); //ObjectClass for Users
        users.child(username).setValue(user);
    }

    public static void addUid(String uid, String username, boolean fromFacebook) {
        uids.child(uid).setValue(new Uid(uid, username, fromFacebook));
    }
}
