package com.example.notes.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.notes.AlarmScheduler
import com.example.notes.model.entities.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNote(navController: NavController, noteId: Int?) {
    val context = LocalContext.current
    val selectedInterval = "minutes"
    AlarmScheduler.scheduleAlarm(selectedInterval, context)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (noteId == null) "New Note" else "Edit Note",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            Text(text = noteId?.toString() ?: "null")
        }
    }
}
