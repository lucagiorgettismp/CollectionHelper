package com.lucagiorgetti.surprix.ui.settingsfragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.model.User;
import com.lucagiorgetti.surprix.ui.activities.LoginActivity;
import com.lucagiorgetti.surprix.utility.DatabaseUtils;
import com.lucagiorgetti.surprix.utility.SystemUtils;
import com.mikelau.countrypickerx.CountryPickerDialog;

public class SettingsFragment extends PreferenceFragmentCompat {

    private Activity activity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    private CountryPickerDialog countryPicker = null;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        Preference privacyPolicy = findPreference(getResources().getString(R.string.settings_privacy_key));
        Preference contactUs = findPreference(getResources().getString(R.string.settings_contact_us_key));
        Preference deleteAccount = findPreference(getResources().getString(R.string.settings_delete_user_key));
        Preference changePassword = findPreference(getResources().getString(R.string.settings_change_password_key));
        Preference countryEdit = findPreference(getResources().getString(R.string.settings_change_country_key));
        Preference logout = findPreference(getResources().getString(R.string.settings_logout_key));
        SwitchPreferenceCompat nightMode = findPreference(getResources().getString(R.string.settings_night_mode_key));

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(getResources().getString(R.string.mailto_surprix)));

        if (logout != null) {
            logout.setOnPreferenceClickListener(p -> {
                SystemUtils.logout();
                SystemUtils.openNewActivityWithFinishing(activity, LoginActivity.class);
                return true;
            });
        }

        if (contactUs != null) {
            contactUs.setIntent(intent);
        }

        if (nightMode != null) {
            nightMode.setChecked(SystemUtils.getDarkThemePreference());
            nightMode.setOnPreferenceChangeListener((p, state) -> {
                boolean checked = (Boolean) state;
                SystemUtils.setDarkThemePreference(checked);
                AppCompatDelegate.setDefaultNightMode(checked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                return true;
            });
        }

        if (privacyPolicy != null) {
            privacyPolicy.setOnPreferenceClickListener(v -> {
                Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(SettingsFragmentDirections.actionSettingsFragmentToPrivacyPolicyFragment());
                return true;
            });
        }

        if (countryEdit != null) {
            User user = SurprixApplication.getInstance().getCurrentUser();
            countryEdit.setSummary(user.getCountry());

            countryEdit.setOnPreferenceClickListener(preference -> {

                countryPicker = new CountryPickerDialog(getContext(), (country, flagResId) -> {
                    String countryName = country.getCountryName(SurprixApplication.getSurprixContext());
                    countryEdit.setSummary(countryName);
                    countryPicker.dismiss();
                    DatabaseUtils.updateUser(countryName);
                }, false, 0);
                countryPicker.show();
                return false;
            });
        }

        if (deleteAccount != null) {
            deleteAccount.setOnPreferenceClickListener(preference -> {

                final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                alertDialog.setTitle(getString(R.string.delete_account));
                alertDialog.setMessage(getString(R.string.dialod_delete_user_text));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                        (dialog, which) -> DatabaseUtils.deleteUser(new CallbackInterface<Boolean>() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(Boolean item) {
                                alertDialog.dismiss();
                                SystemUtils.logout();
                                Toast.makeText(getContext(), R.string.username_delete_success, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure() {

                            }
                        }));
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.dialog_negative),
                        (dialog, which) -> alertDialog.dismiss());
                alertDialog.show();

                return false;
            });
        }

        if (changePassword != null) {
            changePassword.setOnPreferenceClickListener(preference -> {
                final View view = getLayoutInflater().inflate(R.layout.dialog_change_password, null);

                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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

                    String email;
                    if (user != null) {
                        email = user.getEmail();
                        if (email != null) {
                            if (!oldPwd.equals("") && !newPwd.equals("")) {
                                AuthCredential credential = EmailAuthProvider.getCredential(email, oldPwd);
                                user.reauthenticate(credential).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(newPwd).addOnCompleteListener(task1 -> {
                                            if (!task1.isSuccessful()) {
                                                Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), R.string.password_changed, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        changePwd.dismiss();

                                    } else {
                                        Toast.makeText(getContext(), R.string.old_password_wrong, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), R.string.password_cannot_be_null, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                return false;
            });
        }

    }
}