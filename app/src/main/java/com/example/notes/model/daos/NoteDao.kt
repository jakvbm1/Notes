package com.example.notes.model.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notes.model.entities.Note

@Dao
interface NoteDao
{
    @Query("Select * from Note")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("Select * from Note where id = (:id)")
    fun getOneID(id: Int): Note

    @Update
    suspend fun updateNote(note: Note)

    @Insert
    suspend fun addNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT MAX(id) FROM Note")
    suspend fun getMaxId(): Int?
}