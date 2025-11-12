package com.univalle.inventoryapp.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.univalle.inventoryapp.R
import com.univalle.inventoryapp.utils.Prefs
import com.univalle.inventoryapp.utils.PriceFormatter

import com.univalle.inventoryapp.utils.WidgetUpdate
import kotlinx.coroutines.launch
import com.univalle.inventoryapp.viewmodel.InventoryViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ////////////////HU1 linea para hacer prueba simple
        WidgetUpdate.saveValueForWidget(this,"0")
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