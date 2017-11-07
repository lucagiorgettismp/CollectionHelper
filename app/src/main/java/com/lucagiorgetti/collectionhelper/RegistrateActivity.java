package com.lucagiorgetti.collectionhelper;

import android.app.DatePickerDialog;
import android.content.Intent;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lucagiorgetti.collectionhelper.model.User;
import com.mikelau.countrypickerx.Country;
import com.mikelau.countrypickerx.CountryPickerCallbacks;
import com.mikelau.countrypickerx.CountryPickerDialog;

/**
 * Created by Luca on 18/10/2017.
 */

public class RegistrateActivity extends AppCompatActivity{

    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtUsername;
    private EditText edtName;
    private EditText edtSurname;
    private EditText edtBirthdate;
    private EditText edtNation;
    private Button submit;
    private FirebaseAuth fireAuth;
    private SimpleDateFormat sdf = null;
    CountryPickerDialog countryPicker = null;

    final Calendar myCalendar = Calendar.getInstance();

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
        edtName = (EditText) findViewById(R.id.edit_reg_name);
        edtSurname = (EditText) findViewById(R.id.edit_reg_surname);
        edtName = (EditText) findViewById(R.id.edit_reg_name);
        edtBirthdate =(EditText) findViewById(R.id.edit_reg_birthdate);
        edtNation =(EditText) findViewById(R.id.edit_reg_nation);
        TextView lblInfoFacebook = (TextView) findViewById(R.id.lbl_reg_info_facebook);
        TextView lblInfoFirstLogin = (TextView) findViewById(R.id.lbl_reg_info_firstlogin);
        Button btnAccountCompleteFacebook = (Button) findViewById(R.id.btn_reg_complete_account);
        submit = (Button) findViewById(R.id.btn_reg_submit);

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
            edtPassword.setVisibility(View.GONE);

            btnAccountCompleteFacebook.setVisibility(View.VISIBLE);
            lblInfoFacebook.setVisibility(View.VISIBLE);
            edtName.setText(facebook_name);
            edtSurname.setText(facebook_surname);
            edtEmail.setText(facebook_email);
        }

        btnAccountCompleteFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = edtEmail.getText().toString().trim();
                final String username = edtUsername.getText().toString().trim();
                final String name = edtName.getText().toString().trim();
                final String surname = edtSurname.getText().toString().trim();
                Date birthDate = null;
                try {
                    birthDate = sdf.parse(edtBirthdate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final String nation = edtNation.getText().toString();
                generateUser(name, surname, email, username, birthDate, nation);
                Intent i = new Intent(RegistrateActivity.this, MainActivity.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Staring Login Activity
                getApplicationContext().startActivity(i);

                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = edtEmail.getText().toString().trim();
                final String password = edtPassword.getText().toString().trim();
                final String username = edtUsername.getText().toString().trim();
                final String name = edtName.getText().toString().trim();
                final String surname = edtSurname.getText().toString().trim();
                Date birthDate = null;
                try {
                    birthDate = sdf.parse(edtBirthdate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final String nation = edtNation.getText().toString();


                final Date finalBirthDate = birthDate;
                fireAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegistrateActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                generateUser(name, surname, email, username, finalBirthDate, nation);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegistrateActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    fireAuth.signInWithEmailAndPassword(email, password);

                                    Intent i = new Intent(RegistrateActivity.this, MainActivity.class);
                                    // Closing all the Activities
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    // Add new Flag to start new Activity
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    // Staring Login Activity
                                    getApplicationContext().startActivity(i);

                                    finish();
                                }
                            }
                        });
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
                edtNation.requestFocus();
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
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        sdf = new SimpleDateFormat(myFormat, Locale.ITALIAN);
        edtBirthdate.setText(sdf.format(myCalendar.getTime()));
    }

    private void generateUser(String name, String surname, String email, String username, Date birthDate, String nation){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String emailCod = email.replaceAll("\\.", ",");

        DatabaseReference users = database.getReference("users"); //users is a node in your Firebase Database.
        User user = new User(name, surname, emailCod, username, sdf.format(birthDate), nation); //ObjectClass for Users

        DatabaseReference emails = database.getReference("emails"); //users is a node in your Firebase Database.
        emails.child(emailCod).child(username).setValue(true);

        users.child(username).setValue(user);
        // users.push().setValue(user);
    }
}
