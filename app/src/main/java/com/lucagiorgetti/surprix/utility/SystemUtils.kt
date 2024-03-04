package com.lucagiorgetti.surprix.utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
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
import com.lucagiorgetti.surprix.SurprixApplication.Companion.getInstance
import com.lucagiorgetti.surprix.SurprixApplication.Companion.surprixContext
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
        var available = false
        val context = surprixContext
        val networkTypes = intArrayOf(ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI)
        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            for (networkType in networkTypes) {
                var activeNetworkInfo: NetworkInfo? = null
                if (connectivityManager != null) {
                    activeNetworkInfo = connectivityManager.activeNetworkInfo
                }
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.type == networkType) available = true
            }
        } catch (e: Exception) {
            available = false
        }
        return available
    }

    fun closeKeyboard(activity: Activity?) {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun openNewActivityWithFinishing(activity: Activity, cls: Class<*>?) {
        val applicationContext = surprixContext
        val i = Intent(applicationContext, cls)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(i)
        activity.finish()
    }

    fun firstTimeOpeningApp() {
        isSetHintDisplayed = false
    }

    var isSetHintDisplayed: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(surprixContext).getBoolean(SET_HINT_DISPLAYED, true)
        set(displayed) {
            val edit = PreferenceManager.getDefaultSharedPreferences(surprixContext).edit()
            edit.putBoolean(SET_HINT_DISPLAYED, displayed)
            edit.apply()
        }
    var darkThemePreference: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(surprixContext).getBoolean(THEME_DARK_SELECTED, false)
        set(darkEnabled) {
            val applicationContext = surprixContext
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
        val applicationContext = surprixContext
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
                    getInstance().setUser(currentUser)
                    listener.onSuccess()
                } else {
                    listener.onFailure(Exception(surprixContext.getString(R.string.something_went_wrong)))
                }
            }

            override fun onStart() {
                listener.onStart()
            }

            override fun onFailure() {
                listener.onFailure(Exception(surprixContext.getString(R.string.something_went_wrong)))
            }
        })
    }

    private fun removeSessionUser() {
        getInstance().setUser(null)
    }

    fun logout() {
        disableFCM()
        removeSessionUser()
        FirebaseAuth.getInstance().signOut()
        LoginManager.getInstance().logOut()
    }

    fun loadImage(path: String?, vImage: ImageView?, placeHolder: Int) {
        if (path!!.startsWith("gs")) {
            val storage = getInstance().firebaseStorage
            val gsReference = storage!!.getReferenceFromUrl(path)
            Glide.with(surprixContext).load(gsReference).apply(RequestOptions()
                    .placeholder(placeHolder))
                    .into(vImage!!)
        } else {
            Glide.with(surprixContext).load(path).apply(RequestOptions()
                    .placeholder(placeHolder))
                    .into(vImage!!)
        }
    }

    fun setSearchViewStyle(searchView: SearchView?) {
        val searchAutoComplete = searchView!!.findViewById<SearchAutoComplete>(R.id.search_src_text)
        val mCloseButton = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        val mSearchButton = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        val search_mag_icon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        val search_badge = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_go_btn)
        val zxc = searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)
        searchAutoComplete.setHintTextColor(surprixContext.resources.getColor(R.color.white))
        searchAutoComplete.setTextColor(surprixContext.resources.getColor(R.color.white))
        search_mag_icon.setColorFilter(surprixContext.resources.getColor(R.color.white))
        search_badge.setColorFilter(surprixContext.resources.getColor(R.color.white))
        mCloseButton.setColorFilter(surprixContext.resources.getColor(R.color.white))
        mSearchButton.setColorFilter(surprixContext.resources.getColor(R.color.white))
        zxc.setBackgroundColor(Color.TRANSPARENT)
    }

    fun getColumnsNumber(recyclerView: RecyclerView?): Int {
        return if (recyclerView!!.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 1 else 2
    }
}
