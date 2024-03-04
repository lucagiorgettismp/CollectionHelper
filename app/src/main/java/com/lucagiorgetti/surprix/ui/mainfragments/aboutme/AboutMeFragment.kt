package com.lucagiorgetti.surprix.ui.mainfragments.aboutme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.SurprixApplication.Companion.getInstance
import com.lucagiorgetti.surprix.model.User
import com.lucagiorgetti.surprix.ui.activities.LoginActivity
import com.lucagiorgetti.surprix.ui.activities.SettingsActivity
import com.lucagiorgetti.surprix.utility.BaseFragment
import com.lucagiorgetti.surprix.utility.SystemUtils

class AboutMeFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val user: User = getInstance().currentUser!!
        val root = inflater.inflate(R.layout.fragment_about_me, container, false)
        val collectionBtn = root.findViewById<View>(R.id.my_collection_btn)
        val logout = root.findViewById<Button>(R.id.logout_btn)
        val username = root.findViewById<TextView>(R.id.username)
        val email = root.findViewById<TextView>(R.id.email)
        username.text = user.username
        email.text = user.clearedEmail()
        collectionBtn.setOnClickListener { view: View? ->
            val action = AboutMeFragmentDirections.actionNavigationAboutMeToNavigationMyCollection()
            findNavController(requireView()).navigate(action)
        }
        logout.setOnClickListener { v: View? ->
            SystemUtils.logout()
            SystemUtils.openNewActivityWithFinishing(requireActivity(), LoginActivity::class.java)
        }
        setHasOptionsMenu(true)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.my_collection_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_settings) {
            SystemUtils.openNewActivity(SettingsActivity::class.java, null)
        }
        return super.onOptionsItemSelected(item)
    }
}