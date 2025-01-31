package com.example.notes.view

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
import androidx.compose.ui.tooling.preview.Preview
import com.example.notes.model.entities.Note
import com.example.notes.model.entities.Priority
import com.example.notes.model.entities.Type
import java.sql.Date

val notes = listOf(
    Note(
        id = 0,
        name = "title",
        date = Date(123),
        type = Type.work,
        priority = Priority.low,
        hasSubnotes = null,
        description = "note content"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NotesScreen() {
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
                onClick = { /* navigate to AddEditNote */ },
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
                    note,
                    onClick = { /* navigate to AddEditNote */ },
                    onDeleteClick = { /* delete note */ }
                )
            }
        }
    }
}