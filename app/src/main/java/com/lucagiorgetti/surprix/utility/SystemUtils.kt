package com.lucagiorgetti.surprix.utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.SearchAutoComplete
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface
import com.lucagiorgetti.surprix.listenerInterfaces.LoginFlowCallbackInterface
import com.lucagiorgetti.surprix.model.User
import com.lucagiorgetti.surprix.utility.dao.UserDao
import timber.log.Timber

/**
 * Utility which contains all the implementations of methods which needs a connection with Firebase Database.
 *
 *
 * Created by Luca on 13/11/2017.
 */
object SystemUtils {
    private const val SET_HINT_DISPLAYED = "setHintDisplayed"
    private const val THEME_DARK_SELECTED = "darkThemeSelected"

    fun checkNetworkAvailability(): Boolean {
        val context = SurprixApplication.instance.applicationContext

        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork
            return if (activeNetwork == null) {
                false // if there is no active network, then simply no internet connection.
            } else {
                // NetworkCapabilities object contains information about properties of a network
                val netCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
                (netCapabilities != null
                        // indicates that the network is set up to access the internet
                        && netCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        // indicates that the network provides actual access to the public internet when it is probed
                        && netCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED))
            }
        } catch (e: Exception) {
            return false
        }
    }

    fun closeKeyboard(activity: Activity?) {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun openNewActivityWithFinishing(activity: Activity, cls: Class<*>?) {
        val applicationContext = SurprixApplication.instance.applicationContext
        val i = Intent(applicationContext, cls)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(i)
        activity.finish()
    }

    fun firstTimeOpeningApp() {
        setHintDisplayed(false)
    }

    fun setHintDisplayed(displayed: Boolean){
        val edit = PreferenceManager.getDefaultSharedPreferences(SurprixApplication.instance.applicationContext).edit()
        edit.putBoolean(SET_HINT_DISPLAYED, displayed)
        edit.apply()
    }

    fun getHintDisplayed() : Boolean{
        return PreferenceManager.getDefaultSharedPreferences(SurprixApplication.instance.applicationContext).getBoolean(SET_HINT_DISPLAYED, true)
    }

    var darkThemePreference: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(SurprixApplication.instance.applicationContext).getBoolean(THEME_DARK_SELECTED, false)
        set(darkEnabled) {
            val applicationContext = SurprixApplication.instance.applicationContext
            val edit = PreferenceManager.getDefaultSharedPreferences(applicationContext).edit()
            edit.putBoolean(THEME_DARK_SELECTED, darkEnabled)
            edit.apply()
        }

    fun enableFCM() {
        // Enable FCM via enable Auto-init service which generate new token and receive in FCMService
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        Timber.i("FCM enable")
        FirebaseMessaging.getInstance().subscribeToTopic("global")
    }

    private fun disableFCM() {
        // Disable auto init
        FirebaseMessaging.getInstance().isAutoInitEnabled = false
        Thread {

            // Remove InstanceID initiate to unsubscribe all topic
            FirebaseMessaging.getInstance().unsubscribeFromTopic("global")
            Timber.i("FCM disable")
        }.start()
    }

    fun openNewActivity(cls: Class<*>?, b: Bundle?) {
        val applicationContext = SurprixApplication.instance.applicationContext
        val i = Intent(applicationContext, cls)
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (b != null) {
            i.putExtras(b)
        }
        applicationContext.startActivity(i)
    }

    fun setSessionUser(uid: String?, listener: LoginFlowCallbackInterface) {
        UserDao.getUserByUid(uid, object : CallbackInterface<User?> {
            override fun onSuccess(currentUser: User?) {
                if (currentUser != null) {
                    SurprixApplication.instance.currentUser = currentUser
                    listener.onSuccess()
                } else {
                    listener.onFailure(Exception(SurprixApplication.instance.applicationContext.getString(R.string.something_went_wrong)))
                }
            }

            override fun onStart() {
                listener.onStart()
            }

            override fun onFailure() {
                listener.onFailure(Exception(SurprixApplication.instance.applicationContext.getString(R.string.something_went_wrong)))
            }
        })
    }

    private fun removeSessionUser() {
        SurprixApplication.instance.currentUser = null
    }

    fun logout() {
        disableFCM()
        removeSessionUser()
        FirebaseAuth.getInstance().signOut()
        LoginManager.getInstance().logOut()
    }

    fun loadImage(path: String?, vImage: ImageView?, placeHolder: Int) {
        if (path!!.startsWith("gs")) {
            val storage = SurprixApplication.instance.firebaseStorage
            val gsReference = storage!!.getReferenceFromUrl(path)
            Glide.with(SurprixApplication.instance.applicationContext).load(gsReference).apply(RequestOptions()
                    .placeholder(placeHolder))
                    .into(vImage!!)
        } else {
            Glide.with(SurprixApplication.instance.applicationContext).load(path).apply(RequestOptions()
                    .placeholder(placeHolder))
                    .into(vImage!!)
        }
    }

    fun setSearchViewStyle(searchView: SearchView?) {
        val searchAutoComplete = searchView!!.findViewById<SearchAutoComplete>(R.id.search_src_text)
        val mCloseButton = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        val mSearchButton = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        val searchMagIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        val searchBadge = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_go_btn)
        val zxc = searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)
        searchAutoComplete.setHintTextColor(SurprixApplication.instance.applicationContext.resources.getColor(R.color.white, SurprixApplication.instance.applicationContext.theme))
        searchAutoComplete.setTextColor(SurprixApplication.instance.applicationContext.resources.getColor(R.color.white, SurprixApplication.instance.applicationContext.theme))
        searchMagIcon.setColorFilter(SurprixApplication.instance.applicationContext.resources.getColor(R.color.white, SurprixApplication.instance.applicationContext.theme))
        searchBadge.setColorFilter(SurprixApplication.instance.applicationContext.resources.getColor(R.color.white, SurprixApplication.instance.applicationContext.theme))
        mCloseButton.setColorFilter(SurprixApplication.instance.applicationContext.resources.getColor(R.color.white, SurprixApplication.instance.applicationContext.theme))
        mSearchButton.setColorFilter(SurprixApplication.instance.applicationContext.resources.getColor(R.color.white, SurprixApplication.instance.applicationContext.theme))
        zxc.setBackgroundColor(Color.TRANSPARENT)
    }

    fun getColumnsNumber(recyclerView: RecyclerView?): Int {
        return if (recyclerView!!.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 1 else 2
    }
}
