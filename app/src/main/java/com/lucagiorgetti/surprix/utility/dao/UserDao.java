package com.lucagiorgetti.surprix.utility.dao;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.model.User;

import java.util.Objects;

public class UserDao {
    private static DatabaseReference reference = SurprixApplication.getInstance().getDatabaseReference();
    static DatabaseReference emails = reference.child("emails");
    static DatabaseReference users = reference.child("users");

    public static void userAlreadyRegisteredByEmail(final String email, final CallbackInterface<Boolean> listener) {
        final String emailCod = email.replaceAll("\\.", ",");

        emails.child(emailCod).addListenerForSingleValueEvent(new ValueEventListener() {
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

    public static void getUserByEmail(final CallbackInterface<User> listen, String email) {
        listen.onStart();
        if (email != null && !email.isEmpty()) {
            String emailCod = email.replaceAll("\\.", ",");

            emails.child(emailCod).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String username = "";

                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        username = d.getKey();
                    }
                    if (username != null) {
                        users.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
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

    public static void getUserById(final CallbackInterface<User> listen, String userId) {
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

    public static void generateUser(String email, String username, String nation, Boolean facebook) {
        String emailCod = email.replaceAll("\\.", ",");

        User user = new User(emailCod, username, nation, facebook); //ObjectClass for Users

        users.child(username).setValue(user);
        emails.child(emailCod).child(username).setValue(true);
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
            emails.child(Objects.requireNonNull(firebaseUser.getEmail()).replaceAll("\\.", ",")).setValue(null);
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
}
