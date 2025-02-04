package com.example.notes.view

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.notes.model.entities.Intervals
import com.example.notes.model.entities.Priority
import com.example.notes.model.entities.Type
import com.example.notes.view.components.ExpandableList
import com.example.notes.viewmodel.AddEditListVM
import com.example.notes.viewmodel.AddEditListVMFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditList(navController: NavController, noteId: Int?) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: AddEditListVM = viewModel(factory = AddEditListVMFactory(application, noteId))
    var priorityNames = Priority.entries.map{it.name}
    var typeNames = Type.entries.map{it.name}
    var intervalNames = Intervals.entries.map{it.name}
    var selInterval: Intervals = Intervals.daily
    var intervalsVisibility = false

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

                ExpandableList(priorityNames, {selectedPriority -> viewModel.updateNotePriority(selectedPriority)
                    if (selectedPriority==Priority.low.name)
                        intervalsVisibility=false
                    else
                        intervalsVisibility=true})

                Spacer(modifier = Modifier.height(16.dp))

                ExpandableList(typeNames, {selectedName -> viewModel.updateNoteType(selectedName)})

                Spacer(modifier = Modifier.height(16.dp))
                if (intervalsVisibility)
                    ExpandableList(intervalNames, {selectedInterval -> selInterval = Intervals.valueOf(selectedInterval)})

                //Spacer(modifier = Modifier.height(16.dp))

                // Subnotes List
                LazyColumn {
                    itemsIndexed(viewModel.subnotes.value) { index, subnote ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        ) {
                            Checkbox(
                                checked = subnote.isCompleted,
                                onCheckedChange = { viewModel.toggleCompletion(index) }
                            )
                            OutlinedTextField(
                                value = subnote.name,
                                onValueChange = { newValue ->
                                    viewModel.updateSubnote(index, newValue)
                                },
                                label = { Text("Subnote") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Add new subnote button
                OutlinedButton(
                    onClick = { viewModel.addNewSubnote() },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add subnote", tint = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add new list element", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}
