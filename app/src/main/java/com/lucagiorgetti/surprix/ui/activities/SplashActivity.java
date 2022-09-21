package com.lucagiorgetti.surprix.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucagiorgetti.surprix.listenerInterfaces.LoginFlowCallbackInterface;
import com.lucagiorgetti.surprix.utility.SystemUtils;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth fireAuth;
    private FirebaseAuth.AuthStateListener fireAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fireAuth = FirebaseAuth.getInstance();
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        fireAuth.addAuthStateListener(fireAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fireAuthStateListener != null) {
            fireAuth.removeAuthStateListener(fireAuthStateListener);
        }
    }

}
