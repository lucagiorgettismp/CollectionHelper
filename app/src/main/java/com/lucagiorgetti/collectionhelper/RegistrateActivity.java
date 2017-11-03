package com.lucagiorgetti.collectionhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lucagiorgetti.collectionhelper.model.User;

/**
 * Created by Luca on 18/10/2017.
 */

public class RegistrateActivity extends AppCompatActivity{

    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtUsername;
    private Button submit;
    private FirebaseAuth fireAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fireAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.registration);

        edtEmail = (EditText) findViewById(R.id.edit_reg_email);
        edtPassword = (EditText) findViewById(R.id.edit_reg_password);
        edtUsername = (EditText) findViewById(R.id.edit_reg_username);
        submit = (Button) findViewById(R.id.btn_reg_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = edtEmail.getText().toString().trim();
                final String password = edtPassword.getText().toString().trim();
                final String username = edtUsername.getText().toString().trim();

                fireAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegistrateActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                generateUser(email, password, username);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegistrateActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(RegistrateActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });

    }

    private void generateUser(String email, String pwd, String username){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference users = database.getReference("users"); //users is a node in your Firebase Database.
        User user = new User(email, username, pwd); //ObjectClass for Users

        users.child(username).setValue(user);
        // users.push().setValue(user);
    }
}
