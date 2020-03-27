package com.lucagiorgetti.surprix.ui.loginfragments.privacypolicy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.utility.SystemUtils;

public class PrivacyPolicyFragment extends Fragment {
    private String email;
    private boolean fromFacebook;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        email = PrivacyPolicyFragmentArgs.fromBundle(getArguments()).getEmail();
        fromFacebook = PrivacyPolicyFragmentArgs.fromBundle(getArguments()).getFromFacebook();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.privacy_policy_fragment, container, false);

        TextView policyText = root.findViewById(R.id.policy_text);
        policyText.setMovementMethod(new ScrollingMovementMethod());

        Button accept = root.findViewById(R.id.btn_accept);
        TextView refuse = root.findViewById(R.id.btn_refuse);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            policyText.setText(Html.fromHtml(getString(R.string.privacy_policy_text), Html.FROM_HTML_MODE_LEGACY));
        } else {
            policyText.setText(Html.fromHtml(getString(R.string.privacy_policy_text)));
        }

        accept.setOnClickListener(view -> {
            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(SurprixApplication.getSurprixContext()).edit();
            edit.putBoolean(SystemUtils.PRIVACY_POLICY_ACCEPTED, true);
            edit.apply();

            Navigation.findNavController(view).navigate(PrivacyPolicyFragmentDirections.actionNavigationLoginPrivacyToNavigationLoginSignup(email, fromFacebook));
        });

        refuse.setOnClickListener(view -> {
            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(SurprixApplication.getSurprixContext()).edit();
            edit.putBoolean(SystemUtils.PRIVACY_POLICY_ACCEPTED, false);
            edit.apply();

            Navigation.findNavController(view).navigate(PrivacyPolicyFragmentDirections.actionNavigationLoginPrivacyToNavigationLoginHome());
        });

        return root;
    }
}
