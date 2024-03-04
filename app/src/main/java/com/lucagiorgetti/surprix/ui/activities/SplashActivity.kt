package com.lucagiorgetti.surprix.ui.activities

import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
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
        val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                Timber.d("checkForUpdate: update available")
                if (appUpdateInfo.updatePriority() >= 3
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    // Request the update.
                    try {
                        appUpdateManager!!.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, UPDATE_REQUEST_CODE)
                    } catch (e: SendIntentException) {
                        e.printStackTrace()
                    }
                } else {
                    Timber.d("checkForUpdate: unmanaged priority: %s, updateTypeSupported: %b", appUpdateInfo.updatePriority(), appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
                    updateCheckListener!!.onSuccess(true)
                }
            } else {
                Timber.d("checkForUpdate: no updates found")
                updateCheckListener!!.onSuccess(true)
            }
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

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        if (fireAuthStateListener != null) {
            fireAuth!!.removeAuthStateListener(fireAuthStateListener!!)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                checkForUpdates()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager
                ?.appUpdateInfo
                ?.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
                    if (appUpdateInfo.updateAvailability()
                            == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        // If an in-app update is already running, resume the update.
                        try {
                            appUpdateManager!!.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, UPDATE_REQUEST_CODE)
                        } catch (e: SendIntentException) {
                            e.printStackTrace()
                        }
                    }
                }
    }

    companion object {
        private const val UPDATE_REQUEST_CODE = 15
    }
}
