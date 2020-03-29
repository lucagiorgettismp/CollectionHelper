package com.lucagiorgetti.surprix.ui.loginfragments.home;

import android.app.Activity;
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
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.utility.AuthUtils;
import com.lucagiorgetti.surprix.utility.DatabaseUtils;
import com.lucagiorgetti.surprix.utility.SystemUtils;

import java.util.Collections;

import timber.log.Timber;

public class LoginHomeFragment extends Fragment {
    private ProgressBar progressBar;
    private CallbackManager callbackManager;
    private Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppEventsLogger.activateApp(SurprixApplication.getInstance());
        activity = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_home_fragment, container, false);
        callbackManager = CallbackManager.Factory.create();

        progressBar = root.findViewById(R.id.login_loading);

        Button facebookCustomLogin = root.findViewById(R.id.btn_start_facebook);

        Button login = root.findViewById(R.id.btn_sign_in);
        Button registerBtn = root.findViewById(R.id.btn_sign_up);

        facebookCustomLogin.setOnClickListener(view -> {
            if (SystemUtils.checkNetworkAvailability()) {
                progressBar.setVisibility(View.VISIBLE);
                LoginManager.getInstance().logInWithReadPermissions(this, Collections.singletonList("email"));
                try {
                    LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Timber.d("facebook:onSuccess: %s", loginResult);
                            AuthUtils.signInWithFacebookToken(activity, loginResult.getAccessToken(), new CallbackInterface<String>() {
                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onSuccess(String email) {
                                    if (email != null) {
                                        DatabaseUtils.isAnExistingUser(email, new CallbackInterface<Boolean>() {
                                            @Override
                                            public void onStart() {

                                            }

                                            @Override
                                            public void onSuccess(Boolean result) {
                                                if (result) {
                                                    SystemUtils.setSessionUser(email, new CallbackInterface<Boolean>() {
                                                        @Override
                                                        public void onSuccess(Boolean b) {
                                                            SystemUtils.enableFCM();
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            Navigation.findNavController(view).navigate(LoginHomeFragmentDirections.actionNavigationLoginHomeToMainActivity());
                                                            activity.finish();
                                                        }

                                                        @Override
                                                        public void onStart() {

                                                        }

                                                        @Override
                                                        public void onFailure() {
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                        }
                                                    });
                                                } else {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    Navigation.findNavController(view).navigate(LoginHomeFragmentDirections.actionNavigationLoginHomeToNavigationLoginPrivacy(email, true));
                                                }
                                            }

                                            @Override
                                            public void onFailure() {
                                                progressBar.setVisibility(View.INVISIBLE);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(getContext(), R.string.auth_failed, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(getContext(), "Facebook login cancelled", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Toast.makeText(getContext(), "Facebook login error", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                } catch (Exception e){
                    Timber.e(e);
                }
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
