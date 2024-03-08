package com.lucagiorgetti.surprix.utility.dao

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface
import com.lucagiorgetti.surprix.model.Uid
import com.lucagiorgetti.surprix.model.User
import com.lucagiorgetti.surprix.utility.LoginFlowHelper.AuthMode
import java.util.Objects

object UserDao {
    private val reference = SurprixApplication.instance.databaseReference
    private var users = reference!!.child("users")
    private var uids = reference!!.child("uids")
    fun getUserByUsername(username: String?, listen: CallbackInterface<User?>) {
        users.child(username!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    listen.onSuccess(snapshot.getValue(User::class.java)!!)
                } else {
                    listen.onSuccess(null) // user not found
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                listen.onFailure()
            }
        })
    }

    fun updateUser(nation: String?) {
        val username = SurprixApplication.instance.currentUser?.username
        users.child(username!!).child("country").setValue(nation)
    }

    fun deleteUser(listener: CallbackInterface<Boolean?>) {
        listener.onStart()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val username = SurprixApplication.instance.currentUser?.username
        users.child(username!!).setValue(null)
        if (firebaseUser != null) {
            users.child(Objects.requireNonNull(firebaseUser.uid)).setValue(null)
        }
        MissingListDao(username).clearMissings()
        DoubleListDao(username).clearDoubles()
        firebaseUser?.delete()?.addOnCompleteListener { task: Task<Void?> ->
            if (task.isSuccessful) {
                listener.onSuccess(true)
            }
        }
        listener.onFailure()
    }

    fun getUserByUid(uid: String?, completionListener: CallbackInterface<User?>) {
        uids.child(uid!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val uidVal = dataSnapshot.getValue(Uid::class.java)
                    getUserByUsername(uidVal?.username, object : CallbackInterface<User?> {
                        override fun onStart() {}
                        override fun onSuccess(user: User?) {
                            completionListener.onSuccess(user)
                        }

                        override fun onFailure() {
                            completionListener.onSuccess(null)
                        }
                    })
                } else {
                    completionListener.onSuccess(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        // TODO: to be implemented
    }

    fun newCreateUser(email: String, username: String?, nation: String?, authMode: AuthMode) {
        val emailCod = email.replace("\\.".toRegex(), ",")
        val user = User(emailCod, username, nation, authMode) //ObjectClass for Users
        users.child(username!!).setValue(user)
    }

    fun addUid(uid: Uid) {
        uid.uid?.let { uids.child(it).setValue(uid) }
    }

    fun deleteUser(username: String?) {
        MissingListDao(username).clearMissings()
        DoubleListDao(username).clearDoubles()
        users.child(username!!).setValue(null)
    }
}
