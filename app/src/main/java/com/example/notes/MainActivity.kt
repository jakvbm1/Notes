package com.example.notes

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.notes.ui.theme.NotesTheme
import com.example.notes.view.NotesScreen
import java.util.Date

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        createNotificationChannel()
//        scheduleNotification()


        enableEdgeToEdge()
        setContent {
            NotesTheme {
                NotesScreen()
            }
        }
    }


//    private fun scheduleNotification() {
//        val intent = Intent(applicationContext, Notification::class.java)
//        val title = "title"
//        val message = "message"
//        intent.putExtra(titleExtra, title)
//        intent.putExtra(messageExtra, message)
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            applicationContext,
//            notificationID,
//            intent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        var alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val time = getTime()
////        alarmManager.setExactAndAllowWhileIdle(
////                AlarmManager.RTC_WAKEUP,
////                time,
////                pendingIntent
////            )
//
//        showAlert(time, title, message)
//    }
//
//    private fun showAlert(time: Long, title: String, message: String) {
//val date = Date(time)
//val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
//val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)
//AlertDialog.Builder(this).setTitle("notification").setMessage("Title "+ title + "Message: " + message)
//    .setPositiveButton("okay"){_,_->}.show()
//
//    }
//
//    private fun getTime(): Long {
//    val minute = 1
//    val hour = 2
//    val day = 1
//    val month = 2
//    val year = 2025
//
//    var calendar = Calendar.getInstance()
//    calendar.set(year, month, day, hour, minute)
//    return calendar.timeInMillis
//    }
//
//
//    private fun createNotificationChannel() {
//    val name = "notif channel"
//    val desc = "Description"
//    val importance = NotificationManager.IMPORTANCE_DEFAULT
//    val channel = NotificationChannel(channelID, name, importance)
//    channel.description = desc
//    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//    notificationManager.createNotificationChannel(channel)
//    }

}
