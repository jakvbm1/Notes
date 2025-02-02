import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.notes.R
import com.example.notes.ReceiveNotif

object AlarmScheduler {

    fun scheduleAlarm(interval: String, context: Context, name: String) {
        val sharedPref = context.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
        val currentInterval = sharedPref.getString("interval", "Daily")
        val currentName = sharedPref.getString("name", "event1")
        val alarmId = System.currentTimeMillis()

        if (interval != currentInterval || name != currentName) {
            // Only update SharedPreferences if the values have changed
            with(sharedPref.edit()) {
                putString("interval_$alarmId", interval) // Save interval with unique key
                putString("name_$alarmId", name) // Save name with unique key
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
            "6Hours" -> 6 * 60 * 60 * 1000L
            "12Hours" -> 12 * 60 * 60 * 1000L
            "Daily" -> 24 * 60 * 60 * 1000L
            "2Daily" -> 2 * 24 * 60 * 60 * 1000L
            "Weekly" -> 7 * 24 * 60 * 60 * 1000L
            "minutes" -> 15  * 1000L
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

