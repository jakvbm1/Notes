package com.example.notes

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class ReceiveNotif : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val alarmId = intent?.getStringExtra("alarmId") ?: return



        // Retrieve the interval and name for this specific alarm from SharedPreferences
        val sharedPref = context.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
        val data = sharedPref.getStringSet(alarmId, setOf())
        if (data != null) {
            if (data.size>0){
                val interval = data.first()
                val name = data.elementAt(1)
                simpleNotification(context, name, "This is your scheduled notification!")
                AlarmScheduler.scheduleAlarm(interval, context, name, alarmId)
            }
        }





    }

}