package com.example.lumena.data.mood

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {

    @Query("SELECT * FROM mood_entries WHERE dateIso = :dateIso LIMIT 1")
    suspend fun getEntryForDate(dateIso: String): MoodEntry?


    //Insert new entry. We will use dateIso uniqueness on higher layer (replace by id or use repo logic).
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: MoodEntry): Long

    @Update
    suspend fun update(entry: MoodEntry)


     //Get entries between start and end inclusive (dates in ISO yyyy-MM-dd). Returned as Flow.
    @Query("SELECT * FROM mood_entries WHERE dateIso BETWEEN :startIso AND :endIso ORDER BY dateIso DESC")
    fun getEntriesBetween(startIso: String, endIso: String): Flow<List<MoodEntry>>


     // Convenience: latest N entries
    @Query("SELECT * FROM mood_entries ORDER BY dateIso DESC LIMIT :limit")
    fun getLatest(limit: Int): Flow<List<MoodEntry>>
}
