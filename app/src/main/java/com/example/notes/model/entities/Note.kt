package com.example.notes.model.entities

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.sql.Date

enum class Type
{
    work, health, house, education,
}

enum class Priority
{
    low, medium, high
}

class Converters
{
    @TypeConverter
    fun fromType(value: Type): String
    {
        return value.name
    }

    @TypeConverter
    fun toType(value:String): Type
    {
        return Type.valueOf(value)
    }

    @TypeConverter
    fun fromPriority(value: Priority): String
    {
        return value.name
    }

    @TypeConverter
    fun toPriority(value: String): Priority
    {
        return Priority.valueOf(value)
    }
}


@Entity
data class Note(
@PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "add_date") val date: Date,
    @ColumnInfo(name = "type") val type:Type,
    @ColumnInfo(name = "priority") val priority: Priority,
    @ColumnInfo(name="has_subnotes")  var hasSubnotes: Boolean,
    @ColumnInfo(name = "description") val description: String?
)
