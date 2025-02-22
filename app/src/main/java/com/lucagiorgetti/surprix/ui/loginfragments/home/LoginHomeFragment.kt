package com.lucagiorgetti.surprix.ui.loginfragments.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.navigation.Navigation.findNavController
import com.facebook.CallbackManager
import com.facebook.CallbackManager.Factory.create
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.listenerInterfaces.LoginFlowCallbackInterface
import com.lucagiorgetti.surprix.ui.activities.MainActivity
import com.lucagiorgetti.surprix.utility.BaseFragment
import com.lucagiorgetti.surprix.utility.LoginFlowHelper
import com.lucagiorgetti.surprix.utility.LoginFlowHelper.UserNeedToCompleteSignUpException
import com.lucagiorgetti.surprix.utility.SystemUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.security.MessageDigest
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class LoginHomeFragment : BaseFragment(), CoroutineScope {
    private var callbackManager: CallbackManager? = null
    private var googleSignInClient: GoogleSignInClient? = null
    private var job: Job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        AppEventsLogger.activateApp(SurprixApplication.instance)
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
        googleLogin.setOnClickListener {
            if (SystemUtils.checkNetworkAvailability()) {
                showLoading()
                launch {
                    googleSignIn(requireContext())
                }
            } else {
                hideLoading()
            }
        }
        login.setOnClickListener { v: View? -> findNavController(v!!).navigate(LoginHomeFragmentDirections.actionNavigationLoginHomeToNavigationLoginSignin()) }
        registerBtn.setOnClickListener { v: View? -> findNavController(v!!).navigate(LoginHomeFragmentDirections.actionNavigationLoginHomeToNavigationLoginPrivacy(null, false)) }
        return root
    }

    private suspend fun googleSignIn(context: Context) {
        try {
            // Initialize Credential Manager
            val credentialManager: CredentialManager = CredentialManager.create(context)

            // Generate a nonce (a random number used once)
            val ranNonce: String = UUID.randomUUID().toString()
            val bytes: ByteArray = ranNonce.toByteArray()
            val md: MessageDigest = MessageDigest.getInstance("SHA-256")
            val digest: ByteArray = md.digest(bytes)
            val hashedNonce: String = digest.fold("") { str, it -> str + "%02x".format(it) }

            // Set up Google ID option
            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("500151287055-r40li7rdm5ta8illnlpgjjv7rbqfh59a.apps.googleusercontent.com")
                .setNonce(hashedNonce)
                .build()

            // Request credentials
            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            // Get the credential result
            val result = credentialManager.getCredential(context, request)
            val credential = result.credential

            // Check if the received credential is a valid Google ID Token
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(credential.data)

                Timber.d("firebaseAuthWithGoogle:%s", googleIdTokenCredential.id)
                LoginFlowHelper.loginWithGoogle(requireActivity(), googleIdTokenCredential.idToken, object : LoginFlowCallbackInterface {
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
            } else {
                throw RuntimeException("Received an invalid credential type")
            }
        } catch (e: GetCredentialCancellationException) {
            Timber.e(Exception("Sign-in was canceled. Please try again."))
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}
