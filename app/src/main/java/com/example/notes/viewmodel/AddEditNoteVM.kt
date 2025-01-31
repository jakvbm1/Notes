package com.example.notes.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.model.AppDatabase
import com.example.notes.model.entities.Note
import com.example.notes.model.entities.Priority
import com.example.notes.model.entities.Type
import com.example.notes.model.repository.NoteRepository
import com.example.notes.model.repository.SubnoteRepository
import com.example.notes.view.note
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate

class AddEditNoteVM(application: Application): AndroidViewModel(application)
{
    val noteRepo: NoteRepository
    val subnoteRepo: SubnoteRepository

    init
    {
     val noteDao = AppDatabase.getDatabase(application).noteDao()
     val subnoteDao = AppDatabase.getDatabase(application).subnoteDao()

        noteRepo = NoteRepository(noteDao)
        subnoteRepo = SubnoteRepository(subnoteDao)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addWithoutSubnotes(name: String, description: String, type:String, priority: String)
    {

        val note = Note(
            name = name,
            date = System.currentTimeMillis(),
            type = Type.valueOf(type),
            priority = Priority.valueOf(priority),
            hasSubnotes = false,
            description = description
        )

        viewModelScope.launch { noteRepo.insert(note) }
    }
}