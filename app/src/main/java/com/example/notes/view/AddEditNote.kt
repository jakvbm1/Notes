package com.example.notes.view

import AlarmScheduler.scheduleAlarm
import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.notes.model.entities.Intervals
import com.example.notes.model.entities.Priority
import com.example.notes.model.entities.Type
import com.example.notes.view.components.ExpandableList
import com.example.notes.viewmodel.AddEditNoteVM
import com.example.notes.viewmodel.AddEditNoteVMFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNote(navController: NavController, noteId: Int?) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: AddEditNoteVM = viewModel(factory = AddEditNoteVMFactory(application, noteId))
    val priorityNames = Priority.entries.map{it.name}
    val typeNames = Type.entries.map{it.name}
    val intervalNames = Intervals.entries.map{it.name}
    var selInterval: Intervals = Intervals.daily
    var intervalsVisibility by rememberSaveable { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (noteId == null) "New Note" else "Edit Note",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        // Note save button
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if(viewModel.note.value!!.priority == Priority.medium || viewModel.note.value!!.priority == Priority.high){
                        scheduleAlarm(selInterval.name, context, viewModel.note.value!!.name)
                    }

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
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    // Note Name
                    OutlinedTextField(
                        value = viewModel.note.value?.name!!,
                        onValueChange = { viewModel.updateNoteName(it) },
                        label = { Text("Note Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    ExpandableList("Priority", priorityNames) { selectedPriority ->
                        viewModel.updateNotePriority(selectedPriority)
                        intervalsVisibility = selectedPriority != Priority.low.name
                    }
                }

                if (intervalsVisibility) {
                    item {
                        ExpandableList("Notification interval", intervalNames) { selectedInterval ->
                            selInterval = Intervals.valueOf(selectedInterval)
                        }
                    }
                }

                item {
                    ExpandableList("Type", typeNames) { selectedName ->
                        viewModel.updateNoteType(selectedName)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    // Single Note Content
                    OutlinedTextField(
                        value = viewModel.note.value!!.description ?: "",
                        onValueChange = { viewModel.updateNoteDescription(it) },
                        label = { Text("Note Content") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

        }
    }
}
