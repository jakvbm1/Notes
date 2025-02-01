package com.example.notes.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notes.model.AppDatabase
import com.example.notes.model.entities.Note
import com.example.notes.model.entities.Priority
import com.example.notes.model.entities.Subnote
import com.example.notes.model.entities.Type
import com.example.notes.model.repository.NoteRepository
import com.example.notes.model.repository.SubnoteRepository
import com.example.notes.view.note
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate

class AddEditNoteVM(application: Application, val note: Note?): AndroidViewModel(application)
{
    val noteRepo: NoteRepository
    val subnoteRepo: SubnoteRepository

    val subnotes: LiveData<List<Subnote>>?

    init
    {
     val noteDao = AppDatabase.getDatabase(application).noteDao()
     val subnoteDao = AppDatabase.getDatabase(application).subnoteDao()

        noteRepo = NoteRepository(noteDao)
        subnoteRepo = SubnoteRepository(subnoteDao)

        subnotes = if(note != null) {
            subnoteRepo.notesSubnotes(note)
        } else {
            null
        }

    }

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

    fun addWithSubnotes(name: String, description: String, type:String, priority: String, subnotes: List<String>)
    {

        val note = Note(
            name = name,
            date = System.currentTimeMillis(),
            type = Type.valueOf(type),
            priority = Priority.valueOf(priority),
            hasSubnotes = true,
            description = description
        )

        viewModelScope.launch { noteRepo.insert(note) }

        viewModelScope.launch {
            for(sub in subnotes)
            {
                val subnote = Subnote(noteId = note.id, name = sub, isCompleted = false)
                subnoteRepo.add(subnote)
            }
        }

    }

    fun deleteNote(subnote: Subnote)
    {
        if(note != null)
        {
            if (subnotes == null)
            {
                viewModelScope.launch { noteRepo.delete(note) }
            }

            else
            {
                viewModelScope.launch {
                    val subs = subnotes.value
                    if (subs != null) {
                        for(sub in subs)
                        {
                            subnoteRepo.delete(sub)
                        }
                        noteRepo.delete(note)
                    }
                }
            }
        }
    }
}