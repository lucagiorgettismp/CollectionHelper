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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackWithExceptionInterface;
import com.lucagiorgetti.surprix.ui.activities.MainActivity;
import com.lucagiorgetti.surprix.utility.BaseFragment;
import com.lucagiorgetti.surprix.utility.LoginFlowHelper;
import com.lucagiorgetti.surprix.utility.SystemUtils;

import timber.log.Timber;

public class LoginHomeFragment extends BaseFragment {

    private CallbackManager callbackManager;
    private GoogleSignInClient googleSignInClient;
    public static final int RC_SIGN_IN = 2345;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
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
        Button googleLogin = root.findViewById(R.id.btn_google_login);

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
                        SystemUtils.openNewActivityWithFinishing(getActivity(), MainActivity.class);
                       // getActivity().finish();
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

        googleLogin.setOnClickListener(view -> {
            if (SystemUtils.checkNetworkAvailability()) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                showLoading();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        login.setOnClickListener(v -> Navigation.findNavController(v).navigate(LoginHomeFragmentDirections.actionNavigationLoginHomeToNavigationLoginSignin()));
        registerBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(LoginHomeFragmentDirections.actionNavigationLoginHomeToNavigationLoginPrivacy(null, false)));
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Timber.d("firebaseAuthWithGoogle:%s", account.getId());
                LoginFlowHelper.loginWithGoogle(getActivity(), account.getIdToken(), new CallbackWithExceptionInterface() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess() {
                        hideLoading();
                        SystemUtils.openNewActivityWithFinishing(getActivity(), MainActivity.class);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        if (e instanceof LoginFlowHelper.UserNeedToCompleteSignUpException) {
                            hideLoading();
                            Navigation.findNavController(getView()).navigate(LoginHomeFragmentDirections.actionNavigationLoginHomeToNavigationLoginPrivacy(((LoginFlowHelper.UserNeedToCompleteSignUpException) e).getCurrentUser().getEmail(), true));
                        } else {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }                    }
                });
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Timber.w("Google sign in failed");
                // ...
            }


        } else {
            try {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            } catch (Exception e){
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
