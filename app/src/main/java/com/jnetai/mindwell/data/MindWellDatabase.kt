package com.jnetai.mindwell.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jnetai.mindwell.data.converter.Converters
import com.jnetai.mindwell.data.dao.JournalDao
import com.jnetai.mindwell.data.dao.MoodDao
import com.jnetai.mindwell.data.entity.JournalEntry
import com.jnetai.mindwell.data.entity.MoodEntry

@Database(
    entities = [MoodEntry::class, JournalEntry::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MindWellDatabase : RoomDatabase() {
    abstract fun moodDao(): MoodDao
    abstract fun journalDao(): JournalDao

    companion object {
        @Volatile
        private var INSTANCE: MindWellDatabase? = null

        fun getInstance(context: Context): MindWellDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MindWellDatabase::class.java,
                    "mindwell_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}