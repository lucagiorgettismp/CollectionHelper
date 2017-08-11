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

/**
 * Created by Luca Giorgetti on 27/06/2017.
 */

public class LoginActivity extends AppCompatActivity {
    public static EditText editTextUsername;
    public static EditText editTextPassword;
    public static Button login;
    public static Button new_user;

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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                if(checkLogin(editTextUsername.getText(), editTextPassword.getText())){
                    setUserLogged(editTextUsername.getText().toString());
                    finish();
                }
                else {
                    showLoginError(view);
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

    private boolean checkLogin(Editable username, Editable password) {
        boolean chk_username = username.toString().equals("asd");
        boolean chk_pwd = password.toString().equals("asd");
        return chk_username && chk_pwd;
    }

    private void showLoginError(View view){
        Snackbar.make(view, "Nome utente o password errati", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();;
    }

    public void setUserLogged(String value){
        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.LOGGED, MODE_PRIVATE).edit();
        editor.putString("username", value);
        editor.apply();
    }
}
