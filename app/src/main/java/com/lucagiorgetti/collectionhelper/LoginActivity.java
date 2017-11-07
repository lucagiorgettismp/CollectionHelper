package com.lucagiorgetti.collectionhelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Luca Giorgetti on 27/06/2017.
 */

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private Button registrate;
    private Button facebookLogin;
    private ProgressBar progressBar;
    private String email;
    private String pwd;
    private FirebaseAuth fireAuth;
    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        progressBar = (ProgressBar) findViewById(R.id.login_loading);
        progressBar.setVisibility(View.GONE);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        facebookLogin = (Button) findViewById(R.id.btn_start_facebook);

        final LoginButton loginButton = (LoginButton) findViewById(R.id.btn_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FACEBOOK", "facebook:onSuccess:" + loginResult);
                signInWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("FACEBOOK", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FACEBOOK", "facebook:onError", error);
            }
        });
        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fireAuth = FirebaseAuth.getInstance();

        login = (Button) findViewById(R.id.btn_start_login);
        registrate  = (Button) findViewById(R.id.btn_start_registration);

        this.login = (Button) findViewById(R.id.btn_start_login);
        this.registrate = (Button) findViewById(R.id.btn_start_registration);

        this.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogingDialog();
            }
        });

        this.registrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegistrateActivity.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Staring Login Activity
                getApplicationContext().startActivity(i);
            }
        });
    }

    private void openLogingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Login");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(40, 0, 40, 0);

        final EditText inEmail = new EditText(this);
        inEmail.setHint("Email");
        inEmail.setLayoutParams(params);
        layout.addView(inEmail);

        final EditText inPassword = new EditText(this);
        inPassword.setHint("Password");
        inPassword.setLayoutParams(params);
        layout.addView(inPassword);

        inEmail.setInputType(InputType.TYPE_CLASS_TEXT);
        inEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        inPassword.setInputType(InputType.TYPE_CLASS_TEXT);
        inPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        builder.setView(layout);

        builder.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                email = inEmail.getText().toString().trim();
                Log.w("LOGIN", "input email : " + email);
                pwd = inPassword.getText().toString().trim();
                Log.w("LOGIN", "input pwd : " + pwd);
                fireAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.w("LOGIN", "Fatto. Stato" + task.isSuccessful());
                        if (!task.isSuccessful())
                        {
                            Log.w("LOGIN", "Errore:(");
                            Toast.makeText(LoginActivity.this, "Errore :(",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            // Closing all the Activities
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            // Add new Flag to start new Activity
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            // Staring Login Activity
                            getApplicationContext().startActivity(i);
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void signInWithFacebook(AccessToken token) {
        progressBar.setVisibility(View.VISIBLE);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        fireAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            String name=task.getResult().getUser().getDisplayName();
                            String email=task.getResult().getUser().getEmail();
                            checkUserExisting(email, name);
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

     private void checkUserExisting(final String email, String nameSurname) {
         FirebaseDatabase database = FirebaseDatabase.getInstance();
         DatabaseReference emails = database.getReference("emails"); //users is a node in your Firebase Database.
         final String emailCod = email.replaceAll("\\.", ",");

         String fullName[] = nameSurname.split(" ", 2);
         final String facebook_name = fullName[0];
         final String facebook_surname = fullName[1];

         emails.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot snapshot) {
                 if (snapshot.hasChild(emailCod)) {
                     // utente già registrato

                     Intent i = new Intent(getApplicationContext(), MainActivity.class);
                     i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     getApplicationContext().startActivity(i);

                 } else {
                     Intent i = new Intent(getApplicationContext(), RegistrateActivity.class);

                     Bundle b = new Bundle();
                     b.putInt("facebook", 1);
                     b.putString("name", facebook_name);
                     b.putString("surname", facebook_surname);
                     b.putString("email", email);
                     i.putExtras(b);

                     // Closing all the Activities
                     i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                     // Add new Flag to start new Activity
                     i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                     // Staring Login Activity
                     getApplicationContext().startActivity(i);
                 }
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });
    }

    // Override the onActivityResult method and pass its parameters to the callbackManager//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
