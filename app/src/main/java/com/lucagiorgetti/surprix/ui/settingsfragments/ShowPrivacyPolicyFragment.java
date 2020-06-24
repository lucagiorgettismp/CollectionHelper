package com.lucagiorgetti.surprix.ui.settingsfragments;

import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lucagiorgetti.surprix.R;

public class ShowPrivacyPolicyFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.show_privacy_policy_fragment, container, false);

        TextView policyText = root.findViewById(R.id.policy_text);
        policyText.setMovementMethod(new ScrollingMovementMethod());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            policyText.setText(Html.fromHtml(getString(R.string.privacy_policy_text), Html.FROM_HTML_MODE_LEGACY));
        } else {
            policyText.setText(Html.fromHtml(getString(R.string.privacy_policy_text)));
        }

        return root;
    }
}
