package com.jnetai.mindwell.data.dao

import androidx.room.*
import com.jnetai.mindwell.data.entity.JournalEntry
import java.time.LocalDate

@Dao
interface JournalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: JournalEntry): Long

    @Update
    suspend fun update(entry: JournalEntry)

    @Delete
    suspend fun delete(entry: JournalEntry)

    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    suspend fun getAll(): List<JournalEntry>

    @Query("SELECT * FROM journal_entries WHERE date = :date ORDER BY date DESC")
    suspend fun getByDate(date: LocalDate): List<JournalEntry>

    @Query("SELECT * FROM journal_entries WHERE id = :id")
    suspend fun getById(id: Long): JournalEntry?

    @Query("SELECT * FROM journal_entries WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY date DESC")
    suspend fun search(query: String): List<JournalEntry>
}