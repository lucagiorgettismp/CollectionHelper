package com.lucagiorgetti.surprix.ui.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.listenerInterfaces.LoginFlowCallbackInterface;
import com.lucagiorgetti.surprix.utility.SystemUtils;

import timber.log.Timber;

public class SplashActivity extends AppCompatActivity {
    private static final int UPDATE_REQUEST_CODE = 15;
    private FirebaseAuth fireAuth;
    private FirebaseAuth.AuthStateListener fireAuthStateListener;
    private AppUpdateManager appUpdateManager;
    private CallbackInterface<Boolean> updateCheckListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        updateCheckListener = new CallbackInterface<Boolean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Boolean item) {
                checkSession();
            }

            @Override
            public void onFailure() {

            }
        };

        fireAuth = FirebaseAuth.getInstance();
        //checkForUpdates();
        checkSession();
    }

    private void checkForUpdates() {
        Timber.d("checkForUpdate: checking for updates");
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                Timber.d("checkForUpdate: update available");

                if (appUpdateInfo.updatePriority() >= 3
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    // Request the update.
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, UPDATE_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                } else {
                    Timber.d("checkForUpdate: unmanaged priority: %s, updateTypeSupported: %b", appUpdateInfo.updatePriority(), appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE));
                    updateCheckListener.onSuccess(true);
                }
            } else {
                Timber.d("checkForUpdate: no updates found");
                updateCheckListener.onSuccess(true);
            }
        });
    }

    private void checkSession() {
        fireAuthStateListener = firebaseAuth -> {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                SystemUtils.setSessionUser(firebaseUser.getUid(), new LoginFlowCallbackInterface() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess() {
                        SystemUtils.openNewActivityWithFinishing(SplashActivity.this, MainActivity.class);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        SystemUtils.openNewActivityWithFinishing(SplashActivity.this, LoginActivity.class);
                    }
                });
            } else {
                SystemUtils.openNewActivityWithFinishing(this, LoginActivity.class);
            }
        };

        fireAuth.addAuthStateListener(fireAuthStateListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fireAuthStateListener != null) {
            fireAuth.removeAuthStateListener(fireAuthStateListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                checkForUpdates();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(appUpdateInfo -> {
                    if (appUpdateInfo.updateAvailability()
                            == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        // If an in-app update is already running, resume the update.
                        try {
                            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, UPDATE_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
