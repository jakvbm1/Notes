package com.example.notes.view

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.notes.AlarmScheduler
import com.example.notes.model.entities.Note
import com.example.notes.model.entities.Priority
import com.example.notes.model.entities.Type

val notes = listOf(
    Note(
        id = 1,
        name = "title",
        date = 999999999999999,
        type = Type.work,
        priority = Priority.low,
        hasSubnotes = false,
        description = "note content"
    ),
    Note(
        id = 2,
        name = "title2",
        date = 99999,
        type = Type.work,
        priority = Priority.low,
        hasSubnotes = false,
        description = "note content2"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Notes", style = MaterialTheme.typography.headlineMedium)
                },
                navigationIcon = {
                    IconButton(onClick = { /* navigate to settings */ }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_edit_note") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(notes) { note ->
                NoteItem(
                    note = note,
                    onClick = { navController.navigate("add_edit_note/${note.id}") },
                    onDeleteClick = { /* delete note */ }
                )
            }
        }
    }
}