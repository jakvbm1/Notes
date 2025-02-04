package com.example.notes

import AlarmScheduler
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notes.ui.theme.NotesTheme
import com.example.notes.view.AddEditList
import com.example.notes.view.AddEditNote
import com.example.notes.view.NotesScreen
import com.example.notes.view.Settings
import kotlinx.coroutines.delay
import java.util.Date

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeApp()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "notes_screen",
                builder = {
                    composable("notes_screen") {
                        NotesScreen(navController)
                    }
                    composable("add_edit_note/{noteId}") {
                        val noteId = it.arguments?.getString("noteId")?.toIntOrNull()
                        AddEditNote(navController, noteId)
                    }
                    composable("add_edit_note") {
                        AddEditNote(navController, null)
                    }
                    composable("add_edit_list/{noteId}") {
                        val noteId = it.arguments?.getString("noteId")?.toIntOrNull()
                        AddEditList(navController, noteId)
                    }
                    composable("add_edit_list") {
                        AddEditList(navController, null)
                    }
                    composable("settings_route") {
                        Settings(navController)
                    }
                }
            )
        }
    }

    private  fun initializeApp() {
        val sharedPref = this.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val all_prefs = sharedPref.all
//        editor.clear()
//        editor.apply()
            for ((key, value) in all_prefs) {
                println(value.toString())
                if (value is Set<*>){
                    if (value !=null){
                        val alarmID = key
                        val interval = value.first().toString()
                        val name = value.elementAt(1).toString()
                        AlarmScheduler.scheduleAlarm(interval, this, name,key)
                        Thread.sleep(2000L)
                    }
                }

                }

        }

        }




