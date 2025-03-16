package com.example.organizer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import com.example.organizer.presentation.screens.settings.NotificationHelper

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationHelper = NotificationHelper(context)
        val title = intent.getStringExtra("TITLE") ?: "Напоминание"
        val message = intent.getStringExtra("MESSAGE") ?: "Дедлайн через 1 день"

        if (checkNotificationsEnabled(context)) {
            notificationHelper.showNotification(title, message)
        }
    }

    private fun checkNotificationsEnabled(context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean("notifications_enabled", true)
    }
}