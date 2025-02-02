package com.example.notes.model.entities

import android.os.Parcel
import android.os.Parcelable
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
    @ColumnInfo(name = "add_date") val date: Long,
    @ColumnInfo(name = "type") var type:Type,
    @ColumnInfo(name = "priority") var priority: Priority,
    @ColumnInfo(name="has_subnotes")  var hasSubnotes: Boolean,
    @ColumnInfo(name = "description") var description: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readLong(),
        Type.valueOf(parcel.readString() ?: ""),
        Priority.valueOf(parcel.readString() ?: ""),
        parcel.readByte() != 0.toByte(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeLong(date)
        parcel.writeString(type.name)
        parcel.writeString(priority.name)
        parcel.writeByte(if (hasSubnotes) 1 else 0)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}


