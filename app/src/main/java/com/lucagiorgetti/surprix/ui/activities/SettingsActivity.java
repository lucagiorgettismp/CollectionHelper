package com.lucagiorgetti.surprix.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.utility.SystemUtils;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        setTitle(getResources().getString(R.string.settings));
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference privacyPolicy = findPreference(getResources().getString(R.string.settings_privacy_key));
            Preference contactUs = findPreference(getResources().getString(R.string.settings_contact_us_key));
            SwitchPreferenceCompat nightMode = findPreference(getResources().getString(R.string.settings_night_mode_key));

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(getResources().getString(R.string.mailto_surprix)));

            if (contactUs != null) {
                contactUs.setIntent(intent);
            }

            if (privacyPolicy != null) {
                privacyPolicy.setOnPreferenceClickListener(p -> {
                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle(getString(R.string.privacy_policy));

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        alertDialog.setMessage(Html.fromHtml(getString(R.string.privacy_policy_text), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        alertDialog.setMessage(Html.fromHtml(getString(R.string.privacy_policy_text)));
                    }

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive),
                            (dialog, which) -> alertDialog.dismiss());
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();

                    return true;
                });
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
        }
    }
}