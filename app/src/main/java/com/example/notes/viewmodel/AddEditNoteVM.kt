package com.example.notes.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notes.model.AppDatabase
import com.example.notes.model.entities.Note
import com.example.notes.model.entities.Priority
import com.example.notes.model.entities.Subnote
import com.example.notes.model.entities.Type
import com.example.notes.model.repository.NoteRepository
import com.example.notes.model.repository.SubnoteRepository
import kotlinx.coroutines.launch

class AddEditNoteVMFactory(private val application: Application, private val noteID: Int?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditNoteVM::class.java)) {
            return AddEditNoteVM(application, noteID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class AddEditNoteVM(application: Application, private val noteID: Int?) : AndroidViewModel(application) {
    private val noteRepo: NoteRepository
    private val subnoteRepo: SubnoteRepository

    var note = mutableStateOf<Note?>(null)
        private set

    init {
        val noteDao = AppDatabase.getDatabase(application).noteDao()
        val subnoteDao = AppDatabase.getDatabase(application).subnoteDao()

        noteRepo = NoteRepository(noteDao)
        subnoteRepo = SubnoteRepository(subnoteDao)

        viewModelScope.launch {
            if (noteID != null) {
                val fetchedNote = noteRepo.getNote(noteID)
                note.value = fetchedNote
            } else {
                // Create a new empty note
                note.value = Note(0, "", System.currentTimeMillis(), Type.work, Priority.low, false, "", "")
            }
        }
    }

    fun updateNoteName(newName: String) {
        note.value?.let {
            note.value = it.copy(name = newName)
        }
    }

    fun updateNoteType(newType:String)
    {
        note.value?.let {
            note.value = it.copy(type = Type.valueOf(newType))
        }
    }

    fun updateNotePriority(newP:String)
    {
        note.value?.let {
            note.value = it.copy(priority = Priority.valueOf(newP))
        }
    }

    fun updateNoteDescription(newDescription: String) {
        note.value?.let {
            note.value = it.copy(description = newDescription)
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            note.value?.let {
                if (noteID == null) {
                    noteRepo.insert(it)
                } else {
                    noteRepo.update(it)
                }
            }
        }
    }
}
