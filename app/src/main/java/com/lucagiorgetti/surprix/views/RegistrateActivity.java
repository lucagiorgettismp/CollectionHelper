package com.lucagiorgetti.surprix.views;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.listenerInterfaces.OnGetDataListener;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.SystemUtility;
import com.mikelau.countrypickerx.Country;
import com.mikelau.countrypickerx.CountryPickerCallbacks;
import com.mikelau.countrypickerx.CountryPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Allows user to get registered.
 *
 * Created by Luca on 18/10/2017.
 */

public class RegistrateActivity extends AppCompatActivity{

    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtUsername;
    private EditText edtName;
    private EditText edtSurname;
    private EditText edtBirthdate;
    private ProgressBar progress;
    private EditText edtNation;
    private FirebaseAuth fireAuth;
    private LoginManager facebookLogin;
    CountryPickerDialog countryPicker = null;

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.facebookLogin = LoginManager.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fireAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_registration);

        edtEmail = (EditText) findViewById(R.id.edit_reg_email);
        edtPassword = (EditText) findViewById(R.id.edit_reg_password);

        View layPassword = findViewById(R.id.layout_reg_password);
        edtUsername = (EditText) findViewById(R.id.edit_reg_username);
        edtName = (EditText) findViewById(R.id.edit_reg_name);
        edtSurname = (EditText) findViewById(R.id.edit_reg_surname);
        edtBirthdate =(EditText) findViewById(R.id.edit_reg_birthdate);
        edtNation =(EditText) findViewById(R.id.edit_reg_nation);
        progress = (ProgressBar) findViewById(R.id.progress_bar);
        TextView lblInfoFacebook = (TextView) findViewById(R.id.lbl_reg_info_facebook);
        TextView lblInfoFirstLogin = (TextView) findViewById(R.id.lbl_reg_info_firstlogin);
        Button btnAccountCompleteFacebook = (Button) findViewById(R.id.btn_reg_complete_account);
        Button submit = (Button) findViewById(R.id.btn_reg_submit);

        Bundle b = getIntent().getExtras();
        int facebook = 0;
        String facebook_name = null;
        String facebook_surname = null;
        String facebook_email = null;


        if(b != null){
            facebook = b.getInt("facebook");
            facebook_name = b.getString("name");
            facebook_surname = b.getString("surname");
            facebook_email = b.getString("email");
        }

        if (facebook == 1){
            /*aperto da facebook*/
            submit.setVisibility(View.GONE);
            lblInfoFirstLogin.setVisibility(View.GONE);
            layPassword.setVisibility(View.GONE);

            btnAccountCompleteFacebook.setVisibility(View.VISIBLE);
            lblInfoFacebook.setVisibility(View.VISIBLE);
            edtName.setText(facebook_name);
            edtSurname.setText(facebook_surname);
            edtEmail.setText(facebook_email);
            edtEmail.setEnabled(false);
        }

        btnAccountCompleteFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = edtEmail.getText().toString().trim();
                final String username = edtUsername.getText().toString().trim();
                final String name = edtName.getText().toString().trim();
                final String surname = edtSurname.getText().toString().trim();
                final String birthDate = edtBirthdate.getText().toString();
                final String nation = edtNation.getText().toString();
                DatabaseUtility.generateUser(name, surname, email, username, birthDate, nation, true);

                SystemUtility.firstTimeOpeningApp(RegistrateActivity.this, getApplicationContext(), MainActivity.class, null);

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemUtility.closeKeyboard(RegistrateActivity.this, getCurrentFocus());
                progress.setVisibility(View.VISIBLE);

                final String email = edtEmail.getText().toString().trim();
                final String password = edtPassword.getText().toString().trim();
                final String username = edtUsername.getText().toString().trim().toLowerCase();
                final String name = edtName.getText().toString().trim();
                final String surname = edtSurname.getText().toString().trim();
                final String nation = edtNation.getText().toString();
                final String birthDate = edtBirthdate.getText().toString();

                if (!SystemUtility.checkNetworkAvailability(RegistrateActivity.this)) {
                    progress.setVisibility(View.INVISIBLE);
                    return;
                }

                if (email.isEmpty() || password.isEmpty() || username.isEmpty() ||
                        name.isEmpty() || surname.isEmpty() || nation.isEmpty() ||
                        birthDate.isEmpty()){
                    progress.setVisibility(View.INVISIBLE);
                    Toast.makeText(RegistrateActivity.this, R.string.complete_all_fields, Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6){
                    progress.setVisibility(View.INVISIBLE);
                    Toast.makeText(RegistrateActivity.this, R.string.password_lenght, Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseUtility.checkUsernameExist(username, new OnGetDataListener() {
                        @Override
                        public void onSuccess(DataSnapshot data) {
                            fireAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(RegistrateActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                //noinspection ThrowableResultOfMethodCallIgnored
                                                progress.setVisibility(View.INVISIBLE);
                                                Toast.makeText(RegistrateActivity.this, getString(R.string.auth_failed) + task.getException(),
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                DatabaseUtility.generateUser(name, surname, email, username, birthDate, nation, false);
                                                fireAuth.signInWithEmailAndPassword(email, password);
                                                progress.setVisibility(View.INVISIBLE);

                                                SystemUtility.firstTimeOpeningApp(RegistrateActivity.this, getApplicationContext(), MainActivity.class, null);
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onFailure() {
                            progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(RegistrateActivity.this, R.string.username_existing, Toast.LENGTH_SHORT).show();
                        }
                    });
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

        edtBirthdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    new DatePickerDialog(RegistrateActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        edtNation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                countryPicker = new CountryPickerDialog(RegistrateActivity.this, new CountryPickerCallbacks() {
                    @Override
                    public void onCountrySelected(Country country, int flagResId) {
                        edtNation.setText(country.getCountryName(getApplicationContext()));
                        countryPicker.dismiss();
                /* Get Country Name: country.getCountryName(context); */
                /* Call countryPicker.dismiss(); to prevent memory leaks */
                    }

          /* Set to false if you want to disable Dial Code in the results and true if you want to show it
             Set to zero if you don't have a custom JSON list of countries in your raw file otherwise use
             resourceId for your customly available countries */
                }, false, 0);
                countryPicker.show();
            }
            }
        });
    }

    private void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.dateFormat), Locale.ITALIAN);
        edtBirthdate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        facebookLogin.logOut();
        fireAuth.signOut();

        SystemUtility.openNewActivityWithFinishing(RegistrateActivity.this, getApplicationContext(), MainActivity.class, null);
    }
}
