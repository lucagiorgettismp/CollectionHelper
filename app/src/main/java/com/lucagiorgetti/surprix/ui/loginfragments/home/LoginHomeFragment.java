package com.lucagiorgetti.surprix.ui.loginfragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.facebook.CallbackManager;
import com.facebook.appevents.AppEventsLogger;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackWithExceptionInterface;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.LoginFlowHelper;
import com.lucagiorgetti.surprix.utility.SystemUtils;

public class LoginHomeFragment extends BaseFragment {

    private CallbackManager callbackManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppEventsLogger.activateApp(SurprixApplication.getInstance());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login_home, container, false);

        callbackManager = CallbackManager.Factory.create();

        ProgressBar progressBar = root.findViewById(R.id.login_loading);
        setProgressBar(progressBar);

        Button facebookCustomLogin = root.findViewById(R.id.btn_start_facebook);

        Button login = root.findViewById(R.id.btn_sign_in);
        Button registerBtn = root.findViewById(R.id.btn_sign_up);

        facebookCustomLogin.setOnClickListener(view -> {
            if (SystemUtils.checkNetworkAvailability()) {
                LoginFlowHelper.loginWithFacebook(getActivity(), this, callbackManager, new CallbackWithExceptionInterface() {
                    @Override
                    public void onStart() {
                        showLoading();
                    }

                    @Override
                    public void onSuccess() {
                        hideLoading();
                        Navigation.findNavController(view).navigate(LoginHomeFragmentDirections.actionNavigationLoginHomeToMainActivity());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        hideLoading();
                        if (e instanceof LoginFlowHelper.UserNeedToCompleteSignUpException) {
                            hideLoading();
                            Navigation.findNavController(view).navigate(LoginHomeFragmentDirections.actionNavigationLoginHomeToNavigationLoginPrivacy(((LoginFlowHelper.UserNeedToCompleteSignUpException) e).getCurrentUser().getEmail(), true));
                        } else {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        login.setOnClickListener(v -> Navigation.findNavController(v).navigate(LoginHomeFragmentDirections.actionNavigationLoginHomeToNavigationLoginSignin()));
        registerBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(LoginHomeFragmentDirections.actionNavigationLoginHomeToNavigationLoginPrivacy(null, false)));
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
