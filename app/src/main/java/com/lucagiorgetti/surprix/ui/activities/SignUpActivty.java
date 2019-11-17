package com.lucagiorgetti.surprix.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.login.LoginManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseCallback;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.SystemUtility;
import com.mikelau.countrypickerx.CountryPickerDialog;

import java.util.Objects;

/**
 * Allows user to get registered.
 * <p>
 * Created by Luca on 18/10/2017.
 */

public class SignUpActivty extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtUsername;
    private ProgressBar progress;
    private EditText edtNation;
    private FirebaseAuth fireAuth;
    private LoginManager facebookLogin;
    CountryPickerDialog countryPicker = null;

    // final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.facebookLogin = LoginManager.getInstance();
        this.fireAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setContentView(R.layout.activity_signup);

        edtEmail = findViewById(R.id.edit_reg_email);
        edtPassword = findViewById(R.id.edit_reg_password);
        edtUsername = findViewById(R.id.edit_reg_username);
        edtNation = findViewById(R.id.edit_reg_nation);

        View layPassword = findViewById(R.id.layout_reg_password);
        View laySettings = findViewById(R.id.layout_reg_setting);

        progress = findViewById(R.id.progress_bar);
        TextView lblInfoFacebook = findViewById(R.id.lbl_reg_info_facebook);
        TextView lblInfoFirstLogin = findViewById(R.id.lbl_reg_info_firstlogin);
        TextView lblInfoSettings = findViewById(R.id.lbl_reg_settings_label);
        Button btnAccountCompleteFacebook = findViewById(R.id.btn_reg_complete_account);
        Button submit = findViewById(R.id.btn_reg_submit);
        TextView lblPrivacyPolicy = findViewById(R.id.lbl_reg_policy);
        TextView logout = findViewById(R.id.lbl_reg_logout);

        lblPrivacyPolicy.setOnClickListener(v -> showPrivacyPolicy());

        Bundle b = getIntent().getExtras();
        boolean facebook = false;
        String facebook_email = null;

        if (b != null) {
            facebook = b.getBoolean("facebook");
            facebook_email = b.getString("email");
        }

        if (facebook) {
            /*aperto da facebook*/
            submit.setVisibility(View.GONE);
            lblInfoFirstLogin.setVisibility(View.GONE);
            layPassword.setVisibility(View.GONE);

            edtEmail.setText(facebook_email);
            edtEmail.setEnabled(false);
        } else {
            lblInfoFacebook.setVisibility(View.GONE);
            btnAccountCompleteFacebook.setVisibility(View.GONE);
        }

        laySettings.setVisibility(View.GONE);
        lblInfoSettings.setVisibility(View.GONE);

        btnAccountCompleteFacebook.setOnClickListener(v -> {
            final String email = edtEmail.getText().toString().trim();
            final String username = edtUsername.getText().toString().trim().toLowerCase();
            final String nation = edtNation.getText().toString();
            if (email.isEmpty() ||
                    username.isEmpty() ||
                    nation.isEmpty()) {
                progress.setVisibility(View.INVISIBLE);
                Toast.makeText(SignUpActivty.this, R.string.complete_all_fields, Toast.LENGTH_SHORT).show();
            } else {
                DatabaseUtility.generateUser(email, username, nation, true);
                SystemUtility.firstTimeOpeningApp(SignUpActivty.this, MainActivity.class, null);
            }
        });

        submit.setOnClickListener(v -> {
            SystemUtility.closeKeyboard(SignUpActivty.this);
            progress.setVisibility(View.VISIBLE);

            final String email = edtEmail.getText().toString().trim();
            final String password = edtPassword.getText().toString().trim();
            final String username = edtUsername.getText().toString().trim().toLowerCase();
            final String nation = edtNation.getText().toString();

            if (!SystemUtility.checkNetworkAvailability(SignUpActivty.this)) {
                progress.setVisibility(View.INVISIBLE);
                return;
            }

            if (email.isEmpty() ||
                    password.isEmpty() ||
                    username.isEmpty() ||
                    nation.isEmpty()) {
                progress.setVisibility(View.INVISIBLE);
                Snackbar.make(Objects.requireNonNull(getCurrentFocus()), R.string.complete_all_fields, Snackbar.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                progress.setVisibility(View.INVISIBLE);
                Snackbar.make(Objects.requireNonNull(getCurrentFocus()), R.string.password_lenght, Snackbar.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                progress.setVisibility(View.INVISIBLE);
                Snackbar.make(Objects.requireNonNull(getCurrentFocus()), R.string.email_format, Snackbar.LENGTH_SHORT).show();
            } else {
                DatabaseUtility.checkUsernameDontExists(username, new FirebaseCallback<Boolean>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            fireAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(SignUpActivty.this, task -> {
                                        if (!task.isSuccessful()) {
                                            //noinspection ThrowableResultOfMethodCallIgnored
                                            progress.setVisibility(View.INVISIBLE);
                                            Snackbar.make(Objects.requireNonNull(getCurrentFocus()), getString(R.string.auth_failed),
                                                    Snackbar.LENGTH_SHORT).show();
                                        } else {
                                            DatabaseUtility.generateUser(email, username, nation, false);
                                            fireAuth.signInWithEmailAndPassword(email, password);
                                            progress.setVisibility(View.INVISIBLE);
                                            SystemUtility.firstTimeOpeningApp(SignUpActivty.this, MainActivity.class, null);
                                        }
                                    });
                        } else {
                            progress.setVisibility(View.INVISIBLE);
                            Snackbar.make(Objects.requireNonNull(getCurrentFocus()), R.string.username_existing, Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure() {
                        Snackbar.make(Objects.requireNonNull(getCurrentFocus()), R.string.data_sync_error, Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        showFirstTimePolicy();

        edtNation.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                /* Set to false if you want to disable Dial Code in the results and true if you want to show it
                   Set to zero if you don't have a custom JSON list of countries in your raw file otherwise use
                   resourceId for your customly available countries */
                countryPicker = new CountryPickerDialog(SignUpActivty.this, (country, flagResId) -> {
                    edtNation.setText(country.getCountryName(getApplicationContext()));
                    countryPicker.dismiss();
                }, false, 0);
                countryPicker.show();
            }
        });
    }

    private void showFirstTimePolicy() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean accepted = prefs.getBoolean(SystemUtility.PRIVACY_POLICY_ACCEPTED, false);
        if (!accepted) {
            showPrivacyPolicy();
        }
    }

    private void showPrivacyPolicy() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.privacy_policy));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            alertDialog.setMessage(Html.fromHtml(getString(R.string.privacy_policy_text), Html.FROM_HTML_MODE_LEGACY));
        } else {
            alertDialog.setMessage(Html.fromHtml(getString(R.string.privacy_policy_text)));
        }

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.terms_agree),
                (dialog, which) -> {
                    SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                    edit.putBoolean(SystemUtility.PRIVACY_POLICY_ACCEPTED, true);
                    edit.apply();
                    alertDialog.dismiss();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.back),
                (dialog, which) -> {
                    SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                    edit.putBoolean(SystemUtility.PRIVACY_POLICY_ACCEPTED, false);
                    edit.apply();
                    alertDialog.dismiss();
                    onBackPressed();
                });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        facebookLogin.logOut();
        fireAuth.signOut();

        SystemUtility.openNewActivityWithFinishing(SignUpActivty.this, LoginActivity.class, null);
    }
}
