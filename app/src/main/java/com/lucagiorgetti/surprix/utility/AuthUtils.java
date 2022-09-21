package com.lucagiorgetti.surprix.utility;

import android.app.Activity;

import com.facebook.AccessToken;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.listenerInterfaces.LoginFlowCallbackInterface;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackWithExceptionInterface;

import timber.log.Timber;

public class AuthUtils {
    private static final FirebaseAuth fireAuth = FirebaseAuth.getInstance();

    public static void sendPasswordResetEmail(String email, CallbackInterface<Boolean> listener) {
        listener.onStart();
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
        listener.onStart();
        fireAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(activity, task -> {
            if (!task.isSuccessful()) {
                listener.onFailure();
            } else {
                listener.onSuccess(FirebaseAuth.getInstance().getCurrentUser());
            }
        });
    }

    public static void createUserWithEmailAndPassword(Activity activity, String email, String password, LoginFlowCallbackInterface listener) {
        listener.onStart();
        fireAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (!task.isSuccessful()) {
                        listener.onFailure(task.getException());
                    } else {
                        listener.onSuccess();
                    }
                });
    }

    public static void signInWithGoogleToken(Activity activity, String idToken, CallbackWithExceptionInterface<FirebaseUser> listener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        signInWithCredentials(activity, credential, listener);
    }

    public static void signInWithFacebookToken(Activity activity, AccessToken token, CallbackWithExceptionInterface<FirebaseUser> listener) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Timber.d("signInWithFacebookToken:credential: %s", credential);
        signInWithCredentials(activity, credential, listener);
    }

    private static void signInWithCredentials(Activity activity, AuthCredential credential, CallbackWithExceptionInterface<FirebaseUser> listener) {
        fireAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess(fireAuth.getCurrentUser());
                    } else {
                        if(task.getException() instanceof FirebaseAuthUserCollisionException) {

                            listener.onFailure(task.getException());
                        } else {
                            listener.onFailure(new GenericLoginException());
                        }
                    }
                });
    }
}
