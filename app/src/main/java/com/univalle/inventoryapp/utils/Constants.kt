package com.univalle.inventoryapp.utils

import android.content.Context

object Constants {

    const val NAME_BD: String = "app_data.db"
    const val PREFS_NAME1 = "com.univalle.inventoryapp.widget.WidgetPrefs"
    const val PREF_KEY_WIDGET_VALUE1 = "app_value"
}
object WidgetUpdate {
    fun saveValueForWidget(context: Context, value: String) {
        val prefs = context.getSharedPreferences(Constants.PREFS_NAME1, Context.MODE_PRIVATE)
        prefs.edit().putString(Constants.PREF_KEY_WIDGET_VALUE1, value).apply()
    }
}