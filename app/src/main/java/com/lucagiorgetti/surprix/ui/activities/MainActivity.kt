package com.lucagiorgetti.surprix.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.AppBarConfiguration.Builder
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lucagiorgetti.surprix.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        val appBarConfiguration: AppBarConfiguration = Builder(
                R.id.navigation_missing_list, R.id.navigation_double_list, R.id.navigation_catalog_producer, R.id.navigation_about_me)
                .build()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //ForceUpdateChecker.with(this).onUpdateNeeded(this).check();
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        var navController: NavController? = null
        if (navHostFragment != null) {
            navController = navHostFragment.navController
        }
        setupActionBarWithNavController(this@MainActivity, navController!!, appBarConfiguration)
        setupWithNavController(navView, navController)
        setupWithNavController(toolbar, navController, appBarConfiguration)
    }
}