package com.lucagiorgetti.surprix.ui.settingsfragments

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface
import com.lucagiorgetti.surprix.model.User
import com.lucagiorgetti.surprix.utility.LoginFlowHelper.AuthMode
import com.lucagiorgetti.surprix.utility.SystemUtils
import com.lucagiorgetti.surprix.utility.dao.UserDao
import com.mikelau.countrypickerx.Country
import com.mikelau.countrypickerx.CountryPickerDialog

class SettingsFragment : PreferenceFragmentCompat() {
    private var activity: Activity? = null
    private var user: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = getActivity()
    }

    private var countryPicker: CountryPickerDialog? = null
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.surprix_preferences, rootKey)
        user = SurprixApplication.instance.currentUser
        val privacyPolicy = findPreference<Preference>(resources.getString(R.string.settings_privacy_key))
        val contactUs = findPreference<Preference>(resources.getString(R.string.settings_contact_us_key))
        val deleteAccount = findPreference<Preference>(resources.getString(R.string.settings_delete_user_key))
        val changePassword = findPreference<Preference>(resources.getString(R.string.settings_change_password_key))
        val countryEdit = findPreference<Preference>(resources.getString(R.string.settings_change_country_key))
        val nightMode = findPreference<SwitchPreferenceCompat>(resources.getString(R.string.settings_night_mode_key))
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.setData(Uri.parse(resources.getString(R.string.mailto_surprix)))
        if (contactUs != null) {
            contactUs.intent = intent
        }
        if (nightMode != null) {
            nightMode.isChecked = SystemUtils.darkThemePreference
            nightMode.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _: Preference?, state: Any ->
                val checked = state as Boolean
                SystemUtils.darkThemePreference = checked
                AppCompatDelegate.setDefaultNightMode(if (checked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
                true
            }
        }
        if (privacyPolicy != null) {
            privacyPolicy.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                findNavController(requireActivity(), R.id.nav_host_fragment).navigate(SettingsFragmentDirections.actionSettingsFragmentToPrivacyPolicyFragment())
                true
            }
        }
        if (countryEdit != null) {
            countryEdit.summary = user?.country
            countryEdit.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                countryPicker = CountryPickerDialog(context, { country: Country, _: Int ->
                    val countryName = country.getCountryName(SurprixApplication.instance.applicationContext)
                    countryEdit.summary = countryName
                    countryPicker!!.dismiss()
                    UserDao.updateUser(countryName)
                }, false, 0)
                countryPicker!!.show()
                false
            }
        }
        if (deleteAccount != null) {
            deleteAccount.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val alertDialog = AlertDialog.Builder(requireActivity()).create()
                alertDialog.setTitle(getString(R.string.delete_account))
                alertDialog.setMessage(getString(R.string.dialod_delete_user_text))
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_positive)
                ) { _: DialogInterface?, _: Int ->
                    UserDao.deleteUser(object : CallbackInterface<Boolean?> {
                        override fun onStart() {}
                        override fun onSuccess(item: Boolean?) {
                            alertDialog.dismiss()
                            SystemUtils.logout()
                            Toast.makeText(context, R.string.username_delete_success, Toast.LENGTH_LONG).show()
                        }

                        override fun onFailure() {}
                    })
                }
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.discard_btn)
                ) { _: DialogInterface?, _: Int -> alertDialog.dismiss() }
                alertDialog.show()
                false
            }
        }
        if (changePassword != null) {
            if (AuthMode.fromString(user?.provider!!) == AuthMode.EMAIL_PASSWORD) {
                changePassword.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                    val view = layoutInflater.inflate(R.layout.dialog_change_password, null)
                    val builder = AlertDialog.Builder(requireActivity())
                    builder.setView(view)
                    builder.setTitle(R.string.change_password)
                    val oldPassword = view.findViewById<EditText>(R.id.edt_dialog_old_pwd)
                    val newPassword = view.findViewById<EditText>(R.id.edt_dialog_new_pwd)
                    val btnChangePwd = view.findViewById<Button>(R.id.btn_dialog_submit)
                    val changePwd = builder.create()
                    changePwd.show()
                    btnChangePwd.setOnClickListener {
                        val oldPwd = oldPassword.text.toString().trim { it <= ' ' }
                        val newPwd = newPassword.text.toString().trim { it <= ' ' }
                        val user = FirebaseAuth.getInstance().currentUser
                        val email: String?
                        if (user != null) {
                            email = user.email
                            if (email != null) {
                                if (oldPwd != "" && newPwd != "") {
                                    val credential = EmailAuthProvider.getCredential(email, oldPwd)
                                    user.reauthenticate(credential).addOnCompleteListener { task: Task<Void?> ->
                                        if (task.isSuccessful) {
                                            user.updatePassword(newPwd).addOnCompleteListener { task1: Task<Void?> ->
                                                if (!task1.isSuccessful) {
                                                    Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                                                } else {
                                                    Toast.makeText(context, R.string.password_changed, Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            changePwd.dismiss()
                                        } else {
                                            Toast.makeText(context, R.string.old_password_wrong, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, R.string.password_cannot_be_null, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    false
                }
            } else {
                changePassword.isVisible = false
            }
        }
    }
}