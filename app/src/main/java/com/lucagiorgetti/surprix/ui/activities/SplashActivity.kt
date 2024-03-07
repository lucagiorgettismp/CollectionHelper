package com.lucagiorgetti.surprix.ui.activities

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface
import com.lucagiorgetti.surprix.listenerInterfaces.LoginFlowCallbackInterface
import com.lucagiorgetti.surprix.utility.SystemUtils
import timber.log.Timber


class SplashActivity : AppCompatActivity() {
    private var fireAuth: FirebaseAuth? = null
    private var fireAuthStateListener: AuthStateListener? = null
    private var appUpdateManager: AppUpdateManager? = null
    private var updateCheckListener: CallbackInterface<Boolean>? = null

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        when (val resultCode = result.resultCode) {
            Activity.RESULT_OK -> {
                Timber.v("MyActivity", "Update flow completed!")
            }
            Activity.RESULT_CANCELED -> {
                Timber.v("MyActivity", "User cancelled Update flow!")
            }
            else -> {
                Timber.v("MyActivity", "Update flow failed with resultCode:$resultCode")
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        updateCheckListener = object : CallbackInterface<Boolean> {
            override fun onStart() {}
            override fun onSuccess(item: Boolean) {
                checkSession()
            }

            override fun onFailure() {}
        }

        fireAuth = FirebaseAuth.getInstance()
        //checkForUpdates();
        checkSession()
    }

    private fun checkForUpdates() {
        Timber.d("checkForUpdate: checking for updates")

        appUpdateManager!!.appUpdateInfo.addOnSuccessListener {
            showImmediateUpdate(it)
        }
    }

    private fun showImmediateUpdate(appUpdateInfo: AppUpdateInfo) {
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE ||
            appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS ||
            appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED
        ) {
            appUpdateManager!!.startUpdateFlowForResult(
                // Pass the intent that is returned by 'getAppUpdateInfo()'.
                appUpdateInfo,
                // an activity result launcher registered via registerForActivityResult
                activityResultLauncher,
                // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                // flexible updates.
                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
            )
        }
    }

    private fun checkSession() {
        fireAuthStateListener = AuthStateListener { firebaseAuth: FirebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                SystemUtils.setSessionUser(firebaseUser.uid, object : LoginFlowCallbackInterface {
                    override fun onStart() {}
                    override fun onSuccess() {
                        SystemUtils.openNewActivityWithFinishing(this@SplashActivity, MainActivity::class.java)
                    }

                    override fun onFailure(e: Exception) {
                        SystemUtils.openNewActivityWithFinishing(this@SplashActivity, LoginActivity::class.java)
                    }
                })
            } else {
                SystemUtils.openNewActivityWithFinishing(this, LoginActivity::class.java)
            }
        }
        fireAuth!!.addAuthStateListener(fireAuthStateListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (fireAuthStateListener != null) {
            fireAuth!!.removeAuthStateListener(fireAuthStateListener!!)
        }
    }
}
