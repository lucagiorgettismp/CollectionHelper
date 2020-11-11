package com.lucagiorgetti.surprix.utility;

import android.app.Activity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackWithExceptionInterface;
import com.lucagiorgetti.surprix.model.User;
import com.lucagiorgetti.surprix.utility.dao.UserDao;

import java.util.Collections;

import timber.log.Timber;

public class LoginFlowHelper {
    public enum AuthMode {
        EMAIL_PASSWORD, FACEBOOK
    }

    public static class UserNeedToCompleteSignUpException extends Exception {
        private FirebaseUser currentUser;

        public UserNeedToCompleteSignUpException(FirebaseUser currentUser) {
            this.currentUser = currentUser;
        }

        public FirebaseUser getCurrentUser() {
            return currentUser;
        }
    }

    public static Exception UserNeedToCompleteSignUpException = new Exception();


    private void signUp() {

    }

    private void createUser(String uid, String username, String email, String country, AuthMode authMode) {
        UserDao.addUid(uid, username, authMode);
        UserDao.newCreateUser(email, username, country, authMode);
    }

    // SIGNIN
    public static void signInWithEmailPassword(String email, String pwd, Activity activity, CallbackWithExceptionInterface flowListener) {
        if (!email.equals("") && !pwd.equals("")) {
            AuthUtils.signInWithEmailAndPassword(activity, email, pwd, new CallbackInterface<Boolean>() {
                @Override
                public void onStart() {
                    flowListener.onStart();
                }

                @Override
                public void onSuccess(Boolean success) {
                    if (success) {
                        signInSucceeded(FirebaseAuth.getInstance().getCurrentUser(), flowListener);
                    }
                }

                @Override
                public void onFailure() {
                    flowListener.onFailure(new Exception(SurprixApplication.getSurprixContext().getString(R.string.wrong_email_or_password)));
                }
            });

        } else {
            flowListener.onFailure(new Exception(SurprixApplication.getSurprixContext().getString(R.string.wrong_email_or_password)));
        }
    }

    // LOGFACE
    public static void loginWithFacebook(Activity activity, CallbackManager callbackManager, CallbackWithExceptionInterface flowListener) {

        LoginManager.getInstance().logInWithReadPermissions(activity, Collections.singletonList("email"));
        try {
            flowListener.onStart();
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Timber.d("facebook:onSuccess: %s", loginResult);

                    AuthUtils.signInWithFacebookToken(activity, loginResult.getAccessToken(), new CallbackInterface<FirebaseUser>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(FirebaseUser currentUser) {
                            if (currentUser != null) {
                                UserDao.getUserByUid(currentUser.getUid(), new CallbackInterface<User>() {
                                    @Override
                                    public void onStart() {

                                    }

                                    @Override
                                    public void onSuccess(User user) {
                                        if (user != null) {
                                            signInSucceeded(currentUser, flowListener);
                                        } else {
                                            flowListener.onFailure(new UserNeedToCompleteSignUpException(currentUser));
                                        }
                                    }

                                    @Override
                                    public void onFailure() {
                                        // TODO: impossibile cercare utente
                                    }
                                });
                            } else {
                                // TODO: male perchè facebook scazza
                            }
                        }

                        @Override
                        public void onFailure() {
                            flowListener.onFailure(new Exception(SurprixApplication.getSurprixContext().getString(R.string.auth_failed)));
                        }
                    });
                }

                @Override
                public void onCancel() {
                    flowListener.onFailure(new Exception("Facebook login cancelled"));
                }

                @Override
                public void onError(FacebookException error) {
                    flowListener.onFailure(new Exception("Facebook login error"));
                }
            });
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    // SIS
    private static void signInSucceeded(FirebaseUser currentUser, CallbackWithExceptionInterface listener) {
        String uid = currentUser.getUid();
        SystemUtils.setSessionUser(uid, listener);
    }
}
