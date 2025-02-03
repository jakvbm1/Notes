package com.example.notes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notes.model.AppDatabase
import com.example.notes.model.entities.Note
import com.example.notes.model.entities.Subnote
import com.example.notes.model.repository.NoteRepository
import com.example.notes.model.repository.SubnoteRepository
import kotlinx.coroutines.launch


class NotesScreenVMFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesScreenVM::class.java)) {
            return NotesScreenVM(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


class NotesScreenVM(application: Application) : AndroidViewModel(application) {



    val allNotes: LiveData<List<Note>>
    private val _sortedNotes = MutableLiveData<List<Note>>()
    val sortedNotes: LiveData<List<Note>> get() = _sortedNotes

    private val repository: NoteRepository
    private val subRepository: SubnoteRepository

    init {
        val dao = AppDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(dao)

        val subDao = AppDatabase.getDatabase(application).subnoteDao()
        subRepository = SubnoteRepository(subDao)

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

            if(note.hasSubnotes)
            {


                viewModelScope.launch {val subnotes = subRepository.getNotesSubnotesDirect(note)
                if (subnotes.isNotEmpty())
                {
                    for(sub in subnotes)
                    {
                        subRepository.delete(sub)
                    }

                    repository.delete(note)
                }
                }
            }
            else
            {
                viewModelScope.launch { repository.delete(note) }

            }



        }
    }

