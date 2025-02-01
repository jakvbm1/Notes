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
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notes.ui.theme.NotesTheme
import com.example.notes.view.AddEditNote
import com.example.notes.view.NotesScreen
import java.util.Date

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
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
                }
            )
        }
    }

}
