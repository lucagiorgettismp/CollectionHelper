package com.lucagiorgetti.surprix.ui.loginfragments.signup;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackWithExceptionInterface;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.LoginFlowHelper;
import com.lucagiorgetti.surprix.utility.SystemUtils;
import com.mikelau.countrypickerx.CountryPickerDialog;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SignUpFragment extends BaseFragment {
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtUsername;
    private EditText edtNation;
    private CountryPickerDialog countryPicker = null;
    private Activity activity;
    private boolean fromFacebook;
    private String facebookEmail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        fromFacebook = SignUpFragmentArgs.fromBundle(getArguments()).getFromFacebook();
        facebookEmail = SignUpFragmentArgs.fromBundle(getArguments()).getEmail();

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Navigation.findNavController(getView()).popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sign_up_fragment, container, false);

        setProgressBar(root.findViewById(R.id.progress_bar));

        edtEmail = root.findViewById(R.id.edit_reg_email);
        edtPassword = root.findViewById(R.id.edit_reg_password);
        edtUsername = root.findViewById(R.id.edit_reg_username);
        edtNation = root.findViewById(R.id.edit_reg_nation);
        View passwordField = root.findViewById(R.id.password_field);
        Button signUp = root.findViewById(R.id.btn_sign_up);

        if (fromFacebook) {
            edtEmail.setText(facebookEmail);
            edtEmail.setEnabled(false);
            passwordField.setVisibility(View.GONE);
        }

        edtNation.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                countryPicker = new CountryPickerDialog(getContext(), (country, flagResId) -> {
                    edtNation.setText(country.getCountryName(SurprixApplication.getSurprixContext()));
                    countryPicker.dismiss();
                }, false, 0);
                countryPicker.show();
            }
        });

        signUp.setOnClickListener(new SignUpOnClick());

        return root;
    }

    private class SignUpOnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            SystemUtils.closeKeyboard(activity);

            final String email = edtEmail.getText().toString().trim();
            final String password = edtPassword.getText().toString().trim();
            final String username = edtUsername.getText().toString().trim().toLowerCase();
            final String nation = edtNation.getText().toString();

            if (!SystemUtils.checkNetworkAvailability()) {
                Toast.makeText(getApplicationContext(), R.string.network_unavailable, Toast.LENGTH_SHORT).show();
                return;
            }

            LoginFlowHelper.AuthMode authMode = fromFacebook ? LoginFlowHelper.AuthMode.FACEBOOK : LoginFlowHelper.AuthMode.EMAIL_PASSWORD;
            LoginFlowHelper.signUp(email, password, username, nation, getActivity(), authMode, new CallbackWithExceptionInterface() {
                @Override
                public void onStart() {
                    showLoading();
                }

                @Override
                public void onSuccess() {
                    hideLoading();
                    Navigation.findNavController(view).navigate(SignUpFragmentDirections.actionNavigationLoginSignupToMainActivity());
                    activity.finish();
                }

                @Override
                public void onFailure(Exception e) {
                    hideLoading();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
