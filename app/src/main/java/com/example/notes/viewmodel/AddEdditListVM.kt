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

class AddEditListVMFactory(private val application: Application, private val noteID: Int?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditListVM::class.java)) {
            return AddEditListVM(application, noteID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class AddEditListVM(application: Application, private val noteID: Int?) : AndroidViewModel(application) {
    private val noteRepo: NoteRepository
    private val subnoteRepo: SubnoteRepository

    var note = mutableStateOf<Note?>(null)
        private set

    var subnotes = mutableStateOf<List<Subnote>>(emptyList())
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
                if (fetchedNote.hasSubnotes) {
                    subnotes.value = subnoteRepo.notesSubnotes(fetchedNote).value ?: emptyList()
                }
            } else {
                // Create a new empty note
                note.value = Note(0, "", System.currentTimeMillis(), Type.work, Priority.low, true, "")
            }
        }
    }

    fun updateNoteName(newName: String) {
        note.value?.let {
            note.value = it.copy(name = newName)
        }
    }

    fun updateNoteDescription(newDescription: String) {
        note.value?.let {
            note.value = it.copy(description = newDescription)
        }
    }

    fun updateSubnote(index: Int, newValue: String) {
        val updatedList = subnotes.value.toMutableList()
        updatedList[index] = updatedList[index].copy(name = newValue)
        subnotes.value = updatedList
    }

    fun toggleCompletion(index: Int)
    {
        val updatedList = subnotes.value.toMutableList()
        val temp = updatedList[index].isCompleted
        updatedList[index] = updatedList[index].copy(isCompleted = !temp)
        subnotes.value = updatedList
    }

    fun saveNote(subnoteNames: List<String>) {
        viewModelScope.launch {
            note.value?.let {
                if (noteID == null) {
                    noteRepo.insert(it)
                    for(sub in subnoteNames)
                    {
                        val subnote = Subnote(
                            noteId = note.value!!.id,
                            name = sub,
                            isCompleted = false
                        )
                        subnoteRepo.add(subnote)
                    }


                } else {
                    noteRepo.update(it)
                    for(sub in subnotes.value)
                    {
                        subnoteRepo.update(sub)
                    }
                }
            }
        }
    }
}
