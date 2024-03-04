package com.lucagiorgetti.surprix.ui.loginfragments.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import com.facebook.CallbackManager
import com.facebook.CallbackManager.Factory.create
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication.Companion.getInstance
import com.lucagiorgetti.surprix.listenerInterfaces.LoginFlowCallbackInterface
import com.lucagiorgetti.surprix.ui.activities.MainActivity
import com.lucagiorgetti.surprix.utility.BaseFragment
import com.lucagiorgetti.surprix.utility.LoginFlowHelper
import com.lucagiorgetti.surprix.utility.LoginFlowHelper.UserNeedToCompleteSignUpException
import com.lucagiorgetti.surprix.utility.SystemUtils
import timber.log.Timber

class LoginHomeFragment : BaseFragment() {
    private var callbackManager: CallbackManager? = null
    private var googleSignInClient: GoogleSignInClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        AppEventsLogger.activateApp(getInstance())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_login_home, container, false)
        callbackManager = create()
        val progressBar = root.findViewById<ProgressBar>(R.id.login_loading)
        setProgressBar(progressBar)
        val facebookCustomLogin = root.findViewById<Button>(R.id.btn_start_facebook)
        val login = root.findViewById<Button>(R.id.btn_sign_in)
        val registerBtn = root.findViewById<Button>(R.id.btn_sign_up)
        val googleLogin = root.findViewById<Button>(R.id.btn_google_login)

        facebookCustomLogin.setOnClickListener { view: View? ->
            if (SystemUtils.checkNetworkAvailability()) {
                LoginFlowHelper.loginWithFacebook(requireActivity(), this, callbackManager, object : LoginFlowCallbackInterface {
                    override fun onStart() {
                        showLoading()
                    }

                    override fun onSuccess() {
                        hideLoading()
                        SystemUtils.openNewActivityWithFinishing(requireActivity(), MainActivity::class.java)
                        // getActivity().finish();
                    }

                    override fun onFailure(e: Exception) {
                        hideLoading()
                        if (e is UserNeedToCompleteSignUpException) {
                            hideLoading()
                            findNavController(view!!).navigate(LoginHomeFragmentDirections.actionNavigationLoginHomeToNavigationLoginPrivacy(e.currentUser?.email, true))
                        } else {
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
        googleLogin.setOnClickListener { view: View? ->
            if (SystemUtils.checkNetworkAvailability()) {
                val signInIntent = googleSignInClient!!.signInIntent
                showLoading()
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }
        login.setOnClickListener { v: View? -> findNavController(v!!).navigate(LoginHomeFragmentDirections.actionNavigationLoginHomeToNavigationLoginSignin()) }
        registerBtn.setOnClickListener { v: View? -> findNavController(v!!).navigate(LoginHomeFragmentDirections.actionNavigationLoginHomeToNavigationLoginPrivacy(null, false)) }
        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                Timber.d("firebaseAuthWithGoogle:%s", account.id)
                LoginFlowHelper.loginWithGoogle(requireActivity(), account.idToken, object : LoginFlowCallbackInterface {
                    override fun onStart() {}
                    override fun onSuccess() {
                        hideLoading()
                        SystemUtils.openNewActivityWithFinishing(requireActivity(), MainActivity::class.java)
                    }

                    override fun onFailure(e: Exception) {
                        if (e is UserNeedToCompleteSignUpException) {
                            hideLoading()
                            findNavController(view!!).navigate(LoginHomeFragmentDirections.actionNavigationLoginHomeToNavigationLoginPrivacy(e.currentUser?.email, true))
                        } else {
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Timber.w("Google sign in failed")
                // ...
            }
        } else {
            try {
                callbackManager!!.onActivityResult(requestCode, resultCode, data)
            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        const val RC_SIGN_IN = 2345
    }
}
