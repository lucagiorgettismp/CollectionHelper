package com.lucagiorgetti.surprix

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.storage.FirebaseStorage
import com.lucagiorgetti.surprix.model.User
import com.lucagiorgetti.surprix.utility.SystemUtils
import timber.log.Timber

/**
 * Created by Luca Giorgetti on 11/04/2018.
 */
class SurprixApplication : Application() {
    private var firebaseDatabase: FirebaseDatabase? = null

    @get:Synchronized
    var firebaseStorage: FirebaseStorage? = null
        get() {
            if (field == null) {
                field = FirebaseStorage.getInstance()
            }
            return field
        }
        private set

    @get:Synchronized
    var databaseReference: DatabaseReference? = null
        get() {
            if (firebaseDatabase == null){
                firebaseDatabase = FirebaseDatabase.getInstance()
            }

            if (field == null){
                field = firebaseDatabase!!.reference
            }

            return field
        }
        private set

    var currentUser: User? = null

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        val dark = SystemUtils.darkThemePreference

        AppCompatDelegate.setDefaultNightMode(if (dark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        lateinit var instance: SurprixApplication
    }
}
