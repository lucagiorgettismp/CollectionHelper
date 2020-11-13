package com.lucagiorgetti.surprix.ui.loginfragments.signin;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackWithExceptionInterface;
import com.lucagiorgetti.surprix.utility.AuthUtils;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.LoginFlowHelper;
import com.lucagiorgetti.surprix.utility.SystemUtils;

import timber.log.Timber;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SignInFragment extends BaseFragment {
    private EditText inEmail;
    private EditText inPassword;
    private Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sign_in, container, false);

        inEmail = root.findViewById(R.id.sign_in_email);
        inPassword = root.findViewById(R.id.sign_in_password);
        setProgressBar(root.findViewById(R.id.progress_bar));

        Button loginBtn = root.findViewById(R.id.login_button);
        TextView forgotPwd = root.findViewById(R.id.login_forgot_password);

        forgotPwd.setOnClickListener(v -> openResetPwdDialog(v, container));

        loginBtn.setOnClickListener(new LoginOnClick());
        return root;
    }

    private void openResetPwdDialog(View parentView, ViewGroup container) {
        final View view = getLayoutInflater().inflate(R.layout.dialog_password_reset, container, false);

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setTitle(R.string.forgot_yout_password);

        final EditText inEmail = view.findViewById(R.id.login_reset_pwd_email);
        Button resetBtn = view.findViewById(R.id.login_reset_pwd_submit);

        final AlertDialog resetDialog = builder.create();
        resetDialog.show();

        resetBtn.setOnClickListener(v -> {
            if (inEmail == null || inEmail.getText().toString().equals("")) {
                Toast.makeText(getContext(), R.string.signup_complete_all_fields, Toast.LENGTH_SHORT).show();
            } else {
                AuthUtils.sendPasswordResetEmail(inEmail.getText().toString().trim(), new CallbackInterface<Boolean>() {
                    @Override
                    public void onStart() {
                        showLoading();
                    }

                    @Override
                    public void onSuccess(Boolean item) {
                        Toast.makeText(getContext(), R.string.mail_successfully_sent, Toast.LENGTH_SHORT).show();
                        hideLoading();
                        resetDialog.dismiss();
                        Navigation.findNavController(parentView).popBackStack();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getContext(), R.string.cannot_send_recovery_email, Toast.LENGTH_SHORT).show();
                        resetDialog.dismiss();
                        hideLoading();
                    }
                });
            }
        });
    }

    private class LoginOnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (!SystemUtils.checkNetworkAvailability()) {
                Toast.makeText(getApplicationContext(), R.string.network_unavailable, Toast.LENGTH_SHORT).show();
                return;
            }

            String email = inEmail.getText().toString().trim();
            Timber.d("Input email : %s", email);
            String pwd = inPassword.getText().toString().trim();
            Timber.d("Input pwd : %s", pwd);

            LoginFlowHelper.signInWithEmailPassword(email, pwd, getActivity(), new CallbackWithExceptionInterface() {
                @Override
                public void onStart() {
                    showLoading();
                }

                @Override
                public void onSuccess() {
                    hideLoading();
                    Navigation.findNavController(view).navigate(SignInFragmentDirections.actionNavigationLoginSigninToMainActivity());
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
