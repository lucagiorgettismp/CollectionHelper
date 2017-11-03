package com.lucagiorgetti.collectionhelper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.lucagiorgetti.collectionhelper.Db.DbManager;

/**
 * Created by Luca Giorgetti on 27/06/2017.
 */

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private Button facebookLogin;
    private Button registrate;
    private String email;
    private String pwd;
    private FirebaseAuth fireAuth;
    public static DbManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_welcome);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fireAuth = FirebaseAuth.getInstance();

        login = (Button) findViewById(R.id.btn_start_login);
        facebookLogin  = (Button) findViewById(R.id.btn_start_facebook);
        registrate  = (Button) findViewById(R.id.btn_start_registration);

        this.login = (Button) findViewById(R.id.btn_start_login);
        this.registrate = (Button) findViewById(R.id.btn_start_registration);

        this.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogingDialog();
            }
        });

        this.facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Non ancora implementato. :(", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
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

        this.manager = new DbManager(this);
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

    private void showLoginError(View view){
        Snackbar.make(view, "Nome utente o password errati", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    private void showInsertError(View view){
        Snackbar.make(view, "Devi inserire tutti i dati richiesti", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }
}
