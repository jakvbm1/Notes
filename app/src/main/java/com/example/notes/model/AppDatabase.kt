package com.example.notes.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database")
                    .addMigrations(MIGRATION_1_2) // Apply the migration
                    .build()
                INSTANCE = instance
                instance
            }
        }
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Example: If you added a new column to the Note table
                database.execSQL("ALTER TABLE Note ADD COLUMN notificationid TEXT DEFAULT ''")
            }
        }
    }

}