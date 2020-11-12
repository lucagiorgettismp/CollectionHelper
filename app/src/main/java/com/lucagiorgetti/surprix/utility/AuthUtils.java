package com.lucagiorgetti.surprix.utility;

import android.app.Activity;

import com.facebook.AccessToken;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackWithExceptionInterface;

public class AuthUtils {
    private static FirebaseAuth fireAuth = FirebaseAuth.getInstance();

    public static void signInWithFacebookToken(Activity activity, AccessToken token, CallbackInterface<FirebaseUser> listener) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        fireAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (!task.isSuccessful()) {
                        listener.onFailure();

                    } else {
                        listener.onSuccess(task.getResult().getUser());
                    }
                });
    }

    public static void sendPasswordResetEmail(String email, CallbackInterface<Boolean> listener) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess(true);
                    } else {
                        listener.onFailure();
                    }
                });
    }

    public static void signInWithEmailAndPassword(Activity activity, String email, String pwd, CallbackInterface<FirebaseUser> listener) {
        fireAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(activity, task -> {
            if (!task.isSuccessful()) {
                listener.onFailure();
            } else {
                listener.onSuccess(FirebaseAuth.getInstance().getCurrentUser());
            }
        });
    }

    public static void createUserWithEmailAndPassword(Activity activity, String email, String password, CallbackWithExceptionInterface listener) {
        fireAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (!task.isSuccessful()) {
                        listener.onFailure(task.getException());
                    } else {
                        listener.onSuccess();
                    }
                });
    }
}
