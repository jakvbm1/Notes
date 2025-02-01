package com.example.notes.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat


const val notificationID = 1
const val channelID = "channel1"

class NotificationReceiver : BroadcastReceiver() { // ✅ Renamed class
    override fun onReceive(context: Context, intent: Intent?) {
        showNotification(context, "Reminder", "This is your scheduled notification!")

        // Retrieve last scheduled interval from SharedPreferences
        val sharedPref = context.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
        val interval = sharedPref.getString("interval", "Daily") ?: "Daily"

        // ✅ Re-schedule the alarm
        AlarmScheduler.scheduleAlarm("minutes", context)
    }

    private fun showNotification(context: Context, title: String, message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "reminder_channel"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Reminders", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(notificationID, notification)
    }
}
