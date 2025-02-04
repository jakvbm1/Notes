package com.example.notes

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notes.ui.theme.NotesTheme
import com.example.notes.view.AddEditList
import com.example.notes.view.AddEditNote
import com.example.notes.view.NotesScreen
import com.example.notes.view.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkNotificationPermission()
        initializeApp()

        setContent {
            NotesTheme {
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
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is granted, continue with app setup
                return
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // Show rationale before asking again
                showPermissionRationaleDialog()
            } else {
                // Request permission
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                // Permission denied, show settings dialog
                showSettingsDialog()
            }
        }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Notification Permission Required")
            .setMessage("This app needs notification permissions to send alerts.")
            .setPositiveButton("Allow") { _, _ ->
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Enable Notifications")
            .setMessage("Notifications are disabled. Please enable them in Settings.")
            .setPositiveButton("Open Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun initializeApp() {
        val sharedPref = this.getSharedPreferences("AlarmPrefs", Context.MODE_PRIVATE)

        // Instead of using Thread.sleep, we can use a coroutine here.
        CoroutineScope(Dispatchers.IO).launch {
            val allPrefs = sharedPref.all
            for ((key, value) in allPrefs) {
                if (value is Set<*>) {
                    val alarmID = key
                    val interval = value.first().toString()
                    val name = value.elementAt(1).toString()
                    AlarmScheduler.scheduleAlarm(interval, this@MainActivity, name, key)
                }
            }
        }
    }
}
