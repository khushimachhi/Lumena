package com.example.lumena.data.mood

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MoodEntry::class], version = 1, exportSchema = false)
abstract class MoodDatabase : RoomDatabase() {
    abstract fun moodDao(): MoodDao
}
