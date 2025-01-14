package com.example.notes.model.repository

import androidx.lifecycle.LiveData
import com.example.notes.model.daos.NoteDao
import com.example.notes.model.entities.Note

class NoteRepository(private val noteDao: NoteDao)
{
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note:Note)
    {
        noteDao.addNote(note)
    }

    suspend fun  delete(note: Note)
    {
        noteDao.deleteNote(note)
    }

    suspend fun update(note: Note)
    {
        noteDao.updateNote(note)
    }
}