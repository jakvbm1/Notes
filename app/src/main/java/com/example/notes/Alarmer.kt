package com.example.notes



import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast


object AlarmScheduler {

    fun scheduleAlarm(interval: String, context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(context, "Exact alarms not allowed. Enable in settings.", Toast.LENGTH_LONG).show()
                openAlarmPermissionSettings(context)

                return
            }
        }
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val repeatTime = when (interval) {
            "6Hours" -> 6 * 60 * 60 * 1000L
            "12Hours" -> 12 * 60 * 60 * 1000L
            "Daily" -> 24 * 60 * 60 * 1000L
            "2Daily" -> 2 * 24 * 60 * 60 * 1000L
            "Weekly" -> 7 * 24 * 60 * 60 * 1000L
            "minutes" -> 20 * 1000L
            else -> AlarmManager.INTERVAL_DAY
        }

        val firstTrigger = System.currentTimeMillis() + repeatTime

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            firstTrigger,

            pendingIntent
        )

        Toast.makeText(context, "Notification set for $interval", Toast.LENGTH_SHORT).show()
    }
    private fun openAlarmPermissionSettings(context: Context) {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        intent.data = Uri.parse("package:${context.packageName}")
        context.startActivity(intent)
    }
}
