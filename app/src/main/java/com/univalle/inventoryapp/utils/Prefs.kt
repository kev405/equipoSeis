package com.univalle.inventoryapp.utils

import android.content.Context
import android.content.SharedPreferences


object Prefs {
    private const val PREFS_FILENAME = "app_prefs"
    private const val KEY_IS_LOGGED = "is_logged"


    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    }

    fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        val prefs = getPreferences(context)
        prefs.edit().putBoolean(KEY_IS_LOGGED, isLoggedIn).apply()
    }


    fun isLoggedIn(context: Context): Boolean{
        val prefs = getPreferences(context)
        return prefs.getBoolean(KEY_IS_LOGGED, false)
    }


    fun clearPrefs(context: Context) {
        val prefs = getPreferences(context)
        prefs.edit().clear().apply()
    }
}