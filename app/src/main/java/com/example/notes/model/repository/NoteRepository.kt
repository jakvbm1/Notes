package com.example.notes.model.repository

import androidx.lifecycle.LiveData
import com.example.notes.model.daos.NoteDao
import com.example.notes.model.entities.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun getNote(id: Int): Note {
        return withContext(Dispatchers.IO) {  // Run database query in the background
            noteDao.getOneID(id)
        }
    }

    suspend fun insert(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.addNote(note)
        }
    }

    suspend fun delete(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.deleteNote(note)
        }
    }

    suspend fun update(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.updateNote(note)
        }
    }
}
