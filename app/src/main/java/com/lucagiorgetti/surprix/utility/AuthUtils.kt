package com.lucagiorgetti.surprix.utility

import android.app.Activity
import com.facebook.AccessToken
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackWithExceptionInterface
import com.lucagiorgetti.surprix.listenerInterfaces.LoginFlowCallbackInterface
import timber.log.Timber

object AuthUtils {
    private val fireAuth = FirebaseAuth.getInstance()
    fun sendPasswordResetEmail(email: String?, listener: CallbackInterface<Boolean?>) {
        listener.onStart()
        FirebaseAuth.getInstance().sendPasswordResetEmail(email!!)
                .addOnCompleteListener { task: Task<Void?> ->
                    if (task.isSuccessful) {
                        listener.onSuccess(true)
                    } else {
                        listener.onFailure()
                    }
                }
    }

    fun signInWithEmailAndPassword(activity: Activity?, email: String?, pwd: String?, listener: CallbackInterface<FirebaseUser>) {
        listener.onStart()
        fireAuth.signInWithEmailAndPassword(email!!, pwd!!).addOnCompleteListener(activity!!) { task: Task<AuthResult?> ->
            if (!task.isSuccessful) {
                listener.onFailure()
            } else {
                FirebaseAuth.getInstance().currentUser?.let { listener.onSuccess(it) }
            }
        }
    }

    fun createUserWithEmailAndPassword(activity: Activity?, email: String?, password: String?, listener: LoginFlowCallbackInterface) {
        listener.onStart()
        fireAuth.createUserWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(activity!!) { task: Task<AuthResult?> ->
                    if (!task.isSuccessful) {
                        task.exception?.let { listener.onFailure(it) }
                    } else {
                        listener.onSuccess()
                    }
                }
    }

    fun signInWithGoogleToken(activity: Activity, idToken: String?, listener: CallbackWithExceptionInterface<FirebaseUser>) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        signInWithCredentials(activity, credential, listener)
    }

    fun signInWithFacebookToken(activity: Activity, token: AccessToken, listener: CallbackWithExceptionInterface<FirebaseUser>) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        Timber.d("signInWithFacebookToken:credential: %s", credential)
        signInWithCredentials(activity, credential, listener)
    }

    private fun signInWithCredentials(activity: Activity, credential: AuthCredential, listener: CallbackWithExceptionInterface<FirebaseUser>) {
        fireAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        listener.onSuccess(fireAuth.currentUser!!)
                    } else {
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            listener.onFailure(task.exception as FirebaseAuthUserCollisionException)
                        } else {
                            listener.onFailure(GenericLoginException())
                        }
                    }
                }
    }
}
