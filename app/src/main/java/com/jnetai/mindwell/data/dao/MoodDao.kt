package com.jnetai.mindwell.data.dao

import androidx.room.*
import com.jnetai.mindwell.data.entity.MoodEntry
import java.time.LocalDate

@Dao
interface MoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mood: MoodEntry): Long

    @Update
    suspend fun update(mood: MoodEntry)

    @Query("SELECT * FROM mood_entries WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: LocalDate): MoodEntry?

    @Query("SELECT * FROM mood_entries ORDER BY date DESC")
    suspend fun getAll(): List<MoodEntry>

    @Query("SELECT * FROM mood_entries WHERE date BETWEEN :start AND :end ORDER BY date ASC")
    suspend fun getByDateRange(start: LocalDate, end: LocalDate): List<MoodEntry>

    @Query("DELETE FROM mood_entries WHERE id = :id")
    suspend fun delete(id: Long)
}