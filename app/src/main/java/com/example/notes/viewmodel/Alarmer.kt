import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import com.example.notes.viewmodel.ReceiveNotif

object AlarmScheduler {

    fun scheduleAlarm(interval: String, context: Context, name: String?, AlarmId: String? = null, deleting:Boolean = false) {
        var alarmId = AlarmId ?: System.currentTimeMillis().toString()
        val sharedPref = context.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
        val data = setOf(interval, name)
        val alarm_ids = sharedPref.all.keys

            if (!alarm_ids.contains(alarmId)){
                with(sharedPref.edit()) {
                    putStringSet("$alarmId", data)
                    apply()

            }
        }


        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(context, "Exact alarms not allowed. Enable in settings.", Toast.LENGTH_LONG).show()
                openAlarmPermissionSettings(context)
                return
            }
        }
        val uniqueRequestCode = name.hashCode()
        val intent = Intent(context, ReceiveNotif::class.java).apply {
            putExtra("alarmId", alarmId.toString())
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            uniqueRequestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val repeatTime = when (interval) {
            "sixhours" -> 6 * 60 * 60 * 1000L
            "twelvehours" -> 12 * 60 * 60 * 1000L
            "daily" -> 24 * 60 * 60 * 1000L
            "twodays" -> 2 * 24 * 60 * 60 * 1000L
            "weekly" -> 7 * 24 * 60 * 60 * 1000L
            "seconds" -> 15  * 1000L
            else -> 24 * 60 * 60 * 1000L
        }

        val firstTrigger = System.currentTimeMillis() + repeatTime

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                firstTrigger,
                pendingIntent
            )
            Toast.makeText(context, "Notification set for $interval", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Toast.makeText(context, "Exact alarm permission required", Toast.LENGTH_LONG).show()
            openAlarmPermissionSettings(context)
        }
    }

    // ✅ Opens app settings where the user can allow exact alarms
    private fun openAlarmPermissionSettings(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intent.data = android.net.Uri.parse("package:${context.packageName}")
            context.startActivity(intent)
        }
    }

}

