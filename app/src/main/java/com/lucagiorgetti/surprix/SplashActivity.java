package com.lucagiorgetti.surprix;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucagiorgetti.surprix.utility.SystemUtility;
import com.lucagiorgetti.surprix.views.LoginActivity;
import com.lucagiorgetti.surprix.views.MainActivity;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth fireAuth;
    private FirebaseAuth.AuthStateListener fireAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fireAuth = SurprixApplication.getInstance().getFirebaseAuth();

        setContentView(R.layout.activity_splash);

        fireAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                SystemUtility.openNewActivityWithFinishing(this, LoginActivity.class, null);
            } else {
                SystemUtility.openNewActivityWithFinishing(this, Main2Activity.class, null);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        fireAuth.addAuthStateListener(fireAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fireAuthStateListener != null) {
            fireAuth.removeAuthStateListener(fireAuthStateListener);
        }
    }

}
