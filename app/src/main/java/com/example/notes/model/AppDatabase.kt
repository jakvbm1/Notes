package com.example.notes.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notes.model.daos.NoteDao
import com.example.notes.model.daos.SubnoteDao
import com.example.notes.model.entities.Note
import com.example.notes.model.entities.Subnote

@Database(entities = [Note::class, Subnote::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase()
{
    abstract fun noteDao(): NoteDao
    abstract fun subnoteDao(): SubnoteDao

    companion object
    {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase
        {
            return INSTANCE?: synchronized(this)
            {
                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database").build()
                INSTANCE = instance
                instance
            }
        }
    }
    
}