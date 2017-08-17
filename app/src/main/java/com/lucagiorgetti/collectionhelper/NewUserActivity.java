package com.lucagiorgetti.collectionhelper;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;

import com.lucagiorgetti.collectionhelper.Db.DbManager;
import com.lucagiorgetti.collectionhelper.MainActivity;
import com.lucagiorgetti.collectionhelper.R;
import com.lucagiorgetti.collectionhelper.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
    public static DbManager manager;

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        this.editTextName = (EditText) findViewById(R.id.edt_reg_name);
        this.editTextSurname = (EditText) findViewById(R.id.edt_reg_surname);
        this.editTextEmail = (EditText) findViewById(R.id.edt_reg_email);
        this.editTextUsername = (EditText) findViewById(R.id.edt_reg_username);
        this.editTextPassword = (EditText) findViewById(R.id.edt_reg_pwd);
        this.editTextBirthDate = (EditText) findViewById(R.id.edt_reg_birthdate);
        this.editTextNationality = (EditText) findViewById(R.id.edt_reg_nationality);

        this.submit = (Button) findViewById(R.id.btn_reg_submit);
        this.manager = new DbManager(this);
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
                    int userId = manager.getNewUserId();
                    manager.addUser(new User(
                            editTextName.getText().toString(),editTextSurname.getText().toString(),
                            editTextEmail.getText().toString(),editTextUsername.getText().toString(),
                            editTextPassword.getText().toString(), editTextBirthDate.getText().toString(),
                            editTextNationality.getText().toString(), userId));
                    setUserLogged(userId);
                    finish();
                }
                else {
                    showLoginError(view);
                }
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        editTextBirthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    new DatePickerDialog(NewUserActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTextBirthDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void showLoginError(View view){
        Snackbar.make(view, "E' stato riscontrato un errore. Compilare correttamente tutti i campi", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    private boolean checkUsernameNotExisting(String username){
        DbManager mng = new DbManager(this);
        return !mng.getExistingUsername(username);
    }

    public void setUserLogged(int userId){
        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.LOGGED, MODE_PRIVATE).edit();
        editor.putInt("user", userId);
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
