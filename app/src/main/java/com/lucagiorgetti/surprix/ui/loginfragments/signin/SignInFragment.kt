package com.lucagiorgetti.surprix.ui.loginfragments.signin

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation.findNavController
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface
import com.lucagiorgetti.surprix.listenerInterfaces.LoginFlowCallbackInterface
import com.lucagiorgetti.surprix.utility.AuthUtils
import com.lucagiorgetti.surprix.utility.BaseFragment
import com.lucagiorgetti.surprix.utility.LoginFlowHelper
import com.lucagiorgetti.surprix.utility.SystemUtils
import timber.log.Timber

class SignInFragment : BaseFragment() {
    private var inEmail: EditText? = null
    private var inPassword: EditText? = null
    private var activity: Activity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = getActivity()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_sign_in, container, false)
        inEmail = root.findViewById(R.id.sign_in_email)
        inPassword = root.findViewById(R.id.sign_in_password)
        setProgressBar(root.findViewById(R.id.progress_bar))
        val loginBtn = root.findViewById<Button>(R.id.login_button)
        val forgotPwd = root.findViewById<TextView>(R.id.login_forgot_password)
        forgotPwd.setOnClickListener { v: View -> openResetPwdDialog(v, container) }
        loginBtn.setOnClickListener(LoginOnClick())
        return root
    }

    private fun openResetPwdDialog(parentView: View, container: ViewGroup?) {
        val view = layoutInflater.inflate(R.layout.dialog_password_reset, container, false)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(view)
        builder.setTitle(R.string.forgot_yout_password)
        val inEmail = view.findViewById<EditText>(R.id.login_reset_pwd_email)
        val resetBtn = view.findViewById<Button>(R.id.login_reset_pwd_submit)
        val resetDialog = builder.create()
        resetDialog.show()
        resetBtn.setOnClickListener {
            SystemUtils.closeKeyboard(getActivity())
            if (inEmail == null || inEmail.text.toString() == "") {
                Toast.makeText(context, R.string.signup_complete_all_fields, Toast.LENGTH_SHORT).show()
            } else {
                AuthUtils.sendPasswordResetEmail(inEmail.text.toString().trim { it <= ' ' }, object : CallbackInterface<Boolean?> {
                    override fun onStart() {
                        showLoading()
                    }

                    override fun onSuccess(item: Boolean?) {
                        Toast.makeText(context, R.string.mail_successfully_sent, Toast.LENGTH_SHORT).show()
                        hideLoading()
                        resetDialog.dismiss()
                        findNavController(parentView).popBackStack()
                    }

                    override fun onFailure() {
                        Toast.makeText(context, R.string.cannot_send_recovery_email, Toast.LENGTH_SHORT).show()
                        resetDialog.dismiss()
                        hideLoading()
                    }
                })
            }
        }
    }

    private inner class LoginOnClick : View.OnClickListener {
        override fun onClick(view: View) {
            SystemUtils.closeKeyboard(getActivity())
            if (!SystemUtils.checkNetworkAvailability()) {
                Toast.makeText(context, R.string.network_unavailable, Toast.LENGTH_SHORT).show()
                return
            }
            val email = inEmail!!.text.toString().trim { it <= ' ' }
            Timber.d("Input email : %s", email)
            val pwd = inPassword!!.text.toString().trim { it <= ' ' }
            Timber.d("Input pwd : %s", pwd)
            LoginFlowHelper.signInWithEmailPassword(email, pwd, getActivity(), object : LoginFlowCallbackInterface {
                override fun onStart() {
                    showLoading()
                }

                override fun onSuccess() {
                    hideLoading()
                    findNavController(view).navigate(SignInFragmentDirections.actionNavigationLoginSigninToMainActivity())
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
