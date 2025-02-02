package com.example.notes

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

fun simpleNotification(context: Context, title:String, Text:String) {

    val notification = NotificationCompat.Builder(context, "reminder_channel")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title)
        .setContentText(Text)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){

            NotificationManagerCompat.from(context).notify(1, notification)
        }
    }else{

        NotificationManagerCompat.from(context).notify(1, notification)
    }
}