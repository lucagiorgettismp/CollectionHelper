package com.lucagiorgetti.surprix.ui.activities;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.FirebaseCallback;
import com.lucagiorgetti.surprix.model.User;
import com.lucagiorgetti.surprix.utility.DatabaseUtility;
import com.lucagiorgetti.surprix.utility.SystemUtility;
import com.mikelau.countrypickerx.CountryPickerDialog;

public class SettingsActivity extends AppCompatActivity {
    private User currentUser;
    private EditText edtNation;
    private CountryPickerDialog countryPicker = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        currentUser = SurprixApplication.getInstance().getCurrentUser();

        EditText edtEmail = findViewById(R.id.edit_reg_email);
        EditText edtUsername = findViewById(R.id.edit_reg_username);
        ImageView usernameImage = findViewById(R.id.img_reg_username);
        ImageView emailImage = findViewById(R.id.img_reg_email);
        TextView lblPrivacyPolicy = findViewById(R.id.lbl_reg_policy);
        edtNation = findViewById(R.id.edit_reg_nation);
        View layPassword = findViewById(R.id.layout_reg_password);

        TextView lblInfoFacebook = findViewById(R.id.lbl_reg_info_facebook);
        TextView lblInfoFirstLogin = findViewById(R.id.lbl_reg_info_firstlogin);
        TextView lblInfoSettings = findViewById(R.id.lbl_reg_settings_label);

        TextView changePwd = findViewById(R.id.btn_reg_change_pwd);
        TextView deleteUser = findViewById(R.id.btn_reg_delete_account);
        Button submit = findViewById(R.id.btn_reg_submit);
        Button completeBtn = findViewById(R.id.btn_reg_complete_account);
        usernameImage.setColorFilter(ContextCompat.getColor(this, R.color.disabledIcon));
        emailImage.setColorFilter(ContextCompat.getColor(this, R.color.disabledIcon));

        lblPrivacyPolicy.setOnClickListener(v -> showPrivacyPolicy());

        edtUsername.setEnabled(false);
        edtEmail.setEnabled(false);

        layPassword.setVisibility(View.GONE);

        if (currentUser.isFacebook()) {
            changePwd.setVisibility(View.GONE);
        }
        deleteUser.setVisibility(View.VISIBLE);

        edtEmail.setText(currentUser.getEmail().replaceAll(",", "\\."));
        edtUsername.setText(currentUser.getUsername());
        edtNation.setText(currentUser.getCountry());

        lblInfoFirstLogin.setVisibility(View.GONE);
        lblInfoFacebook.setVisibility(View.GONE);
        completeBtn.setVisibility(View.GONE);

        edtNation.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                countryPicker = new CountryPickerDialog(this, (country, flagResId) -> {
                    edtNation.setText(country.getCountryName(getApplicationContext()));
                    countryPicker.dismiss();
                }, false, 0);
                countryPicker.show();
            }
        });


        submit.setText(R.string.save);

        submit.setOnClickListener(v -> {
            SystemUtility.closeKeyboard(this);
            String nation = edtNation.getText().toString();
            DatabaseUtility.updateUser(nation);
            Snackbar.make(v, R.string.user_added, Snackbar.LENGTH_SHORT).show();
        });

        changePwd.setOnClickListener(v -> openChangePwdDialog());

        deleteUser.setOnClickListener(v -> openDeleteUserDialog());
    }


    private void showPrivacyPolicy() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.privacy_policy));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            alertDialog.setMessage(Html.fromHtml(getString(R.string.privacy_policy_text), Html.FROM_HTML_MODE_LEGACY));
        } else {
            alertDialog.setMessage(Html.fromHtml(getString(R.string.privacy_policy_text)));
        }

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void openChangePwdDialog() {
        final View view = getLayoutInflater().inflate(R.layout.dialog_change_password, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle(R.string.change_password);

        final EditText oldPassword = view.findViewById(R.id.edt_dialog_old_pwd);
        final EditText newPassword = view.findViewById(R.id.edt_dialog_new_pwd);
        Button btnChangePwd = view.findViewById(R.id.btn_dialog_submit);

        final AlertDialog changePwd = builder.create();

        changePwd.show();
        btnChangePwd.setOnClickListener(v -> {
            String oldPwd = oldPassword.getText().toString().trim();
            final String newPwd = newPassword.getText().toString().trim();

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            String email = null;
            if (user != null) {
                email = user.getEmail();
            }
            if (email != null) {

                AuthCredential credential = EmailAuthProvider.getCredential(email, oldPwd);

                user.reauthenticate(credential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.updatePassword(newPwd).addOnCompleteListener(task1 -> {
                            if (!task1.isSuccessful()) {
                                Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, R.string.password_changed, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(this, R.string.old_password_wrong, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            changePwd.dismiss();
        });
    }

    public void openDeleteUserDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.delete_account));
        alertDialog.setMessage(getString(R.string.dialod_delete_user_text));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                (dialog, which) -> {
                    DatabaseUtility.deleteUser(new FirebaseCallback<Boolean>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(Boolean item) {

                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                    alertDialog.dismiss();
                    SystemUtility.logout(SettingsActivity.this);
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.dialog_negative),
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }
}


