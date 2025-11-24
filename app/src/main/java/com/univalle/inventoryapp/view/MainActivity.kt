package com.univalle.inventoryapp.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.univalle.inventoryapp.R
import com.univalle.inventoryapp.utils.Prefs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigationContainer) as? NavHostFragment
        val navController = navHostFragment!!.navController
        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)

        if (Prefs.isLoggedIn(this)) {
            graph.startDestination = R.id.homeInventoryFragment
        } else {
            graph.startDestination = R.id.authenticationFragment
        }

        navController.graph = graph

        handleWidgetIntent(intent, navController)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigationContainer) as? NavHostFragment
        val navController = navHostFragment!!.navController
        setIntent(intent)
        handleWidgetIntent(intent, navController)
    }

    private fun handleWidgetIntent(intent: Intent?, navController: NavController?) {
        intent?.getStringExtra("DESTINATION_FRAGMENT")?.let { destination ->
            when (destination) {
                "LOGIN" -> {
                    if (navController?.currentDestination?.id != R.id.authenticationFragment) {
                        navController?.navigate(R.id.authenticationFragment)
                    }
                }
                "HOME" -> {
                    if (navController?.currentDestination?.id != R.id.homeInventoryFragment) {
                        navController?.navigate(R.id.homeInventoryFragment)
                    }
                }
            }
        }
    }
}