package com.lucagiorgetti.surprix.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseCallback;
import com.lucagiorgetti.surprix.utility.SystemUtils;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth fireAuth;
    private FirebaseAuth.AuthStateListener fireAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        fireAuth = FirebaseAuth.getInstance();
        fireAuthStateListener = firebaseAuth -> {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null){
                SystemUtils.setSessionUser(firebaseUser.getEmail(), new FirebaseCallback<Boolean>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFailure() {

                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        SystemUtils.openNewActivityWithFinishing(SplashActivity.this, MainActivity.class, null);
                    }
                });
            } else {
                SystemUtils.openNewActivityWithFinishing(this, LoginActivity.class, null);
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
        if (fireAuthStateListener != null){
            fireAuth.removeAuthStateListener(fireAuthStateListener);
        }
    }
}
