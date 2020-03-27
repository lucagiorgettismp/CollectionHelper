package com.lucagiorgetti.surprix.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseCallback;
import com.lucagiorgetti.surprix.utility.DatabaseUtils;
import com.lucagiorgetti.surprix.utility.SystemUtils;

import java.util.Collections;

import timber.log.Timber;

/**
 * Activity for loggin user into App.
 * <p>
 * Created by Luca Giorgetti on 27/06/2017.
 */

public class LoginActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private FirebaseAuth fireAuth;
    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = findViewById(R.id.login_loading);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.INVISIBLE);

        AppEventsLogger.activateApp(getApplication());
        callbackManager = CallbackManager.Factory.create();

        Button facebookCustomLogin = findViewById(R.id.btn_start_facebook);

        Button login = findViewById(R.id.btn_start_login);
        Button registerBtn = findViewById(R.id.btn_start_registration);

        facebookCustomLogin.setOnClickListener(view -> {
            if (SystemUtils.checkNetworkAvailability(LoginActivity.this)) {
                progressBar.setVisibility(View.VISIBLE);
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Collections.singletonList("email"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Timber.d("facebook:onSuccess: %s", loginResult);
                        signInWithFacebookToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(), "Facebook login cancelled", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(getApplicationContext(), "Facebook login error", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        login.setOnClickListener(v -> openLoginDialog());

        registerBtn.setOnClickListener(v -> SystemUtils.openNewActivityWithFinishing(LoginActivity.this, SignUpActivty.class, null));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fireAuth = FirebaseAuth.getInstance();
    }

    @SuppressLint("InflateParams")
    private void openLoginDialog() {
        final View view = getLayoutInflater().inflate(R.layout.dialog_login_user, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("Login");

        final EditText inEmail = view.findViewById(R.id.login_dialog_email);
        final EditText inPassword = view.findViewById(R.id.login_dialog_pwd);
        Button loginBtn = view.findViewById(R.id.login_dialog_submit);
        TextView forgetPwd = view.findViewById(R.id.lbl_login_forgotten_pwd);

        final AlertDialog loginDialog = builder.create();
        forgetPwd.setOnClickListener(v -> {
            openResetPwdDialog();
            loginDialog.dismiss();
        });

        loginDialog.show();
        loginBtn.setOnClickListener(v -> {
            if (!SystemUtils.checkNetworkAvailability(LoginActivity.this)) {
                Toast.makeText(getApplicationContext(), R.string.network_unavailable, Toast.LENGTH_SHORT).show();
                return;
            }
            String email = inEmail.getText().toString().trim();
            Timber.w("Input email : %s", email);
            String pwd = inPassword.getText().toString().trim();
            Timber.w("Input pwd : %s", pwd);

            if (!email.equals("") && !pwd.equals("")) {
                fireAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, R.string.wrong_email_or_password,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        SystemUtils.setSessionUser(email, new FirebaseCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean success) {
                                goToMainActivity();
                                finish();
                            }

                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onFailure() {

                            }
                        });

                    }
                });
            } else {
                Toast.makeText(LoginActivity.this, R.string.wrong_email_or_password,
                        Toast.LENGTH_SHORT).show();
            }
            loginDialog.dismiss();
        });
    }

    @SuppressLint("InflateParams")
    private void openResetPwdDialog() {
        final View view = getLayoutInflater().inflate(R.layout.dialog_password_reset, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle(R.string.forgot_yout_password);

        final EditText inEmail = view.findViewById(R.id.login_reset_pwd_email);
        Button resetBtn = view.findViewById(R.id.login_reset_pwd_submit);

        final AlertDialog resetDialog = builder.create();
        resetDialog.show();

        resetBtn.setOnClickListener(v -> {
            sendPasswordResetEmail(inEmail.getText().toString().trim());
            resetDialog.dismiss();
        });
    }

    private void sendPasswordResetEmail(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, R.string.mail_successfully_sent, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithFacebookToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        fireAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        final String email = task.getResult().getUser().getEmail();
                        if (email != null) {
                            DatabaseUtils.checkUserExisting(email, new FirebaseCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean result) {
                                    if (result) {
                                        SystemUtils.setSessionUser(email, new FirebaseCallback<Boolean>() {
                                            @Override
                                            public void onSuccess(Boolean success) {
                                                goToMainActivity();
                                            }

                                            @Override
                                            public void onStart() {

                                            }

                                            @Override
                                            public void onFailure() {

                                            }
                                        });
                                    } else {
                                        Bundle b = new Bundle();
                                        b.putBoolean("facebook", true);
                                        b.putString("email", email);
                                        goToSignUpActivity(b);
                                    }
                                }

                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onFailure() {

                                }
                            });
                        }
                    }

                    progressBar.setVisibility(View.INVISIBLE);
                });
    }

    private void goToSignUpActivity(Bundle bundle) {
        SystemUtils.openNewActivityWithFinishing(LoginActivity.this, SignUpActivty.class, bundle);
    }

    private void goToMainActivity() {
        SystemUtils.openNewActivityWithFinishing(LoginActivity.this, MainActivity.class, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
