package com.univalle.inventoryapp.view.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.univalle.inventoryapp.R
import com.univalle.inventoryapp.view.MainActivity

import android.graphics.Color



class WidgetProvider: AppWidgetProvider() {

    private var isVisible = false
    private var inventoryValue="showing"

    companion object {
        const val ACTION_TOGGLE_TEXT = "com.univalle.inventoryapp.widget.TOGGLE_TEXT"
        const val EXTRA_WIDGET_ID = "com.univalle.inventoryapp.extra.WIDGET_ID"
        const val PREFS_NAME = "WidgetPrefs"
        const val PREF_PREFIX_KEY = "visible_value"
        const val ACTION_CHANGE_TEXT = "com.example.mywidget.CHANGE_TEXT"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
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
            val isVisible = loadVisibility(context, appWidgetId)
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
//            val appWidgetManager = AppWidgetManager.getInstance(context)
//            val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
//
//            val views = RemoteViews(context.packageName, R.layout.widget)

            // Random text or toggle between two values
            val newText = listOf("inventario", "inventary", "Nice!").random()
            views.setTextViewText(R.id.widget_title, newText)

//            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
        if (intent.action == ACTION_TOGGLE_TEXT) {
            val appWidgetId = intent.getIntExtra(EXTRA_WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

            val current = loadVisibility(context, appWidgetId)
            val newState = !current
            saveVisibility(context, appWidgetId, newState)

            if(current){
                views.setTextViewText(R.id.widget_value, "$****")
//                views.setImageViewResource(R.id.eye_icon, R.drawable.ic_closed_eye)
            }
            else{
                views.setTextViewText(R.id.widget_value, inventoryValue)
//                views.setImageViewResource(R.id.eye_icon, R.drawable.ic_opened_eye)
            }
//            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
    private fun saveVisibility(context: Context, appWidgetId: Int, visible: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(PREF_PREFIX_KEY + appWidgetId, visible).apply()
    }

    private fun loadVisibility(context: Context, appWidgetId: Int): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(PREF_PREFIX_KEY + appWidgetId, false)
    }

}