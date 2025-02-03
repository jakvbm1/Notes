package com.example.notes.model.repository

import androidx.lifecycle.LiveData
import com.example.notes.model.daos.SubnoteDao
import com.example.notes.model.entities.Note
import com.example.notes.model.entities.Subnote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SubnoteRepository(private val subnoteDao: SubnoteDao) {

    fun notesSubnotes(note: Note): LiveData<List<Subnote>> {
        return subnoteDao.getNoteSubnotes(note.id)
    }

    suspend fun delete(subnote: Subnote) {
        withContext(Dispatchers.IO) {
            subnoteDao.deleteSubnote(subnote)
        }
    }

    suspend fun update(subnote: Subnote) {
        withContext(Dispatchers.IO) {
            subnoteDao.updateSubnote(subnote)
        }
    }

    suspend fun add(subnote: Subnote) {
        withContext(Dispatchers.IO) {
            subnoteDao.addSubnote(subnote)
        }
    }
}
