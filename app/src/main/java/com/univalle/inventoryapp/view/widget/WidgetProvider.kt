package com.univalle.inventoryapp.view.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.univalle.inventoryapp.R
import com.univalle.inventoryapp.repository.InventoryRepository
import com.univalle.inventoryapp.utils.Constants.PREFS_NAME1
import com.univalle.inventoryapp.utils.Prefs
import com.univalle.inventoryapp.utils.PriceFormatter
import com.univalle.inventoryapp.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ACTION_TOGGLE_VISIBILITY = "com.univalle.action.TOGGLE_VISIBILITY"
private const val PREF_IS_VISIBLE_PREFIX = "is_visible_"

@AndroidEntryPoint
class WidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var repository: InventoryRepository

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == ACTION_TOGGLE_VISIBILITY) {

            val widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

            if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                if (Prefs.isLoggedIn(context)) {
                    toggleVisibility(context, widgetId)
                } else {
                    navigateToLogin(context)
                }
            }
        }

        super.onReceive(context, intent)
    }

    private fun toggleVisibility(context: Context, widgetId: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME1, Context.MODE_PRIVATE)
        val isVisible = prefs.getBoolean(PREF_IS_VISIBLE_PREFIX + widgetId, false)
        prefs.edit().putBoolean(PREF_IS_VISIBLE_PREFIX + widgetId, !isVisible).apply()

        val appWidgetManager = AppWidgetManager.getInstance(context)
        updateWidgetComplete(context, appWidgetManager, widgetId)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (widgetId in appWidgetIds) {
            updateWidgetComplete(context, appWidgetManager, widgetId)
        }
    }

    private fun updateWidgetComplete(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int) {
        CoroutineScope(Dispatchers.Main).launch {

            val totalValue = try {
                repository.getTotalInventoryValue()
            } catch (e: Exception) {
                e.printStackTrace()
                0.0
            }

            val formattedPrice = PriceFormatter.formatPrice(totalValue)

            val views = RemoteViews(context.packageName, R.layout.widget)
            val prefs = context.getSharedPreferences(PREFS_NAME1, Context.MODE_PRIVATE)

            val isLoggedIn = Prefs.isLoggedIn(context)
            val isVisible = isLoggedIn && prefs.getBoolean(PREF_IS_VISIBLE_PREFIX + widgetId, false)

            showBalance(views, isVisible, formattedPrice)

            views.setOnClickPendingIntent(R.id.widget_toggle_visibility, getPendingIntentToggle(context, widgetId))

            val pendingIntentNavigation = getPendingIntentNavigate(context, isLoggedIn)
            views.setOnClickPendingIntent(R.id.widget_logo, pendingIntentNavigation)
            views.setOnClickPendingIntent(R.id.widget_manage_text, pendingIntentNavigation)
            views.setOnClickPendingIntent(R.id.widget_settings_icon, pendingIntentNavigation)

            appWidgetManager.updateAppWidget(widgetId, views)
        }
    }

    private fun showBalance(views: RemoteViews, isVisible: Boolean, textoSaldo: String) {
        if (isVisible) {
            views.setTextViewText(R.id.widget_balance, textoSaldo)
            views.setImageViewResource(R.id.widget_toggle_visibility, R.drawable.ic_closed_eye)
        } else {
            views.setTextViewText(R.id.widget_balance, "$ ****")
            views.setImageViewResource(R.id.widget_toggle_visibility, R.drawable.ic_opened_eye)
        }
    }
    private fun getPendingIntentToggle(context: Context, widgetId: Int): PendingIntent {
        val intent = Intent(context, WidgetProvider::class.java).apply {
            action = ACTION_TOGGLE_VISIBILITY
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        }
        return PendingIntent.getBroadcast(
            context, widgetId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getPendingIntentNavigate(context: Context, isLoggedIn: Boolean): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            if (isLoggedIn) {
                putExtra("DESTINATION_FRAGMENT", "HOME")
            } else {
                putExtra("DESTINATION_FRAGMENT", "LOGIN")
            }
        }

        val requestCode = if (isLoggedIn) 100 else 101

        return PendingIntent.getActivity(
            context, requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun navigateToLogin(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("DESTINATION_FRAGMENT", "LOGIN")
            putExtra("IS_FROM_WIDGET", true)
        }
        context.startActivity(intent)
    }
}