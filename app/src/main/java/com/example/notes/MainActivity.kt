package com.example.notes

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

    private fun initializeApp() {



            val used_ids: MutableList<String> = mutableListOf()
            val sharedPref = this.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val all_prefs = sharedPref.all
        editor.clear()
        editor.apply()
//            for ((key, value) in all_prefs) {
//               println("Key: $key, Value: $value")
//                val str_key = key ?: ""
//                if (str_key.startsWith("interval_")){
//                    var id = str_key.split(("_"))[1]
//                    if (!used_ids.contains(id)){
//                        val nameKey = "name_$id"
//                        val interKey = "interval_$id"
//                        val name = sharedPref.getString(nameKey, null)
//                        AlarmScheduler.scheduleAlarm(interKey, this,nameKey )
//                        used_ids.add(id)
//                    }
//
//                }
//
//        }

        }





}



