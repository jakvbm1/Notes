package com.example.notes.view

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.notes.view.components.ExpandableList
import com.example.notes.viewmodel.AddEditNoteVM
import com.example.notes.viewmodel.AddEditNoteVMFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNote(navController: NavController, noteId: Int?) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: AddEditNoteVM = viewModel(factory = AddEditNoteVMFactory(application, noteId))

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
        },
        // Note save button
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.saveNote()
                    Toast.makeText(context, "Note Saved", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Save Note")
            }
        }
    ) { paddingValues ->

        if (viewModel.note.value == null) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        } else {
            Column(Modifier.padding(paddingValues).padding(16.dp)) {
                // Note Name
                OutlinedTextField(
                    value = viewModel.note.value?.name!!,
                    onValueChange = { viewModel.updateNoteName(it) },
                    label = { Text("Note Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                //ExpandableList() for priority

                //Spacer(modifier = Modifier.height(16.dp))

                //ExpandableList() for type

                //Spacer(modifier = Modifier.height(16.dp))

                // Single Note Content
                OutlinedTextField(
                    value = viewModel.note.value!!.description ?: "",
                    onValueChange = { viewModel.updateNoteDescription(it) },
                    label = { Text("Note Content") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5
                )
            }
        }
    }
}
