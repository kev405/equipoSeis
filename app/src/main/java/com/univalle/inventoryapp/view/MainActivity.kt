package com.univalle.inventoryapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.univalle.inventoryapp.R
import com.univalle.inventoryapp.utils.Prefs

import com.univalle.inventoryapp.utils.WidgetUpdate

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ////////////////HU1 linea para hacer prueba simple
        WidgetUpdate.saveValueForWidget(this,"780000")
        ///////////////////
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
    }
}