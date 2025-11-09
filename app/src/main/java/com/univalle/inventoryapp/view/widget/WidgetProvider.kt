package com.univalle.inventoryapp.view.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.univalle.inventoryapp.R
import com.univalle.inventoryapp.view.MainActivity

import android.graphics.Color
import com.univalle.inventoryapp.utils.Constants.PREF_KEY_WIDGET_VALUE1
import com.univalle.inventoryapp.utils.Constants.PREFS_NAME1

class WidgetProvider: AppWidgetProvider() {

    private var inventoryValue="no data loaded"

    companion object {
        const val ACTION_TOGGLE_TEXT = "com.univalle.inventoryapp.widget.TOGGLE_TEXT"
        const val EXTRA_WIDGET_ID = "com.univalle.inventoryapp.extra.WIDGET_ID"
        const val PREFS_NAME ="com.univalle.inventoryapp.widget.WidgetPrefs"
        const val PREF_PREFIX_KEY = "visible_value"
        const val PREF_VALUE_KEY = "inventory_value"
        const val ACTION_CHANGE_TEXT = "com.example.mywidget.CHANGE_TEXT"
        const val ACTION_LOGIN = "com.univalle.inventoryapp.widget.LOGIN"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            /////////////from the actvity////////////////////////
            val prefs = context.getSharedPreferences(PREFS_NAME1, Context.MODE_PRIVATE)
            val appValue = prefs.getString(PREF_KEY_WIDGET_VALUE1, "No data yet") as String
            saveValue(context,appWidgetId,appValue)
            ////////////////////////////////////
            ////////////////////////////////////
            val views = RemoteViews(context.packageName, R.layout.widget)
            // Create intent to trigger a broadcast when the widget is clicked
            val intent = Intent(context, WidgetProvider::class.java).apply {
                action = ACTION_CHANGE_TEXT
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                appWidgetId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            // Set click listener
            views.setOnClickPendingIntent(R.id.widget_title, pendingIntent)


            ///////////////////////////UNCOVERE
            ///////////////////////////UNCOVERED
            val intentToggle = Intent(context, WidgetProvider::class.java).apply {
                action = ACTION_TOGGLE_TEXT
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }

            val pendingIntentToggle = PendingIntent.getBroadcast(
                context,
                appWidgetId,
                intentToggle,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.eye_icon, pendingIntentToggle)

            ///////////////////////////
            ///////////////////////////GO TO APP
//            val intentApp = Intent(context, MainActivity::class.java).apply {
//                action = ACTION_LOGIN
//                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
//            }
            val intentApp = Intent(context, MainActivity::class.java)

            val pendingIntentApp = PendingIntent.getActivity(
                context,
                appWidgetId,
                intentApp,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.settings_icon, pendingIntentApp)

            /////////////////////////// Update widget of all
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)

        val views = RemoteViews(context.packageName, R.layout.widget)
        if (intent.action == ACTION_CHANGE_TEXT) {
            // Random text or toggle between two values
            val newText = listOf("inventario", "inventory", "Nice!").random()
            views.setTextViewText(R.id.widget_title, newText)

        }
        else if (intent.action == ACTION_TOGGLE_TEXT) {
            val current = loadVisibility(context, appWidgetId)
            val newState = !current
            saveVisibility(context, appWidgetId, newState)

//            val inventoryValue0=loadValue(context,appWidgetId)
            val inventoryValue0=loadValueLive(context,appWidgetId)
            if(current){
                views.setTextViewText(R.id.widget_value, "$****")
                views.setImageViewResource(R.id.eye_icon, R.drawable.ic_closed_eye)
            }
            else{
                views.setTextViewText(R.id.widget_value, inventoryValue0)
                views.setImageViewResource(R.id.eye_icon, R.drawable.ic_opened_eye)
            }
        }
        else if (intent.action == ACTION_LOGIN) {
            val launchIntent = Intent(context, MainActivity::class.java)
            launchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(launchIntent)
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
    fun saveVisibility(context: Context, appWidgetId: Int, visible: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(PREF_PREFIX_KEY + appWidgetId, visible).apply()
    }

    private fun loadVisibility(context: Context, appWidgetId: Int): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(PREF_PREFIX_KEY + appWidgetId, false)
    }
    fun saveValue(context: Context, appWidgetId: Int, value: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(PREF_VALUE_KEY + appWidgetId, value).apply()
    }
    private fun loadValue(context: Context, appWidgetId: Int): String? {
//        val prefs = context.getSharedPreferences(PREFS_NAME1, Context.MODE_PRIVATE)
//        val appValue = prefs.getString(PREF_KEY_WIDGET_VALUE1, "No data yet") as String
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(PREF_VALUE_KEY + appWidgetId, "-65.00")
    }
    private fun loadValueLive(context: Context, appWidgetId: Int): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME1, Context.MODE_PRIVATE)
        val appValue = prefs.getString(PREF_KEY_WIDGET_VALUE1, "No data yet") as String
        return appValue
    }
}