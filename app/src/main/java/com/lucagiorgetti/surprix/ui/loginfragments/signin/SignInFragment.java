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
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.utility.AuthUtils;
import com.lucagiorgetti.surprix.utility.SystemUtils;

import timber.log.Timber;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SignInFragment extends Fragment {
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
        View root = inflater.inflate(R.layout.sign_in_fragment, container, false);

        inEmail = root.findViewById(R.id.sign_in_email);
        inPassword = root.findViewById(R.id.sign_in_password);

        Button loginBtn = root.findViewById(R.id.login_button);
        TextView forgotPwd = root.findViewById(R.id.login_forgot_password);

        forgotPwd.setOnClickListener(v -> openResetPwdDialog());

        loginBtn.setOnClickListener(new LoginOnClick());
        return root;
    }

    private void openResetPwdDialog() {
        final View view = getLayoutInflater().inflate(R.layout.dialog_password_reset, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setTitle(R.string.forgot_yout_password);

        final EditText inEmail = view.findViewById(R.id.login_reset_pwd_email);
        Button resetBtn = view.findViewById(R.id.login_reset_pwd_submit);

        final AlertDialog resetDialog = builder.create();
        resetDialog.show();

        resetBtn.setOnClickListener(v -> {
            if (inEmail == null || inEmail.equals("")) {
                Toast.makeText(getContext(), R.string.signup_complete_all_fields, Toast.LENGTH_SHORT).show();
            } else {
                AuthUtils.sendPasswordResetEmail(inEmail.getText().toString().trim(), new CallbackInterface<Boolean>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Boolean item) {
                        Toast.makeText(getContext(), R.string.mail_successfully_sent, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getContext(), R.string.cannot_send_recovery_email, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            resetDialog.dismiss();
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
            Timber.w("Input email : %s", email);
            String pwd = inPassword.getText().toString().trim();
            Timber.w("Input pwd : %s", pwd);

            if (!email.equals("") && !pwd.equals("")) {
                AuthUtils.signInWithEmailAndPassword(getActivity(), email, pwd, new CallbackInterface<Boolean>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        if (success) {
                            Navigation.findNavController(view).navigate(SignInFragmentDirections.actionNavigationLoginSigninToMainActivity());
                            activity.finish();
                        }
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getContext(), R.string.wrong_email_or_password,
                                Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(getContext(), R.string.wrong_email_or_password, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
