package com.lucagiorgetti.surprix.utility;

import android.app.Activity;
import android.util.Patterns;

import androidx.fragment.app.Fragment;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface;
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackWithExceptionInterface;
import com.lucagiorgetti.surprix.model.Uid;
import com.lucagiorgetti.surprix.model.User;
import com.lucagiorgetti.surprix.utility.dao.UserDao;

import java.util.Collections;

import timber.log.Timber;

public class LoginFlowHelper {
    public enum AuthMode {
        EMAIL_PASSWORD, FACEBOOK, GOOGLE;

        public static AuthMode fromString(String provider) {
            switch (provider) {
                case "password":
                    return EMAIL_PASSWORD;
                case "facebook.com":
                    return FACEBOOK;
                case "google.com":
                    return GOOGLE;
            }
            return null;
        }

        public String getProvider() {
            switch (this) {
                case EMAIL_PASSWORD:
                    return "password";
                case FACEBOOK:
                    return "facebook.com";
                case GOOGLE:
                    return "google.com";
            }
            return "undefined";
        }
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

    // SUP
    public static void signUp(String email, String password, String username, String country, Activity activity, AuthMode authMode, CallbackWithExceptionInterface flowListener) {
        flowListener.onStart();

        if (email.isEmpty() || username.isEmpty() || country.isEmpty()) {
            flowListener.onFailure(new Exception(SurprixApplication.getSurprixContext().getString(R.string.signup_complete_all_fields)));
        }

        if (authMode == AuthMode.EMAIL_PASSWORD) {
            if (password.isEmpty() || password.length() < 6) {
                flowListener.onFailure(new Exception(SurprixApplication.getSurprixContext().getString(R.string.signup_password_lenght)));
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                flowListener.onFailure(new Exception(SurprixApplication.getSurprixContext().getString(R.string.signup_email_format)));
                return;
            }
        }

        UserDao.getUserByUsername(username, new CallbackInterface<User>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(User userAlreadyPresent) {
                if (userAlreadyPresent != null) {
                    flowListener.onFailure(new Exception(SurprixApplication.getSurprixContext().getString(R.string.username_existing)));
                } else {
                    createUserAndSignIn(email, password, username, country, authMode, activity, flowListener);
                }
            }

            @Override
            public void onFailure() {
                flowListener.onFailure(new Exception(SurprixApplication.getSurprixContext().getString(R.string.data_sync_error)));
            }
        });
    }

    // SIGNIN
    public static void signInWithEmailPassword(String email, String pwd, Activity activity, CallbackWithExceptionInterface flowListener) {
        if (!email.equals("") && !pwd.equals("")) {
            AuthUtils.signInWithEmailAndPassword(activity, email, pwd, new CallbackInterface<FirebaseUser>() {
                @Override
                public void onStart() {
                    flowListener.onStart();
                }

                @Override
                public void onSuccess(FirebaseUser currentUser) {
                    signInSucceeded(currentUser, flowListener);
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

    public static void loginWithGoogle(Activity activity, String idToken, CallbackWithExceptionInterface flowListener) {
        AuthUtils.signInWithGoogleToken(activity, idToken, new CallbackInterface<FirebaseUser>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(FirebaseUser currentUser) {
                afterProvidersSignIn(currentUser, flowListener);
            }

            @Override
            public void onFailure() {
                flowListener.onFailure(new Exception(SurprixApplication.getSurprixContext().getString(R.string.auth_failed)));
            }
        });
    }

    // LOGFACE
    public static void loginWithFacebook(Activity activity, Fragment fragment, CallbackManager callbackManager, CallbackWithExceptionInterface flowListener) {

        LoginManager.getInstance().logInWithReadPermissions(fragment, Collections.singletonList("email"));
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
                            afterProvidersSignIn(currentUser, flowListener);
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

    private static void afterProvidersSignIn(FirebaseUser currentUser, CallbackWithExceptionInterface flowListener) {
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
            flowListener.onFailure(new UserNeedToCompleteSignUpException(currentUser));
        }
    }

    // SIS
    private static void signInSucceeded(FirebaseUser currentUser, CallbackWithExceptionInterface listener) {
        String uid = currentUser.getUid();
        SystemUtils.setSessionUser(uid, listener);
        SystemUtils.enableFCM();
    }

    // CREASIG
    private static void createUserAndSignIn(String email, String password, String username, String country, AuthMode authMode, Activity activity, CallbackWithExceptionInterface flowListener) {
        switch (authMode) {
            case FACEBOOK:
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                completeUserCreation(uid, username, email, country, authMode, flowListener);
                break;
            case EMAIL_PASSWORD:
                AuthUtils.createUserWithEmailAndPassword(activity, email, password, new CallbackWithExceptionInterface() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess() {
                        AuthUtils.signInWithEmailAndPassword(activity, email, password, new CallbackInterface<FirebaseUser>() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(FirebaseUser currentUser) {
                                completeUserCreation(currentUser.getUid(), username, email, country, authMode, flowListener);
                            }

                            @Override
                            public void onFailure() {
                                flowListener.onFailure(new Exception(SurprixApplication.getSurprixContext().getString(R.string.signup_default_error)));
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        String message;
                        if (exception instanceof FirebaseAuthWeakPasswordException) {
                            message = SurprixApplication.getSurprixContext().getString(R.string.signup_weak_password);
                        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            message = SurprixApplication.getSurprixContext().getString(R.string.signup_email_format);
                        } else if (exception instanceof FirebaseAuthUserCollisionException) {
                            message = SurprixApplication.getSurprixContext().getString(R.string.signup_mail_existinig);
                        } else {
                            message = SurprixApplication.getSurprixContext().getString(R.string.signup_default_error);
                        }
                        flowListener.onFailure(new Exception(message));
                    }
                });
                break;
        }
    }

    private static void completeUserCreation(String uid, String username, String email, String country, AuthMode authMode, CallbackWithExceptionInterface flowListener) {
        flowListener.onStart();
        Uid uidModel = new Uid(uid, username, authMode);
        UserDao.addUid(uidModel);
        UserDao.newCreateUser(email, username, country, authMode);

        SystemUtils.firstTimeOpeningApp();
        SystemUtils.enableFCM();
        SystemUtils.setSessionUser(uid, flowListener);
    }
}
