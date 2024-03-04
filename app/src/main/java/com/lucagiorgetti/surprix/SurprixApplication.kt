package com.lucagiorgetti.surprix

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.storage.FirebaseStorage
import com.lucagiorgetti.surprix.model.User
import com.lucagiorgetti.surprix.utility.ForceUpdateChecker
import com.lucagiorgetti.surprix.utility.SystemUtils
import timber.log.Timber

/**
 * Created by Luca Giorgetti on 11/04/2018.
 */
class SurprixApplication : Application() {
    @get:Synchronized
    var firebaseStorage: FirebaseStorage? = null
        get() {
            if (field == null) {
                field = FirebaseStorage.getInstance()
            }
            return field
        }
        private set
    private var firebaseDatabase: FirebaseDatabase? = null

    @get:Synchronized
    var databaseReference: DatabaseReference? = null
        get() {
            if (firebaseDatabase == null) {
                firebaseDatabase = FirebaseDatabase.getInstance()
            }
            if (field == null) {
                field = firebaseDatabase!!.reference
            }
            return field
        }
        private set

    private var firebaseRemoteConfig: FirebaseRemoteConfig? = null

    var currentUser: User? = null
        private set

    fun setUser(currentUser: User?) {
        this.currentUser = currentUser
    }


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

    @Synchronized
    fun getFirebaseRemoteConfig(): FirebaseRemoteConfig {
        if (firebaseRemoteConfig == null) {
            firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
            val remoteConfigDefaults: MutableMap<String?, Any?> = HashMap()
            remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_REQUIRED] = false
            remoteConfigDefaults[ForceUpdateChecker.KEY_CURRENT_VERSION] = "1.0.0"
            remoteConfigDefaults[ForceUpdateChecker.KEY_UPDATE_URL] = "https://play.google.com/store/apps/details?id=com.lucagiorgetti.surprix"
            val settings = FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(60)
                    .build()
            firebaseRemoteConfig!!.setConfigSettingsAsync(settings)
            firebaseRemoteConfig!!.setDefaultsAsync(remoteConfigDefaults)
            firebaseRemoteConfig!!.fetchAndActivate() // fetch every minutes
                    .addOnCompleteListener { task: Task<Boolean?> ->
                        if (task.isSuccessful) {
                            Timber.d("remote config is fetched.")
                        }
                    }
        }
        return firebaseRemoteConfig!!
    }

    companion object {
        @Volatile
        private var instance: SurprixApplication? = null

        @JvmStatic
        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: SurprixApplication().also { instance = it }
                }
        @JvmStatic
        @get:Synchronized
        val surprixContext: Context
            get() = instance!!.applicationContext
    }
}
