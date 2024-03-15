package com.lucagiorgetti.surprix.utility

import android.app.Activity
import android.util.Patterns
import androidx.fragment.app.Fragment
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackWithExceptionInterface
import com.lucagiorgetti.surprix.listenerInterfaces.LoginFlowCallbackInterface
import com.lucagiorgetti.surprix.model.Uid
import com.lucagiorgetti.surprix.model.User
import com.lucagiorgetti.surprix.utility.dao.UserDao
import timber.log.Timber

object LoginFlowHelper {
    // SUP
    fun signUp(
        email: String,
        password: String,
        username: String,
        country: String,
        authMode: AuthMode,
        activity: Activity,
        flowListener: LoginFlowCallbackInterface
    ) {
        flowListener.onStart()
        if (email.isEmpty() || username.isEmpty() || country.isEmpty()) {
            flowListener.onFailure(Exception(SurprixApplication.instance.applicationContext.getString(R.string.signup_complete_all_fields)))
        }
        if (authMode == AuthMode.EMAIL_PASSWORD) {
            if (password.isEmpty() || password.length < 6) {
                flowListener.onFailure(Exception(SurprixApplication.instance.applicationContext.getString(R.string.signup_password_lenght)))
                return
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                flowListener.onFailure(Exception(SurprixApplication.instance.applicationContext.getString(R.string.signup_email_format)))
                return
            }
        }
        UserDao.getUserByUsername(username, object : CallbackInterface<User?> {
            override fun onStart() {}
            override fun onSuccess(userAlreadyPresent: User?) {
                if (userAlreadyPresent == null){
                    createUserAndSignIn(email, password, username, country, authMode, activity, flowListener)
                } else {
                    flowListener.onFailure(Exception(SurprixApplication.instance.applicationContext.getString(R.string.username_existing)))
                }
            }

            override fun onFailure() {
                flowListener.onFailure(Exception(SurprixApplication.instance.applicationContext.getString(R.string.data_sync_error)))
            }
        })
    }

    // SIGNIN
    fun signInWithEmailPassword(email: String, pwd: String, activity: Activity?, flowListener: LoginFlowCallbackInterface) {
        if (email != "" && pwd != "") {
            AuthUtils.signInWithEmailAndPassword(activity, email, pwd, object : CallbackInterface<FirebaseUser> {
                override fun onStart() {
                    flowListener.onStart()
                }

                override fun onSuccess(currentUser: FirebaseUser) {
                    signInSucceeded(currentUser, flowListener)
                }

                override fun onFailure() {
                    flowListener.onFailure(Exception(SurprixApplication.instance.applicationContext.getString(R.string.wrong_email_or_password)))
                }
            })
        } else {
            flowListener.onFailure(Exception(SurprixApplication.instance.applicationContext.getString(R.string.wrong_email_or_password)))
        }
    }

    fun loginWithGoogle(activity: Activity, idToken: String?, flowListener: LoginFlowCallbackInterface) {
        AuthUtils.signInWithGoogleToken(activity, idToken, object : CallbackWithExceptionInterface<FirebaseUser> {
            override fun onStart() {}
            override fun onSuccess(currentUser: FirebaseUser) {
                afterProvidersSignIn(currentUser, flowListener)
            }

            override fun onFailure(e: Exception) {
                manageProviderLoginException(e, flowListener)
            }
        })
    }

    // LOGFACE
    fun loginWithFacebook(activity: Activity, fragment: Fragment?, callbackManager: CallbackManager?, flowListener: LoginFlowCallbackInterface) {
        LoginManager.getInstance().logInWithReadPermissions(fragment!!, callbackManager!!, mutableListOf("email", "public_profile"))
        try {
            flowListener.onStart()
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Timber.d("facebook:onSuccess: %s", loginResult)
                    AuthUtils.signInWithFacebookToken(activity, loginResult.accessToken, object : CallbackWithExceptionInterface<FirebaseUser> {
                        override fun onStart() {}
                        override fun onSuccess(currentUser: FirebaseUser) {
                            afterProvidersSignIn(currentUser, flowListener)
                        }

                        override fun onFailure(exception: Exception) {
                            manageProviderLoginException(exception, flowListener)
                        }
                    })
                }

                override fun onCancel() {
                    flowListener.onFailure(Exception("Facebook login cancelled"))
                }

                override fun onError(error: FacebookException) {
                    Timber.d("facebook:onError: %s", error.message)
                    flowListener.onFailure(Exception("Facebook login error"))
                }
            })
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun afterProvidersSignIn(currentUser: FirebaseUser?, flowListener: LoginFlowCallbackInterface) {
        if (currentUser != null) {
            UserDao.getUserByUid(currentUser.uid, object : CallbackInterface<User?> {
                override fun onStart() {}
                override fun onSuccess(user: User?) {
                    if (user != null) {
                        signInSucceeded(currentUser, flowListener)
                    } else {
                        flowListener.onFailure(UserNeedToCompleteSignUpException(currentUser))
                    }
                }

                override fun onFailure() {
                    // TODO: impossibile cercare utente
                }
            })
        } else {
            flowListener.onFailure(UserNeedToCompleteSignUpException(null))
        }
    }

    // SIS
    private fun signInSucceeded(currentUser: FirebaseUser, listener: LoginFlowCallbackInterface) {
        val uid = currentUser.uid
        SystemUtils.setSessionUser(uid, listener)
        SystemUtils.enableFCM()
    }

    // CREASIG
    private fun createUserAndSignIn(email: String, password: String, username: String, country: String, authMode: AuthMode, activity: Activity?, flowListener: LoginFlowCallbackInterface) {
        when (authMode) {
            AuthMode.FACEBOOK -> {
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null) {
                    completeUserCreation(uid, username, email, country, authMode, flowListener)
                }
            }

            AuthMode.EMAIL_PASSWORD -> AuthUtils.createUserWithEmailAndPassword(activity, email, password, object : LoginFlowCallbackInterface {
                override fun onStart() {}
                override fun onSuccess() {
                    AuthUtils.signInWithEmailAndPassword(activity, email, password, object : CallbackInterface<FirebaseUser> {
                        override fun onStart() {}
                        override fun onSuccess(currentUser: FirebaseUser) {
                            completeUserCreation(currentUser.uid, username, email, country, authMode, flowListener)
                        }

                        override fun onFailure() {
                            flowListener.onFailure(Exception(SurprixApplication.instance.applicationContext.getString(R.string.signup_default_error)))
                        }
                    })
                }

                override fun onFailure(e: Exception) {
                    val message: String = when (e) {
                        is FirebaseAuthWeakPasswordException -> {
                            SurprixApplication.instance.applicationContext.getString(R.string.signup_weak_password)
                        }

                        is FirebaseAuthInvalidCredentialsException -> {
                            SurprixApplication.instance.applicationContext.getString(R.string.signup_email_format)
                        }

                        is FirebaseAuthUserCollisionException -> {
                            SurprixApplication.instance.applicationContext.getString(R.string.signup_mail_existinig)
                        }

                        else -> {
                            SurprixApplication.instance.applicationContext.getString(R.string.signup_default_error)
                        }
                    }
                    flowListener.onFailure(Exception(message))
                }
            })

            else -> {}
        }
    }

    private fun completeUserCreation(uid: String, username: String, email: String, country: String, authMode: AuthMode, flowListener: LoginFlowCallbackInterface) {
        flowListener.onStart()
        val uidModel = Uid(uid, username, authMode)
        UserDao.addUid(uidModel)
        UserDao.newCreateUser(email, username, country, authMode)
        SystemUtils.firstTimeOpeningApp()
        SystemUtils.enableFCM()
        SystemUtils.setSessionUser(uid, flowListener)
    }

    private fun manageProviderLoginException(exception: Exception, flowListener: LoginFlowCallbackInterface) {
        if (exception is FirebaseAuthUserCollisionException) {
            flowListener.onFailure(Exception(SurprixApplication.instance.applicationContext.getString(R.string.auth_failed_other_provider)))
        }
        flowListener.onFailure(Exception(SurprixApplication.instance.applicationContext.getString(R.string.auth_failed)))
    }

    enum class AuthMode {
        EMAIL_PASSWORD,
        FACEBOOK,
        GOOGLE;

        val provider: String
            get() {
                return when (this) {
                    EMAIL_PASSWORD -> "password"
                    FACEBOOK -> "facebook.com"
                    GOOGLE -> "google.com"
                }
            }

        companion object {
            fun fromString(provider: String): AuthMode? {
                when (provider) {
                    "password" -> return EMAIL_PASSWORD
                    "facebook.com" -> return FACEBOOK
                    "google.com" -> return GOOGLE
                }
                return null
            }
        }
    }

    class UserNeedToCompleteSignUpException(val currentUser: FirebaseUser?) : Exception()
}
