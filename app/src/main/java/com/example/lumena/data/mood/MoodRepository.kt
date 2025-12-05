package com.example.lumena.data.mood

import kotlinx.coroutines.flow.Flow

/**
 * Simple repo interface for Mood data.
 */
interface MoodRepository {
    suspend fun getEntryForDate(dateIso: String): MoodEntry?
    suspend fun saveOrUpdate(entry: MoodEntry)
    suspend fun getLastEntries(limit: Int): List<MoodEntry>
    fun getEntriesBetween(startIso: String, endIso: String): Flow<List<MoodEntry>>
    fun getLatest(limit: Int): Flow<List<MoodEntry>>
}


