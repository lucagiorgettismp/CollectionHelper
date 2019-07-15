package com.lucagiorgetti.surprix.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.OnGetResultListener;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.SystemUtility;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

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
        //final LoginButton facebookLoginButton = findViewById(R.id.btn_facebook_login);
        Button login = findViewById(R.id.btn_start_login);
        Button registrate = findViewById(R.id.btn_start_registration);

        facebookCustomLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemUtility.checkNetworkAvailability(LoginActivity.this)) {
                    progressBar.setVisibility(View.VISIBLE);
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Collections.singletonList("email"));
                    LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Log.d("FACEBOOK", "facebook:onSuccess:" + loginResult);
                            signInWithFacebookToken(loginResult.getAccessToken());
                        }

                        @Override
                        public void onCancel() {
                            Snackbar.make(Objects.requireNonNull(getCurrentFocus()), "Facebook login cancelled", Snackbar.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Snackbar.make(Objects.requireNonNull(getCurrentFocus()), "Facebook login error", Snackbar.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginDialog();
            }
        });

        registrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemUtility.openNewActivityWithFinishing(LoginActivity.this, RegistrateActivity.class, null);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fireAuth = SurprixApplication.getInstance().getFirebaseAuth();
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
        forgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResetPwdDialog();
                loginDialog.dismiss();
            }
        });

        loginDialog.show();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SystemUtility.checkNetworkAvailability(LoginActivity.this)) {
                    Snackbar.make(view, R.string.network_unavailable, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                String email = inEmail.getText().toString().trim();
                Log.w("LOGIN", "input email : " + email);
                String pwd = inPassword.getText().toString().trim();
                Log.w("LOGIN", "input pwd : " + pwd);

                if (!email.equals("") && !pwd.equals("")) {
                    fireAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, R.string.wrong_email_or_password,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                goToMainActivity();
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, R.string.wrong_email_or_password,
                            Toast.LENGTH_SHORT).show();
                }
                loginDialog.dismiss();
            }
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

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetEmail(inEmail.getText().toString().trim());
                resetDialog.dismiss();
            }
        });
    }

    private void sendPasswordResetEmail(String email) {
        SurprixApplication.getInstance().getFirebaseAuth().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, R.string.mail_successfully_sent, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signInWithFacebookToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        fireAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            final String email = task.getResult().getUser().getEmail();
                            if (email != null) {
                                DatabaseUtility.checkUserExisting(email, new OnGetResultListener() {
                                    @Override
                                    public void onSuccess(boolean result) {
                                        if (result) {
                                            goToMainActivity();
                                        } else {
                                            Bundle b = new Bundle();
                                            b.putBoolean("facebook", true);
                                            b.putString("email", email);
                                            goToRegistrateActivity(b);
                                        }
                                    }

                                    @Override
                                    public void onFailure() {

                                    }
                                });
                            }
                        }

                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void goToRegistrateActivity(Bundle bundle) {
        SystemUtility.openNewActivityWithFinishing(LoginActivity.this, RegistrateActivity.class, bundle);
    }

    private void goToMainActivity() {
        SystemUtility.openNewActivityWithFinishing(LoginActivity.this, MainActivity.class, null);
    }


    // Override the onActivityResult method and pass its parameters to the callbackManager//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
