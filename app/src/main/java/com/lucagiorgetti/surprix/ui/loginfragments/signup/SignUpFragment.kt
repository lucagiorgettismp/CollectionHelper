package com.lucagiorgetti.surprix.ui.loginfragments.signup

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation.findNavController
import com.facebook.login.LoginManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.listenerInterfaces.LoginFlowCallbackInterface
import com.lucagiorgetti.surprix.utility.BaseFragment
import com.lucagiorgetti.surprix.utility.LoginFlowHelper
import com.lucagiorgetti.surprix.utility.LoginFlowHelper.AuthMode
import com.lucagiorgetti.surprix.utility.SystemUtils
import com.mikelau.countrypickerx.Country
import com.mikelau.countrypickerx.CountryPickerDialog
import java.util.Locale

class SignUpFragment : BaseFragment() {
    private var edtEmail: TextInputEditText? = null
    private var edtPassword: EditText? = null
    private var edtUsername: EditText? = null
    private var edtNation: EditText? = null
    private var countryPicker: CountryPickerDialog? = null
    private var activity: Activity? = null
    private var fromFacebook = false
    private var facebookEmail: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = getActivity()
        fromFacebook = SignUpFragmentArgs.fromBundle(requireArguments()).fromFacebook
        facebookEmail = SignUpFragmentArgs.fromBundle(requireArguments()).email

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                FirebaseAuth.getInstance().signOut()
                LoginManager.getInstance().logOut()
                findNavController(view!!).popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_sign_up, container, false)
        setProgressBar(root.findViewById(R.id.progress_bar))
        edtEmail = root.findViewById(R.id.edit_reg_email)
        edtPassword = root.findViewById(R.id.edit_reg_password)
        edtUsername = root.findViewById(R.id.edit_reg_username)
        edtNation = root.findViewById(R.id.edit_reg_nation)
        val passwordField = root.findViewById<View>(R.id.password_field)
        val signUp = root.findViewById<Button>(R.id.btn_sign_up)
        if (fromFacebook) {
            edtEmail!!.setText(facebookEmail)
            edtEmail!!.isEnabled = false
            passwordField.visibility = View.GONE
        }
        edtNation!!.onFocusChangeListener = OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (hasFocus) {
                countryPicker = CountryPickerDialog(context, { country: Country, _: Int ->
                    edtNation!!.setText(country.getCountryName(SurprixApplication.instance.applicationContext))
                    countryPicker!!.dismiss()
                }, false, 0)
                countryPicker!!.show()
            }
        }
        signUp.setOnClickListener(SignUpOnClick())
        return root
    }

    private inner class SignUpOnClick : View.OnClickListener {
        override fun onClick(view: View) {
            SystemUtils.closeKeyboard(activity)
            val email = edtEmail!!.text.toString().trim { it <= ' ' }
            val password = edtPassword!!.text.toString().trim { it <= ' ' }
            val username = edtUsername!!.text.toString().trim { it <= ' ' }.lowercase(Locale.getDefault())
            val nation = edtNation!!.text.toString()
            if (!SystemUtils.checkNetworkAvailability()) {
                Toast.makeText(context, R.string.network_unavailable, Toast.LENGTH_SHORT).show()
                return
            }
            val authMode = if (fromFacebook) AuthMode.FACEBOOK else AuthMode.EMAIL_PASSWORD
            LoginFlowHelper.signUp(email, password, username, nation, authMode, object : LoginFlowCallbackInterface {
                override fun onStart() {
                    showLoading()
                }

                override fun onSuccess() {
                    hideLoading()
                    findNavController(view).navigate(SignUpFragmentDirections.actionNavigationLoginSignupToMainActivity())
                    activity!!.finish()
                }

                override fun onFailure(e: Exception) {
                    hideLoading()
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
