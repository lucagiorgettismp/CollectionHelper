package com.lucagiorgetti.collectionhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.lucagiorgetti.collectionhelper.Db.DbManager;

/**
 * Created by Luca Giorgetti on 27/06/2017.
 */

public class LoginActivity extends AppCompatActivity {
    public static EditText editTextUsername;
    public static EditText editTextPassword;
    public static Button login;
    public static Button new_user;
    public static DbManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        String username;
        String password;

        this.editTextUsername = (EditText) findViewById(R.id.edt_username);
        this.editTextPassword = (EditText) findViewById(R.id.edt_pwd);
        this.login = (Button) findViewById(R.id.btn_login);
        this.new_user = (Button) findViewById(R.id.btn_newuser);
        this.manager = new DbManager(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                if(editTextUsername.getText().toString() != "" && editTextPassword.getText().toString() != ""){
                    int userId = manager.login(editTextUsername.getText().toString(), editTextPassword.getText().toString());
                    if(userId != -1){
                        setUserLogged(userId);
                        finish();
                    } else {
                        showLoginError(view);
                    }
                } else {
                    showInsertError(view);
                }

            }
        });

        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try  {
                    Log.w("LOGIN","Registarti cliccato");
                    Intent registration = new Intent(LoginActivity.this, NewUserActivity.class);
                    startActivity(registration);
                    finish();

                } catch (Exception e) {

                }
            }
        });
    }

    private void showLoginError(View view){
        Snackbar.make(view, "Nome utente o password errati", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();;
    }

    private void showInsertError(View view){
        Snackbar.make(view, "Devi inserire tutti i dati richiesti", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();;
    }

    public void setUserLogged(int userId){
        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.LOGGED, MODE_PRIVATE).edit();
        editor.putInt("userId", userId);
        editor.apply();
    }
}
