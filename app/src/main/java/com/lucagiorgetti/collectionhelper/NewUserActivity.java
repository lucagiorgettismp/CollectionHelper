package com.lucagiorgetti.collectionhelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.lucagiorgetti.collectionhelper.Db.DbManager;
import com.lucagiorgetti.collectionhelper.MainActivity;
import com.lucagiorgetti.collectionhelper.R;

/**
 * Created by Luca Giorgetti on 27/06/2017.
 */

public class NewUserActivity extends AppCompatActivity {
    public static EditText editTextName;
    public static EditText editTextSurname;
    public static EditText editTextEmail;
    public static EditText editTextUsername;
    public static EditText editTextPassword;
    public static EditText editTextBirthDate;
    public static EditText editTextNationality;
    public static Button submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        this.editTextName = (EditText) findViewById(R.id.edt_reg_name);
        this.editTextSurname = (EditText) findViewById(R.id.edt_reg_surname);
        this.editTextEmail = (EditText) findViewById(R.id.edt_reg_email);
        this.editTextUsername = (EditText) findViewById(R.id.edt_username);
        this.editTextPassword = (EditText) findViewById(R.id.edt_pwd);
        this.editTextBirthDate = (EditText) findViewById(R.id.edt_reg_birthdate);
        this.editTextNationality = (EditText) findViewById(R.id.edt_reg_nationality);

        this.submit = (Button) findViewById(R.id.btn_reg_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                if(checkSubmit(editTextName.getText(), editTextSurname.getText(), editTextEmail.getText(), editTextUsername.getText(), editTextPassword.getText(),
                        editTextBirthDate.getText(), editTextNationality.getText())){
                    setUserLogged(editTextUsername.getText().toString());
                    finish();
                }
                else {
                    showLoginError(view);
                }
            }
        });
    }

    private boolean checkSubmit(Editable name, Editable surname, Editable email, Editable username, Editable password, Editable birthdate, Editable nationality) {
        boolean chk_username = !username.equals("") && checkUsernameNotExisting(username.toString());
        boolean chk_pwd = !password.equals("");
        boolean chk_email = !email.equals("") && isValidEmail(email);
        boolean chk_name = !name.equals("");
        boolean chk_surname = !surname.equals("");
        boolean chk_birthdate = !birthdate.equals("");
        boolean chk_nationality = !nationality.equals("");

        return chk_name && chk_surname && chk_email && chk_username && chk_pwd && chk_birthdate && chk_nationality;
    }



    private void showLoginError(View view){
        Snackbar.make(view, "E' stato riscontrato un errore. Compilare correttamente tutti i campi", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    private boolean checkUsernameNotExisting(String username){
        DbManager mng = new DbManager(this);
        return !mng.getExistingUsername(username);
    }

    public void setUserLogged(String value){
        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.LOGGED, MODE_PRIVATE).edit();
        editor.putString("username", value);
        editor.apply();
    }

    //https://stackoverflow.com/questions/1819142/how-should-i-validate-an-e-mail-address
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onBackPressed() {

    }
}
