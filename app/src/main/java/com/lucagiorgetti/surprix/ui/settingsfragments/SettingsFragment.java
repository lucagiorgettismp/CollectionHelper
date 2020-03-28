package com.lucagiorgetti.surprix.ui.settingsfragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.utility.SystemUtils;

public class SettingsFragment extends PreferenceFragmentCompat {
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

        if (nightMode != null) {
            nightMode.setChecked(SystemUtils.getDarkThemePreference());
            nightMode.setOnPreferenceChangeListener((p, state) -> {
                boolean checked = (Boolean) state;
                SystemUtils.setDarkThemePreference(checked);
                AppCompatDelegate.setDefaultNightMode(checked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                return true;
            });
        }

        if (privacyPolicy != null){
            privacyPolicy.setOnPreferenceClickListener(v -> {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(SettingsFragmentDirections.actionSettingsFragmentToPrivacyPolicyFragment());
                return true;
            });
        }

    }
}