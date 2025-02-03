package com.example.notes.model.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notes.model.entities.Subnote

@Dao
interface SubnoteDao
{
    @Query("select * from subnote")
    fun getAllSubnotes(): LiveData<List<Subnote>>

    @Query("select * from subnote where note_id = (:nId)")
    fun getNoteSubnotes(nId: Int): LiveData<List<Subnote>>

    @Update
    suspend fun updateSubnote(subnote: Subnote)

    @Insert
    fun addSubnote(subnote: Subnote)

    @Delete
    fun deleteSubnote(subnote: Subnote)

    @Query("SELECT * FROM subnote WHERE note_id = (:nId)")
    suspend fun getNoteSubnotesDirect(nId: Int): List<Subnote> // Change return type to List<Subnote>


}