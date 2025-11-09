package com.univalle.inventoryapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.univalle.inventoryapp.R

import android.content.Context
const val PREFS_NAME1 = "com.univalle.inventoryapp.widget.WidgetPrefs"
const val PREF_KEY_WIDGET_VALUE1 = "app_value"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ////////////////
        saveValueForWidget(this,"900000")
    }
    //////////////////////////////////
    //////////////////////////////////
    fun saveValueForWidget(context: Context, value: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME1, Context.MODE_PRIVATE)
        prefs.edit().putString(PREF_KEY_WIDGET_VALUE1, value).apply()
    }
    //////////////////////////////////
    //////////////////////////////////
}