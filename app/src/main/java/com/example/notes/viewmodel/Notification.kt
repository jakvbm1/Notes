package com.example.notes.receivers

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build


const val notificationID = 1
const val channelID = "channel1"

class Notification : Application()
{
    override fun onCreate() {
        super.onCreate()
        val channelId = "reminder_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "Reminders", NotificationManager.IMPORTANCE_HIGH)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }}





