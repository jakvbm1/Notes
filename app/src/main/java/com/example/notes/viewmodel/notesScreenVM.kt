package com.example.notes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.notes.model.AppDatabase
import com.example.notes.model.entities.Note
import com.example.notes.model.repository.NoteRepository
import kotlinx.coroutines.launch

class NotesScreenVM(application: Application) : AndroidViewModel(application) {
    val allNotes: LiveData<List<Note>>
    private val _sortedNotes = MutableLiveData<List<Note>>()
    val sortedNotes: LiveData<List<Note>> get() = _sortedNotes

    private val repository: NoteRepository

    init {
        val dao = AppDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(dao)

        allNotes = repository.allNotes
        _sortedNotes.value = allNotes.value // Default to unsorted list
    }

    fun orderByID() {
        _sortedNotes.value = allNotes.value?.sortedBy { it.id }
    }
    fun orderByDate() {

        _sortedNotes.value = allNotes.value?.sortedBy { it.date }
    }

    fun orderByName() {
        _sortedNotes.value = allNotes.value?.sortedBy { it.name }
    }

    fun orderByPriority()
    {
        _sortedNotes.value = allNotes.value?.sortedBy { it.priority }
    }

    fun orderByType()
    {
        _sortedNotes.value = allNotes.value?.sortedBy { it.type }
    }

    fun removeNote(note: Note)
    {
        viewModelScope.launch()
        {
            repository.delete(note)
        }
    }
}
