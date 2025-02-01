import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.notes.receivers.NotificationReceiver

object AlarmScheduler {

    fun scheduleAlarm(interval: String, context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // ✅ Check if exact alarms are allowed
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
